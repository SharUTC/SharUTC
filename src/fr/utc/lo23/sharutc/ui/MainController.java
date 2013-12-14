package fr.utc.lo23.sharutc.ui;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.cathive.fx.guice.GuiceFXMLLoader.Result;
import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.account.DisconnectionCommand;
import fr.utc.lo23.sharutc.controler.command.account.ExportProfileCommand;
import fr.utc.lo23.sharutc.controler.command.player.AddToPlaylistCommand;
import fr.utc.lo23.sharutc.controler.command.player.RemoveFromPlaylistCommand;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.PlayerService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelImpl;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.PlayListListCell;
import fr.utc.lo23.sharutc.ui.custom.PlayListMusic;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import fr.utc.lo23.sharutc.ui.navigation.NavigationController;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends NavigationController implements Initializable,
        PeopleHomeController.IPeopleHomeController,
        SearchResultController.ISearchResultController,
        ArtistsDetailController.IArtistsDetailController,
        AlbumsDetailController.IAlbumsDetailController,
        SongSelectorController.ISongListController,
        SongDetailController.ISongDetailController,
        PeopleDetailController.IPeopleDetailController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    @Inject
    private GuiceFXMLLoader mFxmlLoader;
    @Inject
    private AddToPlaylistCommand mAddToPlaylistCommand;
    /**
     * the drag Preview
     */
    private StackPane mDragPreview;
    private PlayerController mPlayerController;
    private Result mCurrentLoadedRighpaneResult;
    public Button songsbutton;
    public Button peoplebutton;
    public Button artistsbutton;
    public Button albumsbutton;
    public Button logoutButton;
    public Button exportButton;
    public Pane rightpane;
    public HBox bottombar;
    public Region dropOverlay;
    public Label dropOverlayLabel;
    public Label labelMyProfile;
    public ProgressIndicator exportProgress;
    public TextField textFieldSearch;
    @Inject
    private AppModel mAppModel;
    @Inject
    private DisconnectionCommand mDisconnectionCommand;
    public ObservableList<PlayListMusic> mPlayListData;
    @Inject
    private PlayerService mPlayerService;
    private CollectionChangeListener mChangeListenerPlayList;
    private PropertyChangeListener mChangeListenerAppModel;
    @Inject
    private ExportProfileCommand mExportProfileCommand;
    @Inject
    private RemoveFromPlaylistCommand mRemoveFromPlaylistCommand;
    @Inject
    private FileService mFileService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addListeners();

        try {
            //Load the music player
            final Result loadingResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/player.fxml"));
            mPlayerController = loadingResult.getController();
            bottombar.getChildren().add((Node) loadingResult.getRoot());
            initializePlayList();
            mPlayerController.setPlayList(mPlayListData);
        } catch (IOException exception) {
            log.error(exception.getMessage());
        }

        mDragPreview = new StackPane();
        mDragPreview.setOpacity(0.6);
        mDragPreview.setMouseTransparent(true);
        mDragPreview.toFront();

        //Set User name
        final UserInfo currentUser = mAppModel.getProfile().getUserInfo();
        labelMyProfile.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        labelMyProfile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //show the details of the user
                MainController.this.onPeopleDetailRequested(mAppModel.getProfile().getUserInfo());
            }
        });
        
        showLocalCatalog();
    }

    /**
     * all init which need scene
     */
    public void sceneCreated() {
        final AnchorPane root = (AnchorPane) rightpane.getScene().getRoot();
        //add a Drag Handler to move the drag preview
        root.setOnDragOver(new javafx.event.EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                Point2D localPoint = root.sceneToLocal(new Point2D(dragEvent.getSceneX(), dragEvent.getSceneY()));
                mDragPreview.relocate(
                        (int) (localPoint.getX() - mDragPreview.getBoundsInParent().getWidth() / 2),
                        (int) (localPoint.getY() - mDragPreview.getBoundsInParent().getHeight() / 2));
                dragEvent.consume();
            }
        });
        root.getChildren().add(mDragPreview);
    }

    /**
     * display the overlay on the player to inform that the song can be dropped
     * here
     *
     * @param visible
     */
    public void displayBottomBarOverlay(boolean visible) {
        dropOverlay.setVisible(visible);
        dropOverlayLabel.setVisible(visible);
    }

    @FXML
    private void handleMenuButtonAction(ActionEvent event) throws IOException {

        if (event.getSource() == songsbutton) {
            log.debug("song button clicked");
            showLocalCatalog();
        } else if (event.getSource() == peoplebutton) {
            log.debug("people button clicked");
            showPeopleList();
        } else if (event.getSource() == artistsbutton) {
            log.debug("artist button clicked");
            showArtistList();
        } else if (event.getSource() == albumsbutton) {
            log.debug("album button clicked");
            showAlbumList();
        } else if (event.getSource() == logoutButton) {
            log.debug("logout button clicked");
            mDisconnectionCommand.execute();
        } else if (event.getSource() == exportButton) {
            log.debug("export button clicked");
            exportProfile();
        }
    }

    private void exportProfile() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Export Profile");
        final File directory = directoryChooser.showDialog(exportButton.getScene().getWindow());

        if (directory != null) {
            log.debug("export file to : " + directory.getAbsolutePath());

            final String usersPath = mFileService.getAppFolder() + FileService.ROOT_FOLDER_USERS;
            final String login = mAppModel.getProfile().getUserInfo().getLogin();
            final String dest = directory.getAbsolutePath() + File.separator + login + ".zip";

            final Task<Void> exportTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    mExportProfileCommand.setDestFolder(dest);
                    mExportProfileCommand.setSrcFile(usersPath + File.separator + login);
                    mExportProfileCommand.execute();
                    return null;
                }
            };

            exportTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent t) {
                    exportProgress.setVisible(false);
                    exportButton.setVisible(true);
                    log.debug("export finished !");
                }
            });

            exportProgress.setVisible(true);
            exportButton.setVisible(false);
            new Thread(exportTask, "Profile Export").start();
        }

    }

    private void removeListeners() {
        mPlayerService.getPlaylist().removePropertyChangeListener(mChangeListenerPlayList);
        mAppModel.removePropertyChangeListener(mChangeListenerAppModel);
    }

    private void addListeners() {
        //Listen to playlist changes
        mChangeListenerPlayList = new CollectionChangeListener() {
            @Override
            public void collectionChanged(CollectionEvent ev) {
                log.info("Playlist Change : " + ev.getType());
                switch (ev.getType()) {

                    case ADD:
                        mPlayListData.add(ev.getIndex(), new PlayListMusic((Music) ev.getItem()));
                        break;
                    case REMOVE:
                        mPlayListData.remove(ev.getIndex());
                        break;
                    case CLEAR:
                        mPlayListData.clear();
                        break;
                    case UPDATE:
                        mPlayListData.remove(ev.getIndex());
                        mPlayListData.add(ev.getIndex(), new PlayListMusic((Music) ev.getItem()));
                        break;

                }
            }
        };
        mPlayerService.getPlaylist().addPropertyChangeListener(mChangeListenerPlayList);

        //Listen to appmodel changes
        mChangeListenerAppModel = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                final String propertyName = evt.getPropertyName();
                log.debug("AppModel propertyChange : " + propertyName);
                if (AppModelImpl.Property.PROFILE.name().equals(propertyName)) {
                    if (evt.getNewValue() == null) {
                        log.debug("logout trigered");
                        logout();
                    }
                } else if (AppModelImpl.Property.LOCAL_CATALOG.name().equals(propertyName)) {
                    showLocalCatalog();
                }
            }
        };
        mAppModel.addPropertyChangeListener(mChangeListenerAppModel);
    }

    private void logout() {
        mPlayerController.onDetach();
        removeListeners();
        mNavigationHandler.goToLoginPage();
    }

    public void close() {
        mPlayerController.onDetach();
        removeListeners();
        mDisconnectionCommand.execute();
    }

    public void detachRightpane() {
        if (mCurrentLoadedRighpaneResult != null && mCurrentLoadedRighpaneResult.getController() instanceof RighpaneInterface) {
            ((RighpaneInterface) mCurrentLoadedRighpaneResult.getController()).onDetach();
        }
        ObservableList<Node> children = rightpane.getChildren();
        children.clear();
    }

    public void attachRightpane(Result mCurrentLoadedRighpaneResult) {
        ObservableList<Node> children = rightpane.getChildren();
        if (mCurrentLoadedRighpaneResult != null) {
            children.add((Node) mCurrentLoadedRighpaneResult.getRoot());
        }
    }

    @FXML
    public void handleTextEntered(ActionEvent actionEvent) {
        showSearchResult();
    }

    @FXML
    public void handleBottomBarDragEntered(DragEvent dragEvent) {
        final String dragKey = dragEvent.getDragboard().getString();
        if (dragKey.equals(SongCard.DROP_KEY)) {
            //display drag overlay
            displayBottomBarOverlay(true);
            dragEvent.consume();
        }
    }

    @FXML
    public void handleBottomBarDragOver(DragEvent dragEvent) {
        final String dragKey = dragEvent.getDragboard().getString();
        if (dragKey.equals(SongCard.DROP_KEY)) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        //don't consume because root uses it to relocate preview
        //dragEvent.consume();
    }

    @FXML
    public void handleBottomBarDragDropped(DragEvent dragEvent) {
        final Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        //id droppable is a SongCard
        if (db.hasString() && db.getString().equals(SongCard.DROP_KEY)) {
            final SongCard droppedCard = (SongCard) dragEvent.getGestureSource();

            //if the card comes from SongSelectorController
            if (mCurrentLoadedRighpaneResult.getController() instanceof SongSelectorController || mCurrentLoadedRighpaneResult.getController() instanceof SearchResultController) {
                //Add all selected song to the player
                final ArrayList<SongCard> songs = ((SongSelectorController) mCurrentLoadedRighpaneResult.getController()).getSelectedSong();

                List<Music> musics = new ArrayList<Music>();
                for (SongCard songCard : songs) {
                    //mPlayerController.addSong(songCard.getMusic());
                    musics.add(songCard.getModel());
                    songCard.dropped();
                }
                mAddToPlaylistCommand.setMusics(musics);

            } else {
                //only add selected song to the player
                //mPlayerController.addSong(droppedCard.getMusic());
                mAddToPlaylistCommand.setMusic(droppedCard.getModel());
            }
            mAddToPlaylistCommand.execute();
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    @FXML
    public void handleBottomBarDragExited(DragEvent dragEvent) {
        //hide drag overlay
        displayBottomBarOverlay(false);
    }

    @Override
    public void onPeopleDetailRequested(UserInfo user) {
        log.info("people detail requested : " + user.getLogin());
        showPeopleDetail(user);
    }

    @Override
    public void onGroupRightsRequested(Category category) {
        log.info("Group Rights requested : " + category.getName());
        showGroupRights(category);
    }

    @Override
    public void onArtistDetailRequested(String artistName, SongDetailController.CatalogType type) {
        log.info("Artist detail requested : " + artistName);
        showAlbumListWithArtistFilter(artistName, type);
    }

    @Override
    public void onAlbumDetailRequested(String albumName, SongDetailController.CatalogType type) {
        log.info("Album detail requested : " + albumName);
        showLocalCatalogWithAlbumFilter(albumName, type);
    }

    //Need some improvements
    private void initializePlayList() {
        final ListView listView = new ListView();
        listView.setOrientation(Orientation.HORIZONTAL);
        listView.setId("playList");
        HBox.setHgrow(listView, Priority.ALWAYS);
        bottombar.getChildren().add(listView);

        mPlayListData = FXCollections.observableArrayList();

        listView.setItems(mPlayListData);


        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                final Object target = event.getTarget();
                if (target instanceof ImageView) {
                    ImageView skin = (ImageView) target;
                    if (skin.getId().equals("deleteButton")) {
                        List<Integer> indexs = new ArrayList<Integer>();
                        Music m = ((PlayListMusic) listView.getSelectionModel().getSelectedItem()).music;
                        indexs.add(listView.getSelectionModel().getSelectedIndex());
                        mRemoveFromPlaylistCommand.setMusicsIndex(indexs);
                        mRemoveFromPlaylistCommand.execute();
                    }

                } else if (event.getClickCount() > 1) {
                    mPlayerService.playMusicFromPlaylist(listView.getSelectionModel().getSelectedIndex());
                }
            }
        });

        listView.setCellFactory(new Callback<ListView<PlayListMusic>, ListCell<PlayListMusic>>() {
            @Override
            public ListCell<PlayListMusic> call(ListView<PlayListMusic> p) {
                return new PlayListListCell();
            }
        });

        listView.getSelectionModel().select(1);
    }

    @Override
    public void onSongDetailRequested(final Music music) {
        log.info("Music detail requested : " + music.getAlbum());
        showSongDetailWithComment(music);
    }

    @Override
    public void onSongRemovedFromLocalCatalog() {
        showLocalCatalog();
    }

    @Override
    public void onTagDetailRequested(Music music) {
        showSongDetailWithTag(music);
    }

    @Override
    public void onTagFilterRequested(String tagName) {
        showLocalCatalogWithTagFilter(tagName);
    }

    private void showGroupRights(Category category) {
        if (loadDragPreviewDrawer("/fr/utc/lo23/sharutc/ui/fxml/group_rights.fxml")) {
            ((GroupRightController) mCurrentLoadedRighpaneResult.getController()).setGroupInfo(category);
        }
    }

    private void showPeopleDetail(UserInfo userInfo) {
        if (loadDragPreviewDrawer("/fr/utc/lo23/sharutc/ui/fxml/people_detail.fxml")) {
            ((PeopleDetailController) mCurrentLoadedRighpaneResult.getController()).setUserInfo(userInfo);
            ((PeopleDetailController) mCurrentLoadedRighpaneResult.getController()).setInterface(this);
        }
    }

    private void showSearchResult() {
        if (loadDragPreviewDrawer("/fr/utc/lo23/sharutc/ui/fxml/searchresult_detail.fxml")) {
            ((SearchResultController) mCurrentLoadedRighpaneResult.getController()).setInterface(this);
            ((SearchResultController) mCurrentLoadedRighpaneResult.getController()).searchAll(textFieldSearch.getText());
        }
    }

    private void showArtistList() {
        try {
            detachRightpane();
            mCurrentLoadedRighpaneResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/artists_detail.fxml"));
            ((ArtistsDetailController) mCurrentLoadedRighpaneResult.getController()).setInterface(this);
            attachRightpane(mCurrentLoadedRighpaneResult);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void showPeopleList() {
        if (loadDragPreviewDrawer("/fr/utc/lo23/sharutc/ui/fxml/people_home.fxml")) {
            ((PeopleHomeController) mCurrentLoadedRighpaneResult.getController()).setInterface(this);
        }
    }

    private void showSongDetailWithComment(final Music music) {
        if (loadSongDetail(music)) {
            ((SongDetailController) mCurrentLoadedRighpaneResult.getController()).showComments();
        }
    }

    private void showSongDetailWithTag(final Music music) {
        if (loadSongDetail(music)) {
            ((SongDetailController) mCurrentLoadedRighpaneResult.getController()).showTags();
        }
    }

    private boolean loadSongDetail(final Music music) {
        boolean success = loadDragPreviewDrawer("/fr/utc/lo23/sharutc/ui/fxml/song_detail.fxml");
        if (success) {
            ((SongDetailController) mCurrentLoadedRighpaneResult.getController()).setInterface(this);
            ((SongDetailController) mCurrentLoadedRighpaneResult.getController()).setMusic(music);
        }
        return success;
    }

    private void showAlbumListWithArtistFilter(String artistName, SongDetailController.CatalogType type) {
        if (loadAlbumList()) {
            ((AlbumsDetailController) mCurrentLoadedRighpaneResult.getController()).showAlbumsWithArtistFilter(artistName, type);
        }
    }

    private void showAlbumList() {
        if (loadAlbumList()) {
            ((AlbumsDetailController) mCurrentLoadedRighpaneResult.getController()).showAlbums();
        }
    }

    private boolean loadAlbumList() {
        boolean success = false;
        try {
            detachRightpane();
            mCurrentLoadedRighpaneResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/albums_detail.fxml"));
            ((AlbumsDetailController) mCurrentLoadedRighpaneResult.getController()).setInterface(this);
            attachRightpane(mCurrentLoadedRighpaneResult);
            success = true;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return success;
    }

    private void showLocalCatalogWithTagFilter(String tagName) {
        if (loadSongList()) {
            ((SongListController) mCurrentLoadedRighpaneResult.getController()).showLocalCatalogWithTagFilter(tagName);
        }
    }

    private void showLocalCatalogWithAlbumFilter(String albumName, SongDetailController.CatalogType type) {
        if (loadSongList()) {
            ((SongListController) mCurrentLoadedRighpaneResult.getController()).showLocalCatalogWithAlbumFilter(albumName, type);
        }
    }

    private void showLocalCatalog() {
        if (loadSongList()) {
            ((SongListController) mCurrentLoadedRighpaneResult.getController()).showLocalCatalog();
        }
    }

    private boolean loadSongList() {
        boolean success = false;
        if (loadDragPreviewDrawer("/fr/utc/lo23/sharutc/ui/fxml/song_list.fxml")) {
            ((SongListController) mCurrentLoadedRighpaneResult.getController()).setInterface(this);
            success = true;
        }
        return success;
    }

    private boolean loadDragPreviewDrawer(String fxmlResource) {
        boolean success = false;
        try {
            detachRightpane();
            mCurrentLoadedRighpaneResult = mFxmlLoader.load(getClass().getResource(fxmlResource));
            ((DragPreviewDrawer) mCurrentLoadedRighpaneResult.getController()).init(mDragPreview);
            attachRightpane(mCurrentLoadedRighpaneResult);
            success = true;
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return success;
    }
}

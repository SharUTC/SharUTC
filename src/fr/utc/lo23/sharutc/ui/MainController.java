package fr.utc.lo23.sharutc.ui;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.cathive.fx.guice.GuiceFXMLLoader.Result;
import com.google.inject.Inject;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.PlayListListCell;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable, PeopleHomeController.IPeopleHomeController, SearchResultController.ISearchResultController, ArtistsDetailController.IArtistsDetailController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    @Inject
    private GuiceFXMLLoader mFxmlLoader;
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
    public Pane rightpane;
    public HBox bottombar;
    public Region dropOverlay;
    public Label dropOverlayLabel;

    //TODO Remove once we get a real list of Musics
    static public ArrayList<Music> population;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO Remove once we get a real list of Musics
        population = new ArrayList();
        populateMusics();


        try {
            final Result loadingResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/player.fxml"));
            mPlayerController = loadingResult.getController();
            bottombar.getChildren().add((Node) loadingResult.getRoot());

            initializePlayList();

        } catch (IOException exception) {
        }

        mDragPreview = new StackPane();
        mDragPreview.setOpacity(0.6);
        mDragPreview.setMouseTransparent(true);
        mDragPreview.toFront();

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
     * display the overlay on the player to inform that the song can be dropped here
     *
     * @param visible
     */
    public void displayBottomBarOverlay(boolean visible) {
        dropOverlay.setVisible(visible);
        dropOverlayLabel.setVisible(visible);
    }

    @FXML
    private void handleMenuButtonAction(ActionEvent event) throws IOException {

        ObservableList<Node> children = rightpane.getChildren();
        children.clear();

        if (event.getSource() == songsbutton) {
            mCurrentLoadedRighpaneResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/song_list.fxml"));
            ((DragPreviewDrawer) mCurrentLoadedRighpaneResult.getController()).init(mDragPreview);
        } else if (event.getSource() == peoplebutton) {
            mCurrentLoadedRighpaneResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/people_home.fxml"));
            ((PeopleHomeController) mCurrentLoadedRighpaneResult.getController()).setInterface(this);
            ((DragPreviewDrawer) mCurrentLoadedRighpaneResult.getController()).init(mDragPreview);
        } else if (event.getSource() == artistsbutton) {
            mCurrentLoadedRighpaneResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/artists_detail.fxml"));
            ((ArtistsDetailController) mCurrentLoadedRighpaneResult.getController()).setInterface(this);
        } else if (event.getSource() == albumsbutton) {
            mCurrentLoadedRighpaneResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/albums_detail.fxml"));
            ((AlbumsDetailController) mCurrentLoadedRighpaneResult.getController()).createCards();
        }

        if (mCurrentLoadedRighpaneResult != null) {
            children.add((Node) mCurrentLoadedRighpaneResult.getRoot());
        }
    }

    @FXML
    public void handleTextEntered(ActionEvent actionEvent) throws IOException {
        ObservableList<Node> children = rightpane.getChildren();
        children.clear();
        final Result loadingResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/searchresult_detail.fxml"));
        ((SearchResultController) loadingResult.getController()).setInterface(this);
        children.add((Node) loadingResult.getRoot());

    }

    @FXML
    public void handleBottomBarDragEntered(DragEvent dragEvent) throws IOException {
        final String dragKey = dragEvent.getDragboard().getString();
        if (dragKey.equals(SongCard.DROP_KEY)) {
            //display drag overlay
            displayBottomBarOverlay(true);
            dragEvent.consume();
        }
    }

    @FXML
    public void handleBottomBarDragOver(DragEvent dragEvent) throws IOException {
        final String dragKey = dragEvent.getDragboard().getString();
        if (dragKey.equals(SongCard.DROP_KEY)) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        //don't consume because root uses it to relocate preview
        //dragEvent.consume();
    }

    @FXML
    public void handleBottomBarDragDropped(DragEvent dragEvent) throws IOException {
        final Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        //id droppable is a SongCard
        if (db.hasString() && db.getString().equals(SongCard.DROP_KEY)) {
            final SongCard droppedCard = (SongCard) dragEvent.getGestureSource();
            //if the card comes from SongSelectorController
            if (mCurrentLoadedRighpaneResult.getController() instanceof SongSelectorController) {
                //Add all selected song to the player
                final ArrayList<SongCard> songs =
                        ((SongSelectorController) mCurrentLoadedRighpaneResult.getController()).getSelectedSong();
                for (SongCard songCard : songs) {
                    mPlayerController.addSong(songCard.getModel());
                    songCard.dropped();
                }
            } else {
                //only add selected song to the player
                mPlayerController.addSong(droppedCard.getModel());
            }
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    @FXML
    public void handleBottomBarDragExited(DragEvent dragEvent) throws IOException {
        //hide drag overlay
        displayBottomBarOverlay(false);
    }

    @Override
    public void onPeopleDetailRequested(UserInfo user) {
        ObservableList<Node> children = rightpane.getChildren();
        children.clear();
        log.info("people detail requested : " + user.getLogin());
        try {
            mCurrentLoadedRighpaneResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/people_detail.fxml"));
            ((PeopleDetailController) mCurrentLoadedRighpaneResult.getController()).setUserInfo(user);
            ((DragPreviewDrawer) mCurrentLoadedRighpaneResult.getController()).init(mDragPreview);
            children.add((Node) mCurrentLoadedRighpaneResult.getRoot());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onGroupDetailRequested() {
    }

    @Override
    public void onArtistDetailRequested(Music music) {
        ObservableList<Node> children = rightpane.getChildren();
        children.clear();
        log.info("Artist detail requested : " + music.getArtist());
        try {
            mCurrentLoadedRighpaneResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/albums_detail.fxml"));
            ((AlbumsDetailController) mCurrentLoadedRighpaneResult.getController()).setArtistWanted(music.getArtist());
            ((AlbumsDetailController) mCurrentLoadedRighpaneResult.getController()).createCards();
            children.add((Node) mCurrentLoadedRighpaneResult.getRoot());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    //Need some improvements
    private void initializePlayList() {
        final ListView listView = new ListView();
        listView.setOrientation(Orientation.HORIZONTAL);
        listView.setId("playList");
        HBox.setHgrow(listView, Priority.ALWAYS);
        bottombar.getChildren().add(listView);

        final ObservableList<Music> listData = FXCollections.observableArrayList();
        listData.addAll(population);


        listView.setItems(listData);
        listView.setCellFactory(new Callback<ListView<Music>, ListCell<Music>>() {
            @Override
            public ListCell<Music> call(ListView<Music> p) {
                return new PlayListListCell();
            }
        });

        listView.getSelectionModel().select(1);
    }

    //TODO Remove once we get a real list of Musics
    private void populateMusics() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Music m = new Music();
                m.setArtist("Artist " + String.valueOf(i));
                m.setAlbum("Album " + String.valueOf(j));
                m.setId(0l);
                m.setOwnerPeerId(0l);
                m.setHash(0);
                population.add(m);
            }
        }
    }
}

package fr.utc.lo23.sharutc.ui;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.cathive.fx.guice.GuiceFXMLLoader.Result;
import com.google.inject.Inject;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.SongCard;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable, PeopleHomeController.IPeopleHomeController, SearchResultController.ISearchResultController {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            final Result loadingResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/player.fxml"));
            mPlayerController = loadingResult.getController();
            bottombar.getChildren().add((Node) loadingResult.getRoot());
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

    @FXML
    private void handleMenuButtonAction(ActionEvent event) throws IOException {

        ObservableList<Node> children = rightpane.getChildren();
        children.clear();

        if (event.getSource() == songsbutton) {
            mCurrentLoadedRighpaneResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/song_list.fxml"));
        } else if (event.getSource() == peoplebutton) {
            mCurrentLoadedRighpaneResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/people_home.fxml"));
            ((PeopleHomeController) mCurrentLoadedRighpaneResult.getController()).setInterface(this);
            ((DragPreviewDrawer) mCurrentLoadedRighpaneResult.getController()).init(mDragPreview);
        } else if (event.getSource() == artistsbutton) {
            mCurrentLoadedRighpaneResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/artists_detail.fxml"));
        } else if (event.getSource() == albumsbutton) {
            mCurrentLoadedRighpaneResult = mFxmlLoader.load(getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/albums_detail.fxml"));
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
}

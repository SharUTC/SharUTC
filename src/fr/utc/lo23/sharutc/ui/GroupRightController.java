package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.profile.ManageRightsCommand;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.Rights;
import fr.utc.lo23.sharutc.model.domain.RightsList;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.ui.custom.HorizontalScrollHandler;
import fr.utc.lo23.sharutc.ui.custom.card.DraggableCard;
import fr.utc.lo23.sharutc.ui.custom.card.RightCard;
import fr.utc.lo23.sharutc.ui.custom.card.SimpleCard;
import fr.utc.lo23.sharutc.ui.custom.card.SongRightCard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author shima
 */
public class GroupRightController extends DragPreviewDrawer implements Initializable, SongRightCard.ISongCardRight, RightCard.IRightCard {


    @Inject
    private ManageRightsCommand rightCommand;
    @Inject
    public AppModel mAppModel;
    @FXML
    public FlowPane songsContainer;
    @FXML
    public FlowPane rightContainer;
    @FXML
    public ScrollPane rightScrollPane;
    @FXML
    public Label Group;
    @FXML
    public StackPane contentContainer;

    private Category currentCategory;
    private static final Logger log = LoggerFactory.getLogger(GroupRightController.class);

    private SimpleCard mAllSongCard;
    private RightCard mReadSongCard;
    private RightCard mListenSongCard;
    private RightCard mCommentAndNoteSongCard;
    private RightCard mAllRightsSongCard;
    /**
     * Display message to the user
     */
    private Label mPlaceHolderLabel;
    /**
     * Card selected by the user
     */
    private ArrayList<SongRightCard> mSongRightCardSelected;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rightScrollPane.getStyleClass().add("myScrollPaneWithTopBorder");
        HorizontalScrollHandler scrollHandler = new HorizontalScrollHandler(rightScrollPane);

        mSongRightCardSelected = new ArrayList<SongRightCard>();
        drawRightCard();
    }


    public void setGroupInfo(Category category) {

        Group.setText(category.getName());
        currentCategory = category;

        //get rights
        RightsList rightsList = mAppModel.getRightsList();

        log.info("rights size : " + rightsList.size());
        final List<Music> musics = mAppModel.getLocalCatalog().getMusics();
        if (musics.isEmpty()) {
            showPlaceHolder("There is no song in your catalogue. Go to the Songs tab first.");
        } else {
            for (Music m : musics) {
                //for each music display rights
                final Rights rights = rightsList.getByMusicIdAndCategoryId(m.getId(), currentCategory.getId());
                if (rights == null) {
                    //no rights yet, all false
                    songsContainer.getChildren().add(new SongRightCard(m, this, false, false, false, false));
                } else {
                    //display rights
                    //TODO wait for null pointer fixed
//                    songsContainer.getChildren().add(new SongRightCard(m, this, false, rights.getMayListen(),
//                            rights.getMayReadInfo(), rights.getMayNoteAndComment()));

                    //TODO remove when fixed
                    songsContainer.getChildren().add(new SongRightCard(m, this, false, false, false, false));
                }

            }
        }
    }


    public void handleCheckBoxListenClicked(Music m, boolean boxstate) {

        rightCommand.setCategory(currentCategory);
        rightCommand.setMusic(m);
        if (boxstate) {
            log.info("Goup:" + Group.getText() + " gives maylisten rights on music :" + m.getTitle());
            rightCommand.setMayListen(true);
        } else {
            log.info("Goup:" + Group.getText() + " removes maylisten rights on music :" + m.getTitle());
            rightCommand.setMayListen(false);
        }
        rightCommand.execute();
    }

    public void handleCheckBoxCommentClicked(Music m, boolean boxstate) {
        rightCommand.setCategory(currentCategory);
        rightCommand.setMusic(m);
        if (boxstate) {
            log.info("Goup:" + Group.getText() + " gives Comment rights on music :" + m.getTitle());
            rightCommand.setMayCommentAndScore(true);
        } else {
            log.info("Goup:" + Group.getText() + " removes Comment rights on music :" + m.getTitle());
            rightCommand.setMayCommentAndScore(false);
        }
        rightCommand.execute();
    }

    public void handleCheckBoxReadClicked(Music m, boolean boxstate) {
        rightCommand.setCategory(currentCategory);
        rightCommand.setMusic(m);
        if (boxstate) {
            log.info("Goup:" + Group.getText() + " gives ReadInfo rights on music :" + m.getTitle());
            rightCommand.setMayReadInfo(true);
        } else {
            log.info("Goup:" + Group.getText() + " removes ReadInfo rights on music :" + m.getTitle());
            rightCommand.setMayReadInfo(false);
        }
        rightCommand.execute();
    }

    /**
     * display right card
     */
    private void drawRightCard() {

        final Label allSongLabel = new Label("Songs");
        allSongLabel.getStyleClass().add("largeTextGreyBold");
        mAllSongCard = new SimpleCard("/fr/utc/lo23/sharutc/ui/fxml/simple_card.fxml",
                180, 101, Pos.CENTER);
        mAllSongCard.getChildren().add(allSongLabel);

        mReadSongCard = new RightCard("Read", this);

        mListenSongCard = new RightCard("Listen", this);

        mCommentAndNoteSongCard = new RightCard("Note", this);

        mAllRightsSongCard = new RightCard("All Right", this);

        rightContainer.getChildren().addAll(mAllSongCard, mReadSongCard, mListenSongCard, mCommentAndNoteSongCard,
                mAllRightsSongCard);
    }


    /**
     * Display the message in the place Holder
     */
    private void showPlaceHolder(String message) {
        mPlaceHolderLabel = new Label(message);
        mPlaceHolderLabel.getStyleClass().add("placeHolderLabel");
        mPlaceHolderLabel.setWrapText(true);
        mPlaceHolderLabel.setTextAlignment(TextAlignment.CENTER);
        contentContainer.getChildren().add(mPlaceHolderLabel);
    }

    /**
     * hide the place holder if it's displayed
     */
    private void hidePlaceHolder() {
        if (mPlaceHolderLabel != null) {
            contentContainer.getChildren().remove(mPlaceHolderLabel);
            mPlaceHolderLabel = null;
        }
    }

    @Override
    public void onSongAdded(RightCard card) {
        //TODO implement command
        if (card.equals(mAllRightsSongCard)) {
            log.info("song dropped in all right card : ");
        } else if (card.equals(mListenSongCard)) {
            log.info("song dropped in listen right card : ");
        } else if (card.equals(mReadSongCard)) {
            log.info("song dropped in read right card : ");
        } else if (card.equals(mCommentAndNoteSongCard)) {
            log.info("song dropped in comment right card : ");
        }

        for (SongRightCard songRightCard : mSongRightCardSelected) {
            log.info(songRightCard.getModel().getRealName());
        }
    }

    @Override
    public void onSongRightCardSelected(SongRightCard songCardRight) {
        if (mSongRightCardSelected.contains(songCardRight)) {
            mSongRightCardSelected.remove(songCardRight);
        } else {
            mSongRightCardSelected.add(songCardRight);
        }
    }

    @Override
    public void onDragStart(MouseEvent event, DraggableCard draggableCard) {
        if (draggableCard instanceof SongRightCard) {
            final SongRightCard draggedCard = (SongRightCard) draggableCard;
            //had to checked if this card is already selected because user
            //can drag a selected one or a new one
            mSongRightCardSelected.remove(draggedCard);
            mSongRightCardSelected.add(draggedCard);

            //drag event start, inform all selected card
            updateDragPreview(event, mSongRightCardSelected);
        }
    }

    @Override
    public void onDragStop(DraggableCard draggableCard) {
        if (draggableCard instanceof SongRightCard) {
            //drag event failed, inform all selected card
            hideDragPreview(mSongRightCardSelected);
            //clean the selection
            mSongRightCardSelected.clear();

        }
    }

    @Override
    public void onDetach() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

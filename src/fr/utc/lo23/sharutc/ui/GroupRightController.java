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
import javafx.event.EventHandler;
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

    /**
     * local identifier
     */
    private static final int RIGHT_LISTEN = 0x00000001;
    private static final int RIGHT_READ = 0x00000002;
    private static final int RIGHT_NOTE = 0x00000003;
    private static final int RIGHT_ALL = 0x00000004;


    @Inject
    private ManageRightsCommand manageRightsCommand;
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

    private Category mCurrentCategory;
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
        mCurrentCategory = category;

        displayAllMusic();
    }

    private void displayMusicByRight(final int rightIdentifier) {
        clearView();

        final List<Music> matchingMusics = getMusicByRights(rightIdentifier);
        final RightsList rightsList = mAppModel.getRightsList();

        if (matchingMusics.isEmpty()) {
            //display place holder cause no music have the requested right
            switch (rightIdentifier) {
                case RIGHT_ALL:
                    showPlaceHolder("Currently, there is no song with all rights in the category : "
                            + mCurrentCategory.getName());
                    break;
                case RIGHT_LISTEN:
                    showPlaceHolder("Currently, your musics can't be heard by the users of "
                            + mCurrentCategory.getName());
                    break;
                case RIGHT_READ:
                    showPlaceHolder("Currently, your musics' info can't be read by the users of "
                            + mCurrentCategory.getName());
                    break;
                case RIGHT_NOTE:
                    showPlaceHolder("Currently, your musics  can't be rated by the users of "
                            + mCurrentCategory.getName());
                    break;
            }
        } else {
            for (Music m : matchingMusics) {
                final Rights rights = rightsList.getByMusicIdAndCategoryId(m.getId(), mCurrentCategory.getId());
                if (rights != null) {
                    songsContainer.getChildren().add(new SongRightCard(m, this, false, rights.getMayListen(),
                            rights.getMayReadInfo(), rights.getMayNoteAndComment()));
                } else {
                    //if doesn't exist, no rights at all
                    songsContainer.getChildren().add(new SongRightCard(m, this, false, false, false, false));
                }
            }
        }


    }

    /**
     * get all music which match with the correct rightIdentifier
     * used to sort music by right
     * Should it be a method of the model ?
     *
     * @param rightIdentifier
     * @return
     */
    private List<Music> getMusicByRights(final int rightIdentifier) {
        //get rights
        final List<Music> musics = mAppModel.getLocalCatalog().getMusics();
        final RightsList rightsList = mAppModel.getRightsList();

        final List<Music> result = new ArrayList<Music>();

        if (musics.isEmpty()) {
            showPlaceHolder("There is no song in your catalogue. Go to the Songs tab first.");
        } else {
            for (Music m : musics) {
                //for each music check rights

                final Rights rights = rightsList.getByMusicIdAndCategoryId(m.getId(), mCurrentCategory.getId());

                if (rights != null) {
                    boolean shouldAdd = false;
                    //are rights matching to requested one ?
                    switch (rightIdentifier) {
                        case RIGHT_LISTEN:
                            if (rights.getMayListen()) shouldAdd = true;
                            break;
                        case RIGHT_READ:
                            if (rights.getMayReadInfo()) shouldAdd = true;
                            break;
                        case RIGHT_NOTE:
                            if (rights.getMayNoteAndComment()) shouldAdd = true;
                            break;
                        case RIGHT_ALL:
                            if (rights.getMayNoteAndComment()
                                    & rights.getMayReadInfo()
                                    & rights.getMayListen()) {
                                shouldAdd = true;
                            }
                            break;
                    }

                    if (shouldAdd) {
                        //add music to the resultList
                        result.add(m);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Display all local music with rights according to the category selected
     */
    private void displayAllMusic() {
        clearView();

        final List<Music> musics = mAppModel.getLocalCatalog().getMusics();
        final RightsList rightsList = mAppModel.getRightsList();
        if (musics.isEmpty()) {
            showPlaceHolder("There is no song in your catalogue. Go to the Songs tab first.");
        } else {
            for (Music m : musics) {
                //for each music display rights
                final Rights rights = rightsList.getByMusicIdAndCategoryId(m.getId(), mCurrentCategory.getId());
                //display the card
                if (rights != null) {
                    songsContainer.getChildren().add(new SongRightCard(m, this, false, rights.getMayListen(),
                            rights.getMayReadInfo(), rights.getMayNoteAndComment()));
                } else {
                    //if doesn't exist, no rights at all
                    songsContainer.getChildren().add(new SongRightCard(m, this, false, false, false, false));
                }
            }
        }
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
        mAllSongCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                displayAllMusic();
            }
        });

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

    /**
     * used to clear songs container
     */
    private void clearView() {
        songsContainer.getChildren().clear();
        hidePlaceHolder();
    }

    @Override
    public void onSongAdded(RightCard card) {
        final RightsList rightsList = mAppModel.getRightsList();

        //for all selected Music dropped
        for (SongRightCard songRightCard : mSongRightCardSelected) {
            //TODO move right into card model
            //recover the music
            final Music m = songRightCard.getModel();

            //get the current rights
            Rights rights = rightsList.getByMusicIdAndCategoryId(m.getId(), mCurrentCategory.getId());
            if (rights == null) {
                //rights doesn't exist yet
                rights = new Rights(mCurrentCategory.getId(), m.getId(), false, false, false);
            }
            //initialize the command
            manageRightsCommand.setCategory(mCurrentCategory);
            manageRightsCommand.setMusic(m);
            manageRightsCommand.setMayCommentAndScore(rights.getMayNoteAndComment());
            manageRightsCommand.setMayListen(rights.getMayListen());
            manageRightsCommand.setMayReadInfo(rights.getMayReadInfo());

            //change according to the drop area
            if (card.equals(mAllRightsSongCard)) {
                log.info("song dropped in all right card : ");
                manageRightsCommand.setMayCommentAndScore(true);
                manageRightsCommand.setMayListen(true);
                manageRightsCommand.setMayReadInfo(true);
            } else if (card.equals(mListenSongCard)) {
                log.info("song dropped in listen right card : ");
                manageRightsCommand.setMayListen(true);
            } else if (card.equals(mReadSongCard)) {
                log.info("song dropped in read right card : ");
                manageRightsCommand.setMayReadInfo(true);
            } else if (card.equals(mCommentAndNoteSongCard)) {
                log.info("song dropped in comment right card : ");
                manageRightsCommand.setMayCommentAndScore(true);
            }

            //execute the command
            manageRightsCommand.execute();
        }
    }

    @Override
    public void onRightCardClicked(RightCard card) {
        //clear the songRightCard container
        log.info("card clicked : " + card.getId());
        if (card.equals(mListenSongCard)) {
            displayMusicByRight(RIGHT_LISTEN);
        } else if (card.equals(mReadSongCard)) {
            displayMusicByRight(RIGHT_READ);
        } else if (card.equals(mCommentAndNoteSongCard)) {
            displayMusicByRight(RIGHT_NOTE);
        } else if (card.equals(mAllRightsSongCard)) {
            displayMusicByRight(RIGHT_ALL);
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

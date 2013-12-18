package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.music.AddMusicToCategoryCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveMusicFromCategoryCommand;
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
import fr.utc.lo23.sharutc.ui.util.DialogBoxBuilder;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionEvent;
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

public class GroupRightController extends DragPreviewDrawer implements Initializable, SongRightCard.ISongCardRight, RightCard.IRightCard, CollectionChangeListener {

    /**
     * local identifier
     */
    private static final int RIGHT_LISTEN = 0x00000001;
    private static final int RIGHT_READ = 0x00000002;
    private static final int RIGHT_NOTE = 0x00000003;
    private static final int RIGHT_ALL = 0x00000004;
    private static final int RIGHT_NONE = 0x00000005;

    @Inject
    private ManageRightsCommand manageRightsCommand;
    @Inject
    private AddMusicToCategoryCommand addMusicToCategoryCommand;
    @Inject
    private RemoveMusicFromCategoryCommand removeMusicFromCategoryCommand;
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
    private RightCard mNoneRightsSongCard;
    private SimpleCard mCurrentRightsSongCard;
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

        mAppModel.getRightsList().addPropertyChangeListener(this);
    }


    public void setGroupInfo(Category category) {

        Group.setText(category.getName());
        mCurrentCategory = category;

        displayAllMusic();
    }


    /**
     * display matching song according to the current selected right.
     *
     * @param rightIdentifier
     */
    private void displayMusicByRight(final int rightIdentifier) {
        clearSongContainer();

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
                case RIGHT_NONE:
                    showPlaceHolder("Currently, your musics as rights in the category : "
                            + mCurrentCategory.getName());
                    break;
            }
        } else {
            for (Music m : matchingMusics) {
                final Rights rights = rightsList.getByMusicIdAndCategoryId(m.getId(), mCurrentCategory.getId());
                displaySongRightCard(m, rights);
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
            //showPlaceHolder("There is no song in your catalogue. Go to the Songs tab first.");
        } else {
            for (Music m : musics) {
                //for each music check rights

                final Rights rights = rightsList.getByMusicIdAndCategoryId(m.getId(), mCurrentCategory.getId());
                boolean shouldAdd = false;
                if (rights != null) {
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
                        case RIGHT_NONE:
                            if (!rights.getMayNoteAndComment()
                                    & !rights.getMayReadInfo()
                                    & !rights.getMayListen()) shouldAdd = true;
                            break;
                    }

                } else {
                    //no rights for this music but should add for none section
                    if (rightIdentifier == RIGHT_NONE) shouldAdd = true;
                }
                if (shouldAdd) {
                    //add music to the resultList
                    result.add(m);
                }
            }
        }

        return result;
    }

    /**
     * Display all local music with rights according to the category selected
     */
    private void displayAllMusic() {
        clearSongContainer();

        final List<Music> musics = mAppModel.getLocalCatalog().getMusics();
        final RightsList rightsList = mAppModel.getRightsList();
        if (musics.isEmpty()) {
            showPlaceHolder("There is no song in your catalogue. Go to the Songs tab first.");
        } else {
            for (Music m : musics) {
                //for each music display rights
                final Rights rights = rightsList.getByMusicIdAndCategoryId(m.getId(), mCurrentCategory.getId());
                //display the card
                displaySongRightCard(m, rights);
            }
        }
    }

    /**
     * Add a card to the songContainer with given data
     *
     * @param m      music
     * @param rights rights
     */
    private void displaySongRightCard(Music m, Rights rights) {
        if (rights != null) {
            songsContainer.getChildren().add(new SongRightCard(m, this, rights));
        } else {
            //if doesn't exist, no rights at all
            songsContainer.getChildren().add(new SongRightCard(m, this,
                    new Rights(mCurrentCategory.getId(), m.getId(), false, false, false)));
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
                mCurrentRightsSongCard = mAllSongCard;
                cleanRightContainer();
                displayAllMusic();
            }
        });

        mReadSongCard = new RightCard("Read", RIGHT_READ, this);

        mListenSongCard = new RightCard("Listen", RIGHT_LISTEN, this);

        mCommentAndNoteSongCard = new RightCard("Note", RIGHT_NOTE, this);

        mAllRightsSongCard = new RightCard("All Right", RIGHT_ALL, this);

        mNoneRightsSongCard = new RightCard("None", RIGHT_NONE, this);

        mCurrentRightsSongCard = mAllSongCard;

        rightContainer.getChildren().addAll(mAllSongCard, mReadSongCard, mListenSongCard, mCommentAndNoteSongCard,
                mAllRightsSongCard, mNoneRightsSongCard);
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
    private void clearSongContainer() {
        songsContainer.getChildren().clear();
        hidePlaceHolder();
    }

    /**
     * used to clean style of right card
     */
    private void cleanRightContainer() {
        mAllRightsSongCard.setSelected(false);
        mListenSongCard.setSelected(false);
        mReadSongCard.setSelected(false);
        mCommentAndNoteSongCard.setSelected(false);
        mNoneRightsSongCard.setSelected(false);
    }

    /**
     * Display highlight on each right card matching with the given rights
     *
     * @param rights which be used to find matching rightCard
     */
    private void selectedMatchingRight(Rights rights) {
        final Boolean mayListen = rights.getMayListen();
        final Boolean mayReadInfo = rights.getMayReadInfo();
        final Boolean mayCommentAndNote = rights.getMayNoteAndComment();

        if (mayListen & mayReadInfo & mayCommentAndNote) {
            mAllRightsSongCard.setSelected(true);
        }

        if (mayListen | mayReadInfo | mayCommentAndNote) {
            if (mayListen) mListenSongCard.setSelected(true);
            if (mayReadInfo) mReadSongCard.setSelected(true);
            if (mayCommentAndNote) mCommentAndNoteSongCard.setSelected(true);
        } else {
            mNoneRightsSongCard.setSelected(true);
        }
    }

    /**
     * init command with current song's right values
     *
     * @param songRightCard
     */
    private void initCommand(SongRightCard songRightCard) {
        //recover the music
        final Music m = songRightCard.getMusic();

        //get the current rights
        Rights rights = songRightCard.getRights();

        //initialize the command
        manageRightsCommand.setCategory(mCurrentCategory);
        manageRightsCommand.setMusic(m);
        manageRightsCommand.setMayCommentAndScore(rights.getMayNoteAndComment());
        manageRightsCommand.setMayListen(rights.getMayListen());
        manageRightsCommand.setMayReadInfo(rights.getMayReadInfo());
    }

    @Override
    public void onSongAdded(RightCard card) {
        //for all selected Music dropped
        for (SongRightCard songRightCard : mSongRightCardSelected) {

            addMusicToCategoryCommand.setCategory(mCurrentCategory);
            addMusicToCategoryCommand.setMusic(songRightCard.getMusic());
            addMusicToCategoryCommand.execute();

            initCommand(songRightCard);

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
                //also add the right to read song info
                // doesn't make sens to be able comment a song without seeing comments
                manageRightsCommand.setMayReadInfo(true);
                manageRightsCommand.setMayCommentAndScore(true);
            } else if (card.equals(mNoneRightsSongCard)) {
                log.info("song dropped in none right card : ");
                manageRightsCommand.setMayCommentAndScore(false);
                manageRightsCommand.setMayListen(false);
                manageRightsCommand.setMayReadInfo(false);

                removeMusicFromCategoryCommand.setMusic(songRightCard.getMusic());
                removeMusicFromCategoryCommand.setCategory(mCurrentCategory);
                removeMusicFromCategoryCommand.execute();
            }

            //execute the command
            manageRightsCommand.execute();
        }
    }

    @Override
    public void onRightCardClicked(RightCard card, MouseEvent event) {
        //select the right one and unselected the other
        if (mCurrentRightsSongCard != mAllSongCard) {
            mCurrentRightsSongCard.setSelected(false);
        }
        mCurrentRightsSongCard = card;
        mCurrentRightsSongCard.setSelected(true);

        displayMusicByRight(card.getIdentifier());
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
    public void onSongRightCardHovered(SongRightCard songRightCard, boolean isHover) {
        if (isHover) {
            selectedMatchingRight(songRightCard.getRights());
            songRightCard.setDeletable(mCurrentRightsSongCard != mAllSongCard);

        } else {
            //hide all
            cleanRightContainer();
            songRightCard.setDeletable(false);
        }
    }

    @Override
    public void onSongRightCardBasketHovered(SongRightCard songRightCard, boolean isHover) {
        if (isHover) {
            cleanRightContainer();
        } else {
            selectedMatchingRight(songRightCard.getRights());
        }
    }

    @Override
    public void onSongRightCardRemove(SongRightCard songRightCard) {
        initCommand(songRightCard);

        String messageToShow = "Do you want to ";

        if (mCurrentRightsSongCard == mListenSongCard) {
            //remove listen right
            manageRightsCommand.setMayListen(false);
            messageToShow += "take off \"listen right\" for ";
        } else if (mCurrentRightsSongCard == mReadSongCard) {
            //remove read info right
            manageRightsCommand.setMayReadInfo(false);
            messageToShow += "take off \"read info right\" for ";
        } else if (mCurrentRightsSongCard == mCommentAndNoteSongCard) {
            //remove comment and note right
            manageRightsCommand.setMayCommentAndScore(false);
            messageToShow += "take off \"comment and not right\" for ";
        } else if (mCurrentRightsSongCard == mAllRightsSongCard) {
            //remove all right
            manageRightsCommand.setMayCommentAndScore(false);
            manageRightsCommand.setMayListen(false);
            manageRightsCommand.setMayReadInfo(false);
            messageToShow += "take off \"all rights\" for ";
        } else if (mCurrentRightsSongCard == mNoneRightsSongCard) {
            //reset all right
            manageRightsCommand.setMayCommentAndScore(true);
            manageRightsCommand.setMayListen(true);
            manageRightsCommand.setMayReadInfo(true);
            messageToShow += "grant \"all rights\" for ";
        }

        messageToShow += songRightCard.getMusic().getTitle() + "?";

        DialogBoxBuilder.createConfirmBox(messageToShow,
                this.getClass().getResource("/fr/utc/lo23/sharutc/ui/css/modal.css").toExternalForm(),
                songsContainer.getScene().getRoot(),
                new DialogBoxBuilder.IConfirmBox() {
                    @Override
                    public void onChoiceMade(boolean answer) {
                        cleanRightContainer();
                        mCurrentRightsSongCard.setSelected(true);
                        if (answer) {
                            manageRightsCommand.execute();
                        }
                    }
                }).show();

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
        mAppModel.getRightsList().removePropertyChangeListener(this);
    }

    @Override
    public void collectionChanged(CollectionEvent ev) {
        final CollectionEvent.Type type = ev.getType();
        final Object item = ev.getItem();
        if (CollectionEvent.Type.UPDATE.equals(type)) {
            if (item instanceof Rights) {
                //update UI
                if (mCurrentRightsSongCard == mAllSongCard) {
                    displayAllMusic();
                } else {
                    displayMusicByRight(((RightCard) mCurrentRightsSongCard).getIdentifier());
                }

                log.info("Rights updated : " + ((Rights) item).getMusicId());
            }
        }
    }
}

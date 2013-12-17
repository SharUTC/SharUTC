package fr.utc.lo23.sharutc.ui.custom.card;

import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * A {@link DraggableCard} that shows the information of a piece of
 * {@link Music}.
 * <p/>
 * This card has three buttons to add the song to the play list, to view the
 * details of the song and to view the tags associated with the song.
 * <p/>
 * This class notifies a {@link ISongCard} on click, double click, and when one
 * of the three buttons is clicked.
 */
public class SongCard extends DraggableCard implements EventHandler<Event> {

    public static final String DROP_KEY = SongCard.class + "DropKey";
    @FXML
    public Label ownerLogin;
    @FXML
    public Label songTitle;
    @FXML
    public Label songArtist;
    @FXML
    Button detailButton;
    @FXML
    Button addToPlayListButton;
    @FXML
    Button tagEditionButton;
    @FXML
    VBox buttonContainer;
    private Music mModel;
    private ISongCard mInterface;
    private boolean mIsOwned;

    public SongCard(Music m, ISongCard i, final AppModel appModel) {
        super("/fr/utc/lo23/sharutc/ui/fxml/song_card.fxml", DROP_KEY, i);
        mInterface = i;
        mModel = m;
        songTitle.setText(mModel.getTitle());
        songArtist.setText(mModel.getArtist());

        setOnMouseEntered(this);
        setOnMouseExited(this);
        if (appModel.getProfile().getUserInfo().getPeerId().equals(mModel.getOwnerPeerId())) {
            mIsOwned = true;
        } else {
            mIsOwned = false;
            ownerLogin.setText(appModel.getActivePeerList().getPeerByPeerId(m.getOwnerPeerId()).getDisplayName());
        }


        //TODO improve, contruct music with at least initialized Boolean
        if (mIsOwned || m.getMayListen()) {
            setOnMouseClicked(this);
        }

    }

    /**
     * Return the {@link Music} from the data was extracted.
     *
     * @return {@link Music}
     */
    public Music getModel() {
        return mModel;
    }

    /**
     * Set the visibility of the buttons.
     *
     * @param isVisible the visibility to be set.
     */
    public void setButtonVisibility(boolean isVisible) {
        //TODO Boolean should be initialized
        //TODO improve model
        if (mIsOwned || mModel.getMayListen()) {
            addToPlayListButton.setVisible(isVisible);
        }

        if (mIsOwned || mModel.getMayReadInfo()) {
            detailButton.setVisible(isVisible);
        }

        if (mIsOwned) {
            tagEditionButton.setVisible(isVisible);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Event event) {
        final Object source = event.getSource();
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (source.equals(this)) {
                mInterface.onSongCardSelected(SongCard.this);
                this.adaptStyle((MouseEvent) event);
                if (((MouseEvent) event).getClickCount() == 2) {
                    mInterface.onPlayRequested(mModel);
                }
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            if (source.equals(this)) {
                this.setButtonVisibility(true);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            if (source.equals(this)) {
                this.setButtonVisibility(false);
            }
        }
    }

    @FXML
    private void handleSongCardButtonAction(ActionEvent event) throws IOException {
        final Object source = event.getSource();
        if (source.equals(addToPlayListButton)) {
            mInterface.onSongAddToPlayList(mModel);
        } else if (source.equals(detailButton)) {
            mInterface.onSongDetailsRequested(mModel);
        } else if (source.equals(tagEditionButton)) {
            mInterface.onTagEditionRequested(mModel);
        }

    }

    /**
     * A {@link IDraggableCardListener} that is notified when a user interacts
     * with a {@link SongCard}
     */
    public interface ISongCard extends IDraggableCardListener {

        /**
         * The {@link ISongCard} is being asked to play a piece of
         * {@link Music}.
         *
         * @param music the piece of {@link Music} to be played.
         */
        public void onPlayRequested(Music music);

        /**
         * The {@link ISongCard} is being asked to display the detail of a piece
         * of {@link Music}
         *
         * @param music the piece of {@link Music} to be displayed.
         */
        public void onSongDetailsRequested(Music music);

        /**
         * The {@link ISongCard} is being notified that a {@link @SongCard} has
         * just been selected.
         *
         * @param songCard the {@link SongCard} that has been selected.
         */
        public void onSongCardSelected(SongCard songCard);

        /**
         * The {@link ISongCard} is being asked to add a piece of {@link Music}
         * to the play list.
         *
         * @param music the piece of {@link Music} to be added.
         */
        public void onSongAddToPlayList(Music music);

        /**
         * The {@link ISongCard} is being asked to edit the tags of a piece of
         * {@link Music}.
         *
         * @param music the piece of {@link Music} to be edited.
         */
        public void onTagEditionRequested(Music music);
    }
}

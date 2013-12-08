package fr.utc.lo23.sharutc.ui.custom.card;

import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * A {@link DraggableCard} that show the rights of a piece of {@link Music}.
 * This class notifies a {@link ISongRightCard} on click.
 */
public class SongRightCard extends DraggableCard implements EventHandler<Event> {

    /**
     * key used for the drag event to identify the content
     */
    public static final String DROP_KEY = SongRightCard.class + "DropKey";
    @FXML
    public Label songTitle;
    @FXML
    public Label songArtist;
    @FXML
    public VBox buttonContainer;
    @FXML
    public CheckBox checkBoxRead;
    @FXML
    public CheckBox checkBoxEdit;
    @FXML
    public CheckBox checkBoxComment;
    private Music mModel;
    private boolean mIsOwned;
    private ISongCardRight mInterface;

    public SongRightCard(Music m, ISongCardRight i, boolean isOwned, boolean mayListen, boolean mayReadInfo, boolean mayComment) {
        super("/fr/utc/lo23/sharutc/ui/fxml/song_card_right.fxml", DROP_KEY, i);
        mModel = m;
        mInterface = i;
        songTitle.setText(mModel.getTitle());
        songArtist.setText(mModel.getArtist());
        mIsOwned = isOwned;

        buttonContainer.setDisable(true);

        checkBoxEdit.setSelected(mayListen);
        checkBoxRead.setSelected(mayReadInfo);
        checkBoxComment.setSelected(mayComment);

        setOnMouseClicked(this);
        setOnMouseEntered(this);
        setOnMouseExited(this);
    }

    public Music getModel() {
        return mModel;
    }

    /**
     * display right details on hover
     *
     * @param isHover
     */
    public void onHover(boolean isHover) {
        buttonContainer.setVisible(isHover);
    }

    @Override
    public void handle(Event event) {
        final Object source = event.getSource();
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (source.equals(this)) {
                this.adaptStyle((MouseEvent) event);
                mInterface.onSongRightCardSelected(this);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            if (source.equals(this)) {
                this.onHover(true);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            if (source.equals(this)) {
                this.onHover(false);
            }
        }
    }

    /**
     * A {@link IDraggableCardListener} that is notified when a user selects the
     * {@link SongRightCard}.
     */
    public interface ISongCardRight extends IDraggableCardListener {

        /**
         * The {@link ISongCardRight} is being notified that a
         * {@link SongCardRight} has just benn selected.
         *
         * @param songCardRight the {@link SongRightCard} that has been
         * selected.
         */
        public void onSongRightCardSelected(SongRightCard songCardRight);
    }
}

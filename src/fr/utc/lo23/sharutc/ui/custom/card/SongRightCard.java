package fr.utc.lo23.sharutc.ui.custom.card;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.Rights;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

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
    public Button deleteButton;

    private Music mMusic;
    private Rights mRights;
    private ISongCardRight mInterface;

    public SongRightCard(Music m, ISongCardRight i, Rights rights) {
        super("/fr/utc/lo23/sharutc/ui/fxml/song_card_right.fxml", DROP_KEY, i);
        mMusic = m;
        mInterface = i;
        songTitle.setText(mMusic.getTitle());
        songArtist.setText(mMusic.getArtist());

        this.updateRights(rights);

        setOnMouseClicked(this);
        setOnMouseEntered(this);
        setOnMouseExited(this);
        deleteButton.setOnMouseClicked(this);
        deleteButton.setOnMouseEntered(this);
        deleteButton.setOnMouseExited(this);
    }

    /**
     * set rights to update the ui
     *
     * @param rights
     */
    public void updateRights(Rights rights) {
        mRights = rights;
    }

    /**
     * retrieve the music
     *
     * @return
     */
    public Music getMusic() {
        return mMusic;
    }

    /**
     * retrieve the rights
     *
     * @return
     */
    public Rights getRights() {
        return mRights;
    }

    /**
     * display right details on hover
     *
     * @param isHover
     */
    private void onHover(boolean isHover) {
        deleteButton.setVisible(isHover);
        mInterface.onSongRightCardHovered(this, isHover);
    }

    /**
     * display right details on hover
     *
     * @param isHover
     */
    public void setDeletable(boolean isDelatable) {
        deleteButton.setVisible(isDelatable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Event event) {
        final Object source = event.getSource();
        final EventType eventType = event.getEventType();
        if (MouseEvent.MOUSE_CLICKED.equals(eventType)) {
            if (source.equals(deleteButton)) {
                mInterface.onSongRightCardRemove(this);
                event.consume();
            } else if (source.equals(this)) {
                this.adaptStyle((MouseEvent) event);
                mInterface.onSongRightCardSelected(this);
            }
        } else if (MouseEvent.MOUSE_ENTERED.equals(eventType)) {
            if (source.equals(this)) {
                this.onHover(true);
            }else if(source.equals(deleteButton)){
                 mInterface.onSongRightCardBasketHovered(this,true);
            }
        } else if (MouseEvent.MOUSE_EXITED.equals(eventType)) {
            if (source.equals(this)) {
                this.onHover(false);
            } else if(source.equals(deleteButton)){
                mInterface.onSongRightCardBasketHovered(this,false);
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
         * {@link SongCardRight} has just been selected.
         *
         * @param songCardRight the {@link SongRightCard} that has been
         *                      selected.
         */
        public void onSongRightCardSelected(SongRightCard songCardRight);

        /**
         * The {@link ISongCardRight} is being notified that a
         * {@link SongCardRight} has just been hovered.
         *
         * @param songCardRight the {@link SongRightCard} that has been
         *                      hovered.
         */
        public void onSongRightCardHovered(SongRightCard songRightCard, boolean isHover);

        /**
         * The {@link ISongCardRight} is being notified that the
         * basket of {@link SongCardRight} has just been hovered.
         *
         * @param songCardRight the {@link SongRightCard} that has been
         *                      hovered.
         */
        public void onSongRightCardBasketHovered(SongRightCard songRightCard, boolean isHover);

        /**
         * The {@link ISongCardRight} is being notified that a
         * {@link SongCardRight} has just been remove.
         *
         * @param songCardRight the {@link SongRightCard} that has been
         *                      hovered.
         */
        public void onSongRightCardRemove(SongRightCard songRightCard);
    }
}

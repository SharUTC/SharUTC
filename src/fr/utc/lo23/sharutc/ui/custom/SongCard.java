package fr.utc.lo23.sharutc.ui.custom;

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

public class SongCard extends DraggableCard implements EventHandler<Event> {

    public static final String DROP_KEY = SongCard.class + "DropKey";
    private Music mModel;
    private ISongCard mInterface;
    private boolean mIsOwned;

    @FXML
    public Label songTitle;

    @FXML
    Button deleteOrAddButton;

    @FXML
    Button playButton;

    @FXML
    Button detailButton;

    @FXML
    Button deleteButton;

    @FXML
    Button addButton;

    @FXML
    VBox buttonContainer;

    public SongCard(Music m, ISongCard i, boolean isOwned) {
        super("/fr/utc/lo23/sharutc/ui/fxml/song_card.fxml", DROP_KEY, i);
        mInterface = i;
        mModel = m;
        songTitle.setText(mModel.getFileName());
        setOnMouseClicked(this);
        setOnMouseEntered(this);
        setOnMouseExited(this);
        mIsOwned = isOwned;
        if (mIsOwned) {
            buttonContainer.getChildren().remove(addButton);
        } else {
            buttonContainer.getChildren().remove(deleteButton);
        }
    }

    public Music getModel() {
        return mModel;
    }

    public void onHover(boolean isHover) {
        detailButton.setVisible(isHover);
        playButton.setVisible(isHover);
        if (mIsOwned) {
            deleteButton.setVisible(isHover);
        } else {
            addButton.setVisible(isHover);
        }

    }

    @Override
    public void handle(Event event) {
        final Object source = event.getSource();
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (source.equals(this)) {
                mInterface.onSongCardSelected(SongCard.this);
                this.adaptStyle((MouseEvent) event);
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

    @FXML
    private void handleSongCardButtonAction(ActionEvent event) throws IOException {
        final Object source = event.getSource();
        if (source.equals(deleteOrAddButton)) {
            if (mIsOwned) {
                //music already owned
                mInterface.onSongRemoveFromMusicRequested(getModel());
            } else {
                //music not owned, want to add it
                mInterface.onSongAddToMusicRequested(getModel());
            }
        } else if (source.equals(playButton)) {
            mInterface.onPlayRequested(getModel());
        } else if (source.equals(detailButton)) {
            mInterface.onSongDetailsRequested(getModel());
        }

    }

    public interface ISongCard extends IDraggableCardListener {
        /**
         * user requested play song
         *
         * @param music clicked card's model
         */
        public void onPlayRequested(Music music);

        /**
         * user requested to remove this song from his musics
         *
         * @param music clicked card's model
         */
        public void onSongRemoveFromMusicRequested(Music music);

        /**
         * user requested more details
         *
         * @param music clicked card's model
         */
        public void onSongDetailsRequested(Music music);

        /**
         * user requested to add this song to his music
         *
         * @param music clicked card's model
         */
        public void onSongAddToMusicRequested(Music music);

        /**
         * card has been selected
         *
         * @param songCard
         */
        public void onSongCardSelected(SongCard songCard);
    }
}

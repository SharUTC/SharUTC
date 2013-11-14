package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class SongCard extends DraggableCard implements EventHandler<Event> {

    public static final String DROP_KEY = SongCard.class + "DropKey";
    private Music mModel;
    private ISongCard mInterface;

    @FXML
    public Label songTitle;

    public SongCard(Music m, ISongCard i) {
        super("/fr/utc/lo23/sharutc/ui/fxml/song_card.fxml", DROP_KEY, i);
        mInterface = i;
        mModel = m;
        songTitle.setText(mModel.getFileName());
        setOnMouseClicked(this);
    }

    @Override
    public void handle(Event event) {
        final Object source = event.getSource();
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (source.equals(this)) {
                mInterface.onSongCardSelected(SongCard.this);
                this.adaptStyle((MouseEvent) event);
            }
        }
    }

    public Music getModel() {
        return mModel;
    }

    public interface ISongCard extends IDraggableCardListener {
        /**
         * user requested play song
         *
         * @param music clicked card's model
         */
        public void onPlayRequested(Music music);

        /**
         * user requested to add this song to the playList
         *
         * @param music clicked card's model
         */
        public void onAddToPlayListRequested(Music music);

        /**
         * user requested more details
         *
         * @param music clicked card's model
         */
        public void onSongDetailsRequested(Music music);

        /**
         * card has been selected
         *
         * @param songCard
         */
        public void onSongCardSelected(SongCard songCard);
    }
}

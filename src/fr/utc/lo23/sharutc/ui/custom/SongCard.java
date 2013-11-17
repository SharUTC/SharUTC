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
    Button detailButton;

    @FXML
    Button addToPlayListButton;
    
    @FXML
    Button tagEditionButton;


    @FXML
    VBox buttonContainer;

    public SongCard(Music m, ISongCard i, boolean isOwned) {
        super("/fr/utc/lo23/sharutc/ui/fxml/song_card.fxml", DROP_KEY, i);
        mInterface = i;
        mModel = m;
        songTitle.setText(mModel.getTitle());
        setOnMouseClicked(this);
        setOnMouseEntered(this);
        setOnMouseExited(this);
        mIsOwned = isOwned;
        
    }

    public Music getModel() {
        return mModel;
    }

    public void onHover(boolean isHover) {
        detailButton.setVisible(isHover);
        addToPlayListButton.setVisible(isHover);
        tagEditionButton.setVisible(isHover);
    }

    @Override
    public void handle(Event event) {
        final Object source = event.getSource();
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (source.equals(this)) {
                mInterface.onSongCardSelected(SongCard.this);
                this.adaptStyle((MouseEvent) event);
                if(((MouseEvent)event).getClickCount() == 2) {
                    mInterface.onPlayRequested(mModel);
                }
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
        if (source.equals(addToPlayListButton)) {
            mInterface.onSongAddToPlayList(mModel);
        } else if (source.equals(detailButton)) {
            mInterface.onSongDetailsRequested(mModel);
        } else if (source.equals(tagEditionButton)) {
            mInterface.onTagEditionRequested(mModel);
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
        
        /**
         * user requested to add this song to his play list
         * 
         * @param music 
         */
        public void onSongAddToPlayList(Music music);
        
        /**
         * user wants edit the tag of the music
         * 
         * @param music 
         */
        public void onTagEditionRequested(Music music);
    }
}

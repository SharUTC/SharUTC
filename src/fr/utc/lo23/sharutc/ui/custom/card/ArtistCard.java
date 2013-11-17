package fr.utc.lo23.sharutc.ui.custom.card;

import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ArtistCard extends SimpleCard implements EventHandler<Event>{
    
    private Music mMusic;
    private IArtistCard mInterface;
    
    public Label artistName;
    
    //TODO Remove once we have a real library of songs
    public ArtistCard() {
        super("/fr/utc/lo23/sharutc/ui/fxml/artist_card.fxml");
    }
    
    public ArtistCard(Music music, IArtistCard i) {
        super("/fr/utc/lo23/sharutc/ui/fxml/artist_card.fxml");
        setOnMouseClicked(this);
        mInterface = i;
        initModel(music);
    }
    
    private void initModel(Music music) {
        mMusic = music;
        artistName.setText(mMusic.getArtist());
    }

    public Music getModel() {
        return mMusic;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    mInterface.onArtistDetailRequested(mMusic);
                }
            }
        }
    }
    
    public interface IArtistCard {

        /**
         * user requested more details
         *
         * @param music
         */
        public void onArtistDetailRequested(Music music);

    }
}

package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ArtistCard extends SimpleCard implements EventHandler<Event>{
    
    private Music mMusic;
    
    public Label artistName;
    
    //TODO Remove once we have a real library of songs
    public ArtistCard() {
        super("/fr/utc/lo23/sharutc/ui/fxml/artist_card.fxml");
    }
    
    public ArtistCard(Music music) {
        super("/fr/utc/lo23/sharutc/ui/fxml/artist_card.fxml");
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
        System.out.println("TEST");
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    System.out.println("TEST2");
                    //mInterface.onArtistDetailsRequested(mMusic);
                }
            }
        }

    }
}

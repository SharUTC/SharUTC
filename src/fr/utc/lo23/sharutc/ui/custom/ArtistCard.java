package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.scene.control.Label;

public class ArtistCard extends SimpleCard {
    
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
}

package fr.utc.lo23.sharutc.ui.custom.card;

import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.scene.control.Label;


public class AlbumCard extends SimpleCard{
    
    private Music mMusic;
    
    public Label albumName;
    public Label artistName;
    
    public AlbumCard(Music music) {
        super("/fr/utc/lo23/sharutc/ui/fxml/album_card.fxml");
        
        initModel(music);
    }
    
    private void initModel(Music music) {
        mMusic = music;
        albumName.setText(mMusic.getAlbum());
        artistName.setText(mMusic.getArtist());
    }

    public Music getModel() {
        return mMusic;
    }
}

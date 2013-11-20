package fr.utc.lo23.sharutc.ui.custom.card;

import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


public class AlbumCard extends SimpleCard implements EventHandler<Event>{
    
    private Music mMusic;
    private IAlbumCard mInterface;
    
    @FXML
    public Label albumNameLabel;
    @FXML
    public Label artistNameLabel;
    
    private String mAlbumName;
    private String mArtistName;
    
    public AlbumCard(String albumName, String artistName, IAlbumCard i) {
        super("/fr/utc/lo23/sharutc/ui/fxml/album_card.fxml");
        setOnMouseClicked(this);
        mInterface = i;
        mArtistName = artistName;
        mAlbumName = albumName;
        artistNameLabel.setText(mArtistName);
        albumNameLabel.setText(mAlbumName);        
    }
    
    public AlbumCard(Music m, IAlbumCard i) {
        this(m.getAlbum(),m.getArtist(),i);      
    }
    
    
    
    public String getArtistName() {
        return mArtistName;
    }
    
    public String getAlbumName() {
        return mAlbumName;
    }
    
    @Override
    public void handle(Event event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    mInterface.onAlbumDetailRequested(mAlbumName);
                }
            }
        }
    }
    
    public interface IAlbumCard {

        /**
         * user requested more details
         *
         * @param music
         */
        public void onAlbumDetailRequested(String albumName);

    }
}

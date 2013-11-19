package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.card.DraggableCard;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SongDetailController implements Initializable, SongCard.ISongCard {

    @FXML
    public VBox topLeftContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        final Music music = new Music();
        music.setAlbum("Natural Mystic");
        music.setTitle("Crazy Baldhead");
        music.setArtist("Bob Marley");
        
        final SongCard songCard = new SongCard(music , this, false);
        songCard.setPrefWidth(230);
        topLeftContainer.getChildren().add(songCard);
    }    

    @Override
    public void onPlayRequested(Music music) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSongDetailsRequested(Music music) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSongCardSelected(SongCard songCard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSongAddToPlayList(Music music) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onTagEditionRequested(Music music) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDragStart(MouseEvent event, DraggableCard draggableCard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDragStop(DraggableCard draggableCard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

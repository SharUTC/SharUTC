package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.AlbumCard;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlbumsDetailController implements Initializable {
    
    private static final Logger log = LoggerFactory.getLogger(PeopleHomeController.class);
    
    @FXML
    public FlowPane albumsContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        createCards();
        
    }
    
    private void createCards() {
        for(Music m : MainController.population) {
            if(!existsAlready(m)) {
                AlbumCard card = new AlbumCard(m);
                albumsContainer.getChildren().add(card);
            }
        } 
    }
    
    private boolean existsAlready(Music m) {
        for(Object c : albumsContainer.getChildren().toArray()) {
            AlbumCard card = (AlbumCard) c;
            if(card.albumName.getText().equals(m.getAlbum()) && card.artistName.getText().equals(m.getArtist())) {
                return true;
            }
        }      
        return false;
    }
}

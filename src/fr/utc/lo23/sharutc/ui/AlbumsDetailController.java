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
    private String artistWanted;
    public Label titlePage;
            
    @FXML
    public FlowPane albumsContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        artistWanted = "";
        titlePage.setText("Discover new albums");
    }
    
    public void createCards() {
        System.out.println("artistWanted = "+artistWanted);
        for(Music m : MainController.population) {
            if(!existsAlready(m)) {
                if(m.getArtist().equals(artistWanted) || artistWanted.equals("")) {
                    AlbumCard card = new AlbumCard(m);
                    albumsContainer.getChildren().add(card);
                }              
            }
        }
        resetAlbumWanted();
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
    
    public void resetAlbumWanted() {
        artistWanted = "";
    }
    
    public String getArtistWanted() {
        return artistWanted;
    }
    
    public void setArtistWanted(String artistName) {
        artistWanted = artistName;
        titlePage.setText("Discover "+artistName+"'s albums");
    }
}

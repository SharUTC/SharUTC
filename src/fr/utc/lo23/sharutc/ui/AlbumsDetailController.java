package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.card.AlbumCard;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlbumsDetailController implements Initializable, AlbumCard.IAlbumCard {
    
    private static final Logger log = LoggerFactory.getLogger(PeopleHomeController.class);
    public IAlbumsDetailController mInterface;
    private String artistWanted;
    public Label titlePage;
            
    @FXML
    public FlowPane albumsContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        artistWanted = "";
        titlePage.setText("Discover new albums");
    }
    
    public void setInterface(IAlbumsDetailController i) {
        mInterface = i;
    }
    
    public void createCards() {
        for(Music m : MainController.population) {
            if(!existsAlready(m)) {
                if(m.getArtist().equals(artistWanted) || artistWanted.equals("")) {
                    AlbumCard card = new AlbumCard(m, this);
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

    @Override
    public void onAlbumDetailRequested(Music music) {
        log.info("onArtistDetailRequested " + music.getAlbum());
        mInterface.onAlbumDetailRequested(music);
    }
    
    public interface IAlbumsDetailController {

        /**
         * display user details
         *
         * @param music
         */
        public void onAlbumDetailRequested(Music music);
    }
}

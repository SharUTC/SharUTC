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

public class AlbumsDetailController implements RighpaneInterface, Initializable, AlbumCard.IAlbumCard {
    
    private static final Logger log = LoggerFactory.getLogger(PeopleHomeController.class);
    public IAlbumsDetailController mInterface;
    public Label titlePage;
            
    @FXML
    public FlowPane albumsContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titlePage.setText("Discover new albums");
    }
    
    public void setInterface(IAlbumsDetailController i) {
        mInterface = i;
    }
    
    public void createCards(String artistName) {
        for(Music m : MainController.population) {
            if(!existsAlready(m)) {
                if(m.getArtist().equals(artistName) || artistName.equals("")) {
                    AlbumCard card = new AlbumCard(m, this);
                    albumsContainer.getChildren().add(card);
                }              
            }
        }
    }
    
    public void createCards() {
        createCards("");
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

    @Override
    public void onAlbumDetailRequested(Music music) {
        log.info("onArtistDetailRequested " + music.getAlbum());
        mInterface.onAlbumDetailRequested(music);
    }

    @Override
    public void onDetach() {}
    
    public interface IAlbumsDetailController {

        /**
         * display user details
         *
         * @param music
         */
        public void onAlbumDetailRequested(Music music);
    }
}

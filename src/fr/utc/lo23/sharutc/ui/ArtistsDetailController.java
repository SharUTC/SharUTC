package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.AlbumCard;
import fr.utc.lo23.sharutc.ui.custom.ArtistCard;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArtistsDetailController implements Initializable {
    
    private static final Logger log = LoggerFactory.getLogger(PeopleHomeController.class);
    private IArtistsDetailController mInterface;
    
    @FXML
    public FlowPane artistsContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createCards();
    }
    
    private void createCards() {
        for(Music m : MainController.population) {
            if(!existsAlready(m)) {
                ArtistCard card = new ArtistCard(m);
                artistsContainer.getChildren().add(card);
            }
        } 
    }
    
    private boolean existsAlready(Music m) {
        for(Object c : artistsContainer.getChildren().toArray()) {
            ArtistCard card = (ArtistCard) c;
            if(card.artistName.getText().equals(m.getArtist())) {
                return true;
            }
        }      
        return false;
    }
      
    public void onArtistDetailsRequested(Music music) {
        log.info("onArtistDetailsRequested " + music.getArtist());
    }
    
    public interface IArtistsDetailController {

        /**
         * display artist details
         *
         * @param music
         */
        void onArtistDetailsRequested(Music music);
    }
}

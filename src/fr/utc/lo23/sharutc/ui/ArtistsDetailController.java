package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.card.ArtistCard;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArtistsDetailController implements Initializable, ArtistCard.IArtistCard{
    
    private static final Logger log = LoggerFactory.getLogger(PeopleHomeController.class);
    private IArtistsDetailController mInterface;
    
    @FXML
    public FlowPane artistsContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createCards();
    }
    
    public void setInterface(IArtistsDetailController i) {
        mInterface = i;
    }
    
    private void createCards() {
        for(Music m : MainController.population) {
            if(!existsAlready(m)) {
                ArtistCard card = new ArtistCard(m, this);
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

    @Override
    public void onArtistDetailRequested(Music music) {
        log.info("onArtistDetailRequested " + music.getArtist());
        mInterface.onArtistDetailRequested(music);
    }
    
    public interface IArtistsDetailController {

        /**
         * display user details
         *
         * @param music
         */
        public void onArtistDetailRequested(Music music);
    }

}

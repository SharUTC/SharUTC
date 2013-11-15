package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.AlbumCard;
import fr.utc.lo23.sharutc.ui.custom.ArtistCard;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArtistsDetailController implements Initializable {
    
    private static final Logger log = LoggerFactory.getLogger(PeopleHomeController.class);
    
    @FXML
    public FlowPane artistsContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populate();
    }
    
    //TODO Remove once we get a real list of albums
    private void populate() {
        for (int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                final Music music = new Music();
                music.setAlbum("Album " + String.valueOf(i));
                music.setArtist("Artist " + String.valueOf(i));
                if(!existsAlready(music.getArtist())) {
                    ArtistCard card = new ArtistCard(music);
                    artistsContainer.getChildren().add(card);
                }
            }
        }
    }
    
    private boolean existsAlready(String name) {
        for(Object c : artistsContainer.getChildren().toArray()) {
            ArtistCard card = (ArtistCard) c;
            if(card.artistName.getText().equals(name)) {
                return true;
            }
        }
        
        return false;
    }
}

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

        populate();
        
    }
    
    //TODO Remove once we get a real list of albums
    private void populate() {
        for (int i = 0; i < 5; i++) {
            final Music music = new Music();
            music.setAlbum("Album " + String.valueOf(i));         
            for(int j = 0; j < 5; j++) {
                music.setArtist("Artist " + String.valueOf(j));
                AlbumCard card = new AlbumCard(music);
                albumsContainer.getChildren().add(card);
            }
        }
    }
}

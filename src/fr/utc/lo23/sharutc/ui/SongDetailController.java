package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class SongDetailController implements Initializable {

    @FXML
    public VBox topLeftContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        final Music music = new Music();
        music.setAlbum("Natural Mystic");
        music.setTitle("Crazy Baldhead");
        music.setArtist("Bob Marley");
        
        final SongCard songCard = new SongCard(music , null, false);
        songCard.setPrefWidth(230);
        topLeftContainer.getChildren().add(songCard);
    }    
    
}

package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import fr.utc.lo23.sharutc.ui.custom.card.TagCard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

public class SongListController extends SongSelectorController implements Initializable {

    private static final Logger log = LoggerFactory
            .getLogger(SongListController.class);
    @FXML
    public Button addNewSongButton;
    @FXML
    public FlowPane songsContainer;
    @FXML
    public HBox tagContainer;
    @FXML
    public ScrollPane tagScrollPane;

    @Override
    public void init(StackPane dragPreview) {
        super.init(dragPreview);
        //TODO retrieve all user song and add them to the view
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        tagScrollPane.getStyleClass().add("myScrollPaneWithTopBorder");

        addNewSongButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                log.info("Add a new song Button Clicked");
                final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Music files (*.mp3)", "*.mp3");
                final FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Add a Song");
                fileChooser.getExtensionFilters().add(extFilter);
                final File file = fileChooser.showOpenDialog(addNewSongButton.getScene().getWindow());
                if (file != null) {
                    log.info("import song, filePath: " + file.getAbsolutePath());
                    songAdded(file);
                }
            }
        });

//        for (int i = 0; i < 3; i++) {
//            final Music m = new Music();
//            m.setTitle("Music " + i);
//            m.setArtist("Artist " + i);
//            SongCard newCard = new SongCard(m, this, true);
//            songsContainer.getChildren().add(newCard);
//        }
        for(Music m : MainController.population) {
            SongCard newCard = new SongCard(m, this, true);
            songsContainer.getChildren().add(newCard);
        }
        
        showTags();

    }

    /**
     * user add a new song
     *
     * @param song new song added
     */
    private void songAdded(File song) {
        final Music m = new Music();
        m.setFileName(song.getName());
        songsContainer.getChildren().add(new SongCard(m, this, true));
        //TODO add song to user songs
    }
    
    private void showTags() {
        tagContainer.getChildren().clear();
        
        //The "virtual" "All songs" tag
        showTagCard(new TagCard("All Songs"));
        
        //The "virtual" "My Songs" tag
        showTagCard(new TagCard("My Songs"));
        
        //TODO remove when we get the real tags
        for(int i=0; i< 5; i ++) {
            showTagCard(new TagCard("Tag " + String.valueOf(i)));
        }
    }
    
    private void showTagCard(TagCard tagCard) {
        HBox.setMargin(tagCard, new Insets(0, 5, 0, 5));
        tagContainer.getChildren().add(tagCard);
    }
}

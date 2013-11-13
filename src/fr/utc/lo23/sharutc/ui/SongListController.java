package fr.utc.lo23.sharutc.ui;

import java.io.File;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SongListController implements Initializable {
    
    private static final Logger log = LoggerFactory
            .getLogger(SongListController.class);

    public Button addNewSongButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                }
            }
        });
        
    }
}

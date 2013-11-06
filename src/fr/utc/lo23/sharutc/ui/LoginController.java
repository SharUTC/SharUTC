package fr.utc.lo23.sharutc.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 */
public class LoginController implements Initializable {

    private static final Logger log = LoggerFactory
            .getLogger(LoginController.class);
    public HBox titleContainter;
    public ImageView logoContainer;
    public Button buttonSignUp;
    public Button buttonSignIn;
    public Button buttonImport;
    public TextField userNameField;
    public PasswordField passwordField;
    //drop Overlay
    public Region dropOverlay;
    public Label dropOverlayLabel;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final Text text = new Text();
        text.setText("SharUTC");
        text.setId("title");
        titleContainter.getChildren().add(text);

        logoContainer.setCache(true);
        logoContainer.setSmooth(true);
        logoContainer.setPreserveRatio(true);
        logoContainer.setImage(new Image("fr/utc/lo23/sharutc/ui/drawable/logo.png"));

        //hide drop overlay
        hideDropOverlay();
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {
        if (event.getSource() == buttonSignUp) {
            log.info("Sign Up Button Clicked");
        } else if (event.getSource() == buttonSignIn) {
            log.info("Sign In Button Clicked");
        } else if (event.getSource() == buttonImport) {
            log.info("Import Button Clicked");
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Profile");
            final File file = fileChooser.showOpenDialog(buttonImport.getScene().getWindow());
            if(file != null) {
                log.info("import file, filePath: "+file.getAbsolutePath());
            }
        }
    }

    @FXML
    public void handleTextEntered(ActionEvent actionEvent) throws IOException {
        log.info("username: " + userNameField.getText() + " password: "
                + passwordField.getText());
    }
    
    @FXML
    public void handleDragEntered(DragEvent dragEvent) throws IOException {
        showDropOverlay();
        dragEvent.consume();
    }

    @FXML
    public void handleDragOver(DragEvent dragEvent) throws IOException {
        final Dragboard db = dragEvent.getDragboard();
        if (db.hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }        
        dragEvent.consume();        
    }

    @FXML
    public void handleDragExited(DragEvent dragEvent) throws IOException {
        hideDropOverlay();
        dragEvent.consume();
    }

    @FXML
    public void handleDragDropped(DragEvent dragEvent) throws IOException {
        final Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            for (File file : db.getFiles()) {
                log.info("file dropped, filePath: " + file.getAbsolutePath());
            }
        }
        hideDropOverlay();
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    private void hideDropOverlay() {
        dropOverlay.setVisible(false);
        dropOverlayLabel.setVisible(false);
    }

    private void showDropOverlay() {
        dropOverlay.setVisible(true);
        dropOverlayLabel.setVisible(true);
    }
}

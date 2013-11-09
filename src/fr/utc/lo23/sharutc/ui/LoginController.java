package fr.utc.lo23.sharutc.ui;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import fr.utc.lo23.sharutc.ui.custom.SharutcLogo;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    
    public StackPane loginRoot;
    public SharutcLogo sharutcLogo;
    public Button buttonSignUp;
    public Button buttonSignIn;
    public Button buttonImport;
    public TextField userNameField;
    public PasswordField passwordField;
    //drop Overlay
    public Region dropOverlay;
    public Label dropOverlayLabel;
    public VBox errorContainer;
    @Inject
    private GuiceFXMLLoader mFxmlLoader;
    private ArrayList<String> mErrorMessages;

    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mErrorMessages = new ArrayList<>();
        
        loginRoot.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                sharutcLogo.animate(mouseEvent);
            }      
        });
        
        //hide drop overlay
        hideDropOverlay();
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {
        if (event.getSource() == buttonSignUp) {
            log.info("Sign Up Button Clicked");
            Parent root = mFxmlLoader.load(getClass().getResource("fxml/registration.fxml")).getRoot();
            buttonSignUp.getScene().setRoot(root);
        } else if (event.getSource() == buttonSignIn) {
            log.info("Sign In Button Clicked");
            signInButtonClicked();
        } else if (event.getSource() == buttonImport) {
            log.info("Import Button Clicked");
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Profile");
            final File file = fileChooser.showOpenDialog(buttonImport.getScene().getWindow());
            if (file != null) {
                log.info("import file, filePath: " + file.getAbsolutePath());
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

    private void signInButtonClicked() {
        errorContainer.getChildren().clear();
        if (!validateForm()) {
            displayErrorMessages();
        }
    }

    private void displayErrorMessages() {
        for (String errorMessage : mErrorMessages) {
            errorContainer.getChildren().add(new Label(errorMessage));
        }
    }

    private boolean validateForm() {
        boolean isFormValid = false;
        int emptyField = 0;
        mErrorMessages.clear();

        if (userNameField.getText().isEmpty()) {
            emptyField += 1;
        }

        if (passwordField.getText().isEmpty()) {
            emptyField += 2;
        }

        if (emptyField != 0) {
            //at least one empty field
            switch (emptyField) {
                case 1:
                    mErrorMessages.add(
                            "User name can't be empty.");
                    break;
                case 2:
                    mErrorMessages.add(
                            "Password can't be empty.");
                    break;
                case 3:
                    mErrorMessages.add(
                            "User name & Password can't be empty.");
                    break;
            }
        } else {
            isFormValid = true;
        }

        return isFormValid;
    }
}

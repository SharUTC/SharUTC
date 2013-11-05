package fr.utc.lo23.sharutc.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.*;
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

    /**
     * Initializes the controller class.
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
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {
        if (event.getSource() == buttonSignUp) {
            log.info("Sign Up Button Clicked");
        } else if (event.getSource() == buttonSignIn) {
            log.info("Sign In Button Clicked");
        } else if (event.getSource() == buttonImport) {
            log.info("Import Button Clicked");
        }
    }

    @FXML
    public void handleTextEntered(ActionEvent actionEvent) throws IOException {
        log.info("username: " + userNameField.getText() + " password: "
                + passwordField.getText());
    }

}

package fr.utc.lo23.sharutc.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 */
public class RegistrationController implements Initializable {
    
    private static final Logger log = LoggerFactory
            .getLogger(RegistrationController.class);
    
    public Button buttonCancel;
    public ImageView logoContainer;

    /**
     * Initializes the controller class.
     * 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logoContainer.setCache(true);
        logoContainer.setSmooth(true);
        logoContainer.setPreserveRatio(true);
        logoContainer.setImage(new Image("fr/utc/lo23/sharutc/ui/drawable/logo.png"));
    }    
    
    @FXML
    public void handleCancelRegistration(ActionEvent actionEvent) throws IOException {
        log.info("Cancel Button Clicked");
        Parent root = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
        buttonCancel.getScene().setRoot(root);
    }
}

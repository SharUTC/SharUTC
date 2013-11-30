package fr.utc.lo23.sharutc.ui;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.account.ConnectionRequestCommand;
import fr.utc.lo23.sharutc.controler.command.account.ImportProfileCommand;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelImpl;
import fr.utc.lo23.sharutc.model.ErrorBus;
import fr.utc.lo23.sharutc.model.ErrorMessage;
import fr.utc.lo23.sharutc.ui.custom.SharutcLogo;
import fr.utc.lo23.sharutc.ui.navigation.NavigationController;
import fr.utc.lo23.sharutc.ui.navigation.NavigationHandler;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
 * A FXML Controller that displays a login page.
 */
public class LoginController extends NavigationController implements Initializable, PropertyChangeListener {

    private static final Logger log = LoggerFactory
            .getLogger(LoginController.class);
    /*
     * The UI attributs
     */
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
    /*
     * The private attributs
     */
    @Inject
    private GuiceFXMLLoader mFxmlLoader;
    private ArrayList<String> mErrorMessages;
    @Inject
    private AppModel mAppModel;
    @Inject
    private ConnectionRequestCommand mConnectionRequestCommand;
    @Inject
    private ImportProfileCommand mImportProfileCommand;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mErrorMessages = new ArrayList();

        //Listen to the mouseMove event to animate the logo
        loginRoot.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                sharutcLogo.animate(mouseEvent);
            }
        });

        if (mAppModel.getProfile() != null && mAppModel.getProfile().getUserInfo() != null) {
            userNameField.setText(mAppModel.getProfile().getUserInfo().getLogin());
        }

        //hide drop overlay
        hideDropOverlay();

        //listen for changes on the AppModel
        mAppModel.addPropertyChangeListener(this);

        //listen for changes on the Error Bus
        mAppModel.getErrorBus().addPropertyChangeListener(this);
    }

    //TODO remove after test
    public void automaticLogin(String login, String password) {
        mConnectionRequestCommand.setLogin(login);
        mConnectionRequestCommand.setPassword(password);
        mConnectionRequestCommand.execute();
    }

    /**
     * Handles the ActionEvents of the three buttons "sign up" "sign in" and
     * "import"
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {
        if (event.getSource() == buttonSignUp) {
            log.info("Sign Up Button Clicked");
            goToRegistrationPage();
        } else if (event.getSource() == buttonSignIn) {
            log.info("Sign In Button Clicked");
            signInRequest();
        } else if (event.getSource() == buttonImport) {
            log.info("Import Button Clicked");
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Profile");
            final File file = fileChooser.showOpenDialog(buttonImport.getScene().getWindow());
            if (file != null) {
                log.info("import file, filePath: " + file.getAbsolutePath());
                importProfile(file.getAbsolutePath());
            }
        }
    }

    /**
     * Handles the ActionEvent of the two TextFields "userName" and "password"
     *
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void handleTextEntered(ActionEvent actionEvent) throws IOException {
        signInRequest();
    }

    /**
     * Handles dragEntered Events. Shows the drop overlay.
     *
     * @param dragEvent
     * @throws IOException
     */
    @FXML
    public void handleDragEntered(DragEvent dragEvent) throws IOException {
        showDropOverlay();
        dragEvent.consume();
    }

    /**
     * Handles dragOver Events. Accepts the transfer is there is a file in the
     * Dragboard.
     *
     * @param dragEvent
     * @throws IOException
     */
    @FXML
    public void handleDragOver(DragEvent dragEvent) throws IOException {
        final Dragboard db = dragEvent.getDragboard();
        if (db.hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
        dragEvent.consume();
    }

    /**
     * Handles dragExited Events. Hides the drop overlay.
     *
     * @param dragEvent
     * @throws IOException
     */
    @FXML
    public void handleDragExited(DragEvent dragEvent) throws IOException {
        hideDropOverlay();
        dragEvent.consume();
    }

    /**
     * Handles dragDrop Events. Tries to import the dropped file and then hides
     * the drop overlay.
     *
     * @param dragEvent
     * @throws IOException
     */
    @FXML
    public void handleDragDropped(DragEvent dragEvent) throws IOException {
        final Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            final File file = db.getFiles().get(0);
            log.info("file dropped, filePath: " + file.getAbsolutePath());
            importProfile(file.getAbsolutePath());
        }
        hideDropOverlay();
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    /**
     * Hides the drop overlay.
     */
    private void hideDropOverlay() {
        dropOverlay.setVisible(false);
        dropOverlayLabel.setVisible(false);
    }

    /**
     * Show the drop overlay.
     */
    private void showDropOverlay() {
        dropOverlay.setVisible(true);
        dropOverlayLabel.setVisible(true);
    }

    /**
     * Validates the login form. If the form is not valid, the error messages
     * are shown.
     */
    private void signInRequest() {
        errorContainer.getChildren().clear();
        if (!validateForm()) {
            displayErrorMessages();
        } else {
            mConnectionRequestCommand.setLogin(userNameField.getText());
            mConnectionRequestCommand.setPassword(passwordField.getText());
            mConnectionRequestCommand.execute();
        }
    }

    /**
     * Shows the error messages.
     */
    private void displayErrorMessages() {
        for (String errorMessage : mErrorMessages) {
            errorContainer.getChildren().add(new Label(errorMessage));
        }
    }

    /**
     * Validates the login form. The errors are added to mErrorMessages for
     * future use.
     *
     * @return true if the form is valid, false otherwise.
     */
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String propertyName = evt.getPropertyName();
        if (AppModelImpl.Property.PROFILE.name().equals(propertyName)) {
            log.info("Profile Changed");
            goToMainPage();
        } else if (ErrorBus.Property.APPLICATION_ERROR_MESSAGE.name().equals(propertyName)) {
            log.info("Application Error Message Changed");
            errorContainer.getChildren().clear();
            errorContainer.getChildren().add(new Label(((ErrorMessage) evt.getNewValue()).getMessage()));
        }
    }

    private void goToRegistrationPage() {
        mAppModel.getErrorBus().removePropertyChangeListener(this);
        mAppModel.removePropertyChangeListener(this);
        mNavigationHandler.goToRegistrationPage();
    }

    private void goToMainPage() {
        mAppModel.getErrorBus().removePropertyChangeListener(this);
        mAppModel.removePropertyChangeListener(this);
        mNavigationHandler.goToMainPage();
    }

    private void importProfile(String filePath) {
        mImportProfileCommand.setPath(filePath);
        mImportProfileCommand.execute();
    }
}

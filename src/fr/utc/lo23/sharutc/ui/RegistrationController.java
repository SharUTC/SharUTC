package fr.utc.lo23.sharutc.ui;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import fr.utc.lo23.sharutc.ui.custom.SharutcLogo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * A FXML Controller that displays a registration page.
 *
 */
public class RegistrationController implements Initializable {

    private static final Logger log = LoggerFactory
            .getLogger(RegistrationController.class);
    /*
     * The UI attributs
     */
    public BorderPane registrationRoot;
    public Button buttonCancel;
    public SharutcLogo sharutcLogo;
    public VBox errorContainer;
    public TextField userNameField;
    public TextField passwordField;
    public TextField passwordConfirmField;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField ageField;
    /*
     * The private attributs
     */
    @Inject
    private GuiceFXMLLoader mFxmlLoader;
    private ArrayList<String> mErrorMessages;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mErrorMessages = new ArrayList();

        //Listen to the mouseMove event to animate the logo
        registrationRoot.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                sharutcLogo.animate(mouseEvent);
            }
        });

    }

    /**
     * Handles the ActionEvents of the Sign Up Button.
     *
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void handleSignUp(ActionEvent actionEvent) throws IOException {
        log.info("Sign Up Button Clicked");
        errorContainer.getChildren().clear();
        if (!validateForm()) {
            displayErrorMessages();
        }
    }

    /**
     * Handles the ActionEvents of the Cancel Button. Goes back to the login
     * page.
     *
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void handleCancelRegistration(ActionEvent actionEvent) throws IOException {
        log.info("Cancel Button Clicked");
        Parent root = mFxmlLoader.load(getClass().getResource("fxml/login.fxml")).getRoot();
        buttonCancel.getScene().setRoot(root);
    }

    /**
     * Shows the error messages stored in mErrorMessages.
     */
    private void displayErrorMessages() {
        for (String errorMessage : mErrorMessages) {
            errorContainer.getChildren().add(new Label(errorMessage));
        }
    }

    /**
     * Validates the registration form.
     *
     * @return true if the form is valid, false otherwise.
     */
    private boolean validateForm() {
        mErrorMessages.clear();
        boolean isFormValid = false;
        ArrayList<String> emptyFields = new ArrayList();

        //Check username, firstname and lastname
        checkEmptyField(emptyFields, userNameField, "User name");
        checkEmptyField(emptyFields, firstNameField, "First name");
        checkEmptyField(emptyFields, lastNameField, "Last name");

        //Check password & password confirmation
        final boolean isPasswordEmpty = checkEmptyField(emptyFields,
                passwordField, "Password");
        final boolean isPasswordConfirmEmpty = checkEmptyField(emptyFields,
                passwordConfirmField, "Password confirmation");
        if (!isPasswordConfirmEmpty && !isPasswordConfirmEmpty
                && !passwordField.getText().equals(passwordConfirmField.getText())) {
            mErrorMessages.add("Password and password confirmation must match.");
        }

        //Check Age
        final boolean isAgeEmpty = checkEmptyField(emptyFields, ageField, "Age");
        if (!isAgeEmpty && !isAgeValid()) {
            mErrorMessages.add("Age is not valid.");
        }


        if (!emptyFields.isEmpty()) {
            mErrorMessages.add(makeEmptyFieldErrorMessage(emptyFields));
        }

        return mErrorMessages.isEmpty();
    }

    /**
     * Checks if a {@link TextField} is empty and stores his name for future
     * use.
     *
     * @param emptyFields the {@link ArrayList<String>} that stores the name of
     * the empty fields
     * @param field the {@link TextField} to check
     * @param fieldName the name of the field
     * @return true if field is empty
     */
    private boolean checkEmptyField(ArrayList<String> emptyFields,
            TextField field, String fieldName) {
        boolean isEmpty = false;
        if (field.getText().isEmpty()) {
            emptyFields.add(fieldName);
            isEmpty = true;
        }
        return isEmpty;
    }

    /**
     * Create a human understandable message telling which fields of the
     * registration form are empty.
     *
     * @param emptyFields the {@link ArrayList<String>} that contains the name
     * of the empty fields.
     * @return
     */
    private String makeEmptyFieldErrorMessage(ArrayList<String> emptyFields) {
        String errorMessage = new String();
        final int size = emptyFields.size();
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                errorMessage += emptyFields.get(i);
            } else if (i == size - 2) {
                errorMessage += emptyFields.get(i) + " & ";
            } else {
                errorMessage += emptyFields.get(i) + ", ";
            }
        }
        return errorMessage + " can't be empty.";
    }

    /**
     * Returns true is the {@code ageField} contains a integer between 0 and
     * 120, false otherwise.
     *
     * @return
     */
    public boolean isAgeValid() {
        final String ageStr = ageField.getText();
        boolean isAgeValid = false;

        try {
            final Integer age = Integer.parseInt(ageStr);
            if (age > 0 && age < 120) {
                isAgeValid = true;
            }
        } catch (NumberFormatException e) {
        }

        return isAgeValid;
    }
}

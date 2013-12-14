package fr.utc.lo23.sharutc.ui;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.account.AccountCreationCommand;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelImpl;
import fr.utc.lo23.sharutc.model.ErrorBus;
import fr.utc.lo23.sharutc.model.ErrorMessage;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.SharutcLogo;
import fr.utc.lo23.sharutc.ui.navigation.NavigationController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;

/**
 * A FXML Controller that displays a registration page.
 *
 */
public class RegistrationController extends NavigationController implements Initializable, PropertyChangeListener {

    private static final Logger log = LoggerFactory
            .getLogger(RegistrationController.class);
    private static final String DEFAULT_FIRST_NAME = "First Name";
    private static final String DEFAULT_LAST_NAME = "Last Name";
    private static final int DEFAULT_AGE = 20;
    /*
     * The UI attributs
     */
    @FXML
    public BorderPane registrationRoot;
    @FXML
    public Button buttonCancel;
    @FXML
    public SharutcLogo sharutcLogo;
    @FXML
    public VBox errorContainer;
    @FXML
    public TextField userNameField;
    @FXML
    public TextField passwordField;
    @FXML
    public TextField passwordConfirmField;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public TextField ageField;
    @FXML
    public ProgressIndicator progressIndicatorSignUp;
    @FXML
    public Button buttonSignUp;
    /*
     * The private attributs
     */
    @Inject
    private GuiceFXMLLoader mFxmlLoader;
    private ArrayList<String> mErrorMessages;
    @Inject
    private AccountCreationCommand mAccountCreationCommand;
    @Inject
    private AppModel mAppModel;

    /**
     * Initializes the controller class. This method is called automatically
     * when the controller is loaded.
     *
     * @param url {@link URL}
     * @param rb {@link ResourceBundle}
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

        //listen to changes made on the AppModel
        mAppModel.addPropertyChangeListener(this);

        //listen to changes made on the ErrorBus
        mAppModel.getErrorBus().addPropertyChangeListener(this);

        //Hide the progress indicator
        progressIndicatorSignUp.setVisible(false);
    }

    /**
     * Handles the ActionEvents of the Sign Up Button.
     *
     * @param actionEvent {@link ActionEvent}
     * @throws IOException
     */
    @FXML
    public void handleSignUp(ActionEvent actionEvent) throws IOException {
        log.info("Sign Up Button Clicked");
        errorContainer.getChildren().clear();
        if (!validateForm()) {
            displayErrorMessages();
        } else {
            
            //update the UI to notify the user that their account is being 
            //created
            progressIndicatorSignUp.setVisible(true);
            buttonCancel.setDisable(true);
            buttonSignUp.setVisible(false);
            
            final Runnable accountCreationRunnable = new Runnable() {
                @Override
                public void run() {
                    final UserInfo userInfo = new UserInfo();
                    //mandatory fields
                    userInfo.setLogin(userNameField.getText());
                    userInfo.setPassword(passwordField.getText());

                    //optionnal fields with default value
                    userInfo.setFirstName(firstNameField.getText().isEmpty() ? DEFAULT_FIRST_NAME : firstNameField.getText());
                    userInfo.setLastName(lastNameField.getText().isEmpty() ? DEFAULT_LAST_NAME : lastNameField.getText());
                    userInfo.setAge(ageField.getText().isEmpty() ? DEFAULT_AGE : Integer.valueOf(ageField.getText()));

                    mAccountCreationCommand.setUserInfo(userInfo);
                    mAccountCreationCommand.execute();
                }
            };       
            new Thread(accountCreationRunnable, "Account Creation").start();
        }
    }

    /**
     * Handles the ActionEvents of the Cancel Button. Goes back to the login
     * page.
     *
     * @param actionEvent {@link ActionEvent}
     * @throws IOException
     */
    @FXML
    public void handleCancelRegistration(ActionEvent actionEvent) throws IOException {
        log.info("Cancel Button Clicked");
        goToLoginPage();
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

        //According to the specification first name, last name are optionnal.        
        //checkEmptyField(emptyFields, firstNameField, "First name");
        //checkEmptyField(emptyFields, lastNameField, "Last name");

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
        //According to the specification age is optionnal.
        //final boolean isAgeEmpty = checkEmptyField(emptyFields, ageField, "Age");
        if (!ageField.getText().isEmpty() && !isAgeValid()) {
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

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt {@link PropertyChangeEvent}
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if(Platform.isFxApplicationThread()) {
            handlePropertyChangeEvent(evt);
        } else {
          Platform.runLater(new Runnable() {
              @Override
              public void run() {
                  handlePropertyChangeEvent(evt);
              }
          });
        }
    }
    
    private void handlePropertyChangeEvent(PropertyChangeEvent evt) {
        final String propertyName = evt.getPropertyName();
        if (AppModelImpl.Property.PROFILE.name().equals(propertyName)) {
            goToLoginPage();
        } else if (ErrorBus.Property.APPLICATION_ERROR_MESSAGE.name().equals(propertyName)) {
            log.info("Application Error Message Changed");
            errorContainer.getChildren().clear();
            errorContainer.getChildren().add(new Label(((ErrorMessage) evt.getNewValue()).getMessage()));
        }
    }    

    /**
     * Load the login page properly.
     */
    private void goToLoginPage() {
        mAppModel.removePropertyChangeListener(this);
        mAppModel.getErrorBus().removePropertyChangeListener(this);
        mNavigationHandler.goToLoginPage();
    }
}

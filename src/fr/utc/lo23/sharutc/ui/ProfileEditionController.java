package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.account.EditUserInfoCommand;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.util.FormUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An FXML Controller class used to edit the user's profile.
 */
public class ProfileEditionController implements Initializable {

    private static final Logger log = LoggerFactory
            .getLogger(ProfileEditionController.class);
    @FXML
    public VBox errorContainer;
    @FXML
    public TextField textFieldLastName;
    @FXML
    public TextField textFieldFirstName;
    @FXML
    public TextField textFieldAge;
    @FXML
    public PasswordField passwordFieldCurrent;
    @FXML
    public PasswordField passwordFieldNew;
    @FXML
    public PasswordField passwordFieldConfirmNew;
    @FXML
    public Button buttonSaveProfile;
    @FXML
    public Button buttonChangePassword;
    private EventHandler<ActionEvent> mButtonHandler;
    @Inject
    AppModel mAppModel;
    @Inject
    private EditUserInfoCommand mEditUserInfoCommand;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorContainer.getChildren().clear();

        //Listen to ActionEvent on the two buttons
        mButtonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                final Object source = t.getSource();
                if (source.equals(buttonSaveProfile)) {
                    log.debug("save profile clicked !");
                    errorContainer.getChildren().clear();
                    if (isEditFormValid()) {
                        editProfile();
                    }
                } else if (source.equals(buttonChangePassword)) {
                    log.debug("change password clicked !");
                    errorContainer.getChildren().clear();
                    if (isPasswordFormValid()) {
                        changePassword();
                    }
                }
            }
        };
        buttonChangePassword.setOnAction(mButtonHandler);
        buttonSaveProfile.setOnAction(mButtonHandler);
        
        showCurrentUserInfo();
    }
    
    private void editUserInfo(final UserInfo userInfo) {
        final Runnable editUserInfoRunnable = new Runnable() {
            @Override
            public void run() {
                log.debug("Edit User Info !");
                mEditUserInfoCommand.setUserInfo(userInfo);
                mEditUserInfoCommand.execute();
            }
        };
        new Thread(editUserInfoRunnable, "Edit User Info").start();
    }
    
    private void changePassword() {
        final UserInfo userInfo = mAppModel.getProfile().getUserInfo().clone();
        final String newPassword = passwordFieldNew.getText();
        userInfo.setPassword(UserInfo.sha1(newPassword));
        log.debug("Change password !");
        editUserInfo(userInfo);
    }
    
    private void editProfile() {
        final UserInfo userInfo = mAppModel.getProfile().getUserInfo().clone();
        final Integer newAge = Integer.valueOf(textFieldAge.getText());
        final String newLastName = textFieldLastName.getText();
        final String newFirstName = textFieldFirstName.getText();
        
        if(newLastName.equals(userInfo.getLastName())
                && newFirstName.equals(userInfo.getFirstName())
                && newAge.equals(userInfo.getAge())) {
            log.debug("There is nothing to change !");
        } else {
            userInfo.setFirstName(newFirstName);
            userInfo.setLastName(newLastName);
            userInfo.setAge(newAge);
            log.debug("Edit user info !");
            editUserInfo(userInfo);
        }
    }
    
    private void showCurrentUserInfo() {
        final UserInfo userInfo = mAppModel.getProfile().getUserInfo();
        textFieldLastName.setText(userInfo.getLastName());
        textFieldFirstName.setText(userInfo.getFirstName());
        textFieldAge.setText(String.valueOf(userInfo.getAge()));
    }

    private boolean isEditFormValid() {
        final String lastName = textFieldLastName.getText();
        final String firstName = textFieldFirstName.getText();
        final String ageStr = textFieldAge.getText();
        final UserInfo currentUserInfo = mAppModel.getProfile().getUserInfo();
        boolean isValid = false;

        if (lastName.isEmpty() || firstName.isEmpty() || ageStr.isEmpty()) {
            showErrorMessage("All fields must be filled!");
        } else if (!FormUtils.isAgeValid(ageStr)) {
            showErrorMessage("Age is not valid!");
        } else {
            isValid = true;
        }

        return isValid;
    }

    private boolean isPasswordFormValid() {
        final String passwordCurrent = passwordFieldCurrent.getText();
        final String passwordNew = passwordFieldNew.getText();
        final String passwordConfirm = passwordFieldConfirmNew.getText();
        boolean isValid = false;

        if (passwordCurrent.isEmpty() || passwordNew.isEmpty()
                || passwordConfirm.isEmpty()) {
            showErrorMessage("All fields must be filled!");
        } else if (!passwordNew.equals(passwordConfirm)) {
            showErrorMessage("Passwords don't match!");
        } else if (!mAppModel.getProfile().getUserInfo().getPassword().equals(UserInfo.sha1(passwordCurrent))) {
            showErrorMessage("Your current password is wrong!");
        } else if (passwordCurrent.equals(passwordNew)) {
            showErrorMessage("You can't change for the same password!");
        } else {
            isValid = true;
        }

        return isValid;
    }

    private void showErrorMessage(String errorMessage) {
        errorContainer.getChildren().add(new Label(errorMessage));
    }
}

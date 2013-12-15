package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.account.EditUserInfoCommand;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.util.FormUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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
    @FXML
    public Label labelChangeSaved;
    private EventHandler<ActionEvent> mButtonHandler;
    @Inject
    AppModel mAppModel;
    @Inject
    private EditUserInfoCommand mEditUserInfoCommand;
    private IProfileEditionController mCallback;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Listen to ActionEvent on the two buttons
        mButtonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                final Object source = t.getSource();
                if (source.equals(buttonSaveProfile)) {
                    log.debug("save profile clicked !");
                    labelChangeSaved.setVisible(false);
                    errorContainer.getChildren().clear();
                    if (isEditFormValid()) {
                        editProfile();
                    }
                } else if (source.equals(buttonChangePassword)) {
                    log.debug("change password clicked !");
                    labelChangeSaved.setVisible(false);
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

        mCallback = new DummyCallBack();
    }

    /**
     * Set an {@link IProfileEditionController} that will be notified on profile
     * edition.
     *
     * @param callback {@link IProfileEditionController} to be notified.
     */
    public void setInterface(IProfileEditionController callback) {
        if (callback == null) {
            mCallback = new DummyCallBack();
        } else {
            mCallback = callback;
        }
    }

    /**
     * Edit the current UserInfo.
     *
     * Run a new {@link Task} that executes an {@link EditUserInfoCommand}.
     *
     * @param userInfo the new {@link UserInfo} to be set.
     */
    private void editUserInfo(final UserInfo userInfo) {
        final Task<Void> editUserInfoRunnable = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                log.debug("Edit User Info !");
                mEditUserInfoCommand.setUserInfo(userInfo);
                mEditUserInfoCommand.execute();
                return null;
            }
        };

        editUserInfoRunnable.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                mCallback.onUserInfoEdition();
                labelChangeSaved.setVisible(true);
            }
        });
        new Thread(editUserInfoRunnable, "Edit User Info").start();
    }

    /**
     * Change the current password.
     *
     * The new password is retrieved from the passwordFieldNew.
     */
    private void changePassword() {
        final UserInfo userInfo = mAppModel.getProfile().getUserInfo().clone();
        final String newPassword = passwordFieldNew.getText();
        userInfo.setPassword(UserInfo.sha1(newPassword));
        log.debug("Change password !");
        editUserInfo(userInfo);
    }

    /**
     * Edit the current profile.
     */
    private void editProfile() {
        final UserInfo userInfo = mAppModel.getProfile().getUserInfo().clone();
        final Integer newAge = Integer.valueOf(textFieldAge.getText());
        final String newLastName = textFieldLastName.getText();
        final String newFirstName = textFieldFirstName.getText();

        if (newLastName.equals(userInfo.getLastName())
                && newFirstName.equals(userInfo.getFirstName())
                && newAge.equals(userInfo.getAge())) {
            log.debug("There is nothing to change !");
            showErrorMessage("Nothing to change!");
        } else {
            userInfo.setFirstName(newFirstName);
            userInfo.setLastName(newLastName);
            userInfo.setAge(newAge);
            log.debug("Edit user info !");
            editUserInfo(userInfo);
        }
    }

    /**
     * Show the information stored in the current {@link UserInfo}.
     */
    private void showCurrentUserInfo() {
        final UserInfo userInfo = mAppModel.getProfile().getUserInfo();
        textFieldLastName.setText(userInfo.getLastName());
        textFieldFirstName.setText(userInfo.getFirstName());
        textFieldAge.setText(String.valueOf(userInfo.getAge()));
    }

    /**
     * Check if the edition form is valid.
     *
     * @return true is the form is valid, false otherwise.
     */
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

    /**
     * Check if the password form is valid.
     *
     * @return true is the form is valid, false otherwise.
     */
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

    /**
     * Show an error message.
     *
     * @param errorMessage the message to be shown.
     */
    private void showErrorMessage(String errorMessage) {
        errorContainer.getChildren().add(new Label(errorMessage));
    }

    /**
     * A simple interface used as a callback since the edition of the
     * {@link UserInfo} does not trigger any events.
     */
    public interface IProfileEditionController {

        /**
         * The {@link IProfileEditionController} is being notified that the
         * {@link UserInfo} has just been edited.
         */
        void onUserInfoEdition();
    }

    /**
     * A dummy implementation of the {@link IProfileEditionController}.
     */
    private class DummyCallBack implements IProfileEditionController {

        @Override
        public void onUserInfoEdition() {
        }
    }
}

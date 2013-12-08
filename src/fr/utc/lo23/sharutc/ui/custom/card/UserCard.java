package fr.utc.lo23.sharutc.ui.custom.card;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * A {@link SimpleCard} that can display the information of a user.
 * This class notifies a {@link IUserCard} on double click.
 */
public class UserCard extends SimpleCard implements EventHandler<Event> {

    @FXML
    public Label userLogin;
    @FXML
    public Label userName;
    private UserInfo mUserInfo;
    private IUserCard mInterface;

    public UserCard(UserInfo userInfo, IUserCard i) {
        this(userInfo, i, "/fr/utc/lo23/sharutc/ui/fxml/user_card.fxml");
    }

    protected UserCard(UserInfo userInfo, IUserCard i, String resourceFXML) {
        super(resourceFXML);
        mInterface = i;
        if (i != null) {
            setOnMouseClicked(this);
        }
        initModel(userInfo);
    }

    /**
     * Extract the login, the first name and the last name 
     * from a {@link UserInfo}, and show them.
     * 
     * @param userInfo the {@link UserInfo} from which the data is extracted.
     */
    private void initModel(UserInfo userInfo) {
        mUserInfo = userInfo;
        userLogin.setText(mUserInfo.getLogin());
        userName.setText(mUserInfo.getFirstName() + " " + mUserInfo.getLastName());
    }

    /**
     * Return the {@link UserInfo} from which the data was extracted.
     * 
     * @return a {@link UserInfo} 
     */
    public UserInfo getModel() {
        return mUserInfo;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    mInterface.onPeopleDetailsRequested(mUserInfo);
                }
            }
        }

    }

    /**
     * A callback used by a {@link UserCard} to notify that the user
     * wants to view the detail of a user.
     */
    public interface IUserCard {

        /**
         * The {@link IUserCard} is being asked to show the {@link UserInfo}.
         *
         * @param userInfo the {@link UserInfo} to show.
         */
        public void onPeopleDetailsRequested(UserInfo userInfo);
    }
}

package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class UserCard extends SimpleCard implements EventHandler<Event> {

    private UserInfo mUserInfo;

    public Label userLogin;
    public Label userName;

    private IUserCard mInterface;

    public UserCard(UserInfo userInfo, IUserCard i) {
        this(userInfo, i, "../fxml/user_card.fxml");
    }

    protected UserCard(UserInfo userInfo, IUserCard i, String resourceFXML) {
        super(resourceFXML);
        mInterface = i;
        if(i!=null)
            setOnMouseClicked(this);
        initModel(userInfo);
    }

    private void initModel(UserInfo userInfo) {
        mUserInfo = userInfo;
        userLogin.setText(mUserInfo.getLogin());
        userName.setText(mUserInfo.getFirstName() + " " + mUserInfo.getLastName());
    }

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

    public interface IUserCard {

        /**
         * user requested more details
         *
         * @param userInfo
         */
        public void onPeopleDetailsRequested(UserInfo userInfo);

    }

}

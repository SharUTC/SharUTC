package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PeopleDetailController implements Initializable {

    public Label login;
    public Button addToFriendsButton;
    private UserInfo mUserInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }
    
    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        login.setText(mUserInfo.getLogin());
    }

    public void handleAddToFriendsClicked(ActionEvent actionEvent) {

    }
}

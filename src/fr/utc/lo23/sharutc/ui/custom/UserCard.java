package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

public class UserCard extends SimpleCard {
    private final UserInfo mUserInfo;  
    
    public Label userLogin;
    public Label userName;
    
    public UserCard(UserInfo userInfo) {    
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("../fxml/user_card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            mUserInfo = userInfo;
            
            userLogin.setText(mUserInfo.getLogin());
            userName.setText(mUserInfo.getFirstName() + " " + mUserInfo.getLastName());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
                
    }

}

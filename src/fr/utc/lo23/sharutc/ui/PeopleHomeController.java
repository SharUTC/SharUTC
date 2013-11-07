package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.UserCard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.FlowPane;

public class PeopleHomeController implements Initializable {
    
    @FXML
    public FlowPane peopleContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populate();
    }

    //TODO Remove once we get a real list of user
    private void populate() {
        for (int i = 0; i < 50; i++) {
            final UserInfo userInfo = new UserInfo();
            userInfo.setLogin("Login " + String.valueOf(i));
            userInfo.setLastName("LastName");
            userInfo.setFirstName("FirstName");
            
            peopleContainer.getChildren().add(new UserCard(userInfo));
        }
    }
}

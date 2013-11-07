package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import javafx.scene.control.Button;

public class PeopleCard extends UserCard {

    public Button deleteButton;
    public Button detailButton;

    public PeopleCard(UserInfo userInfo) {
        super(userInfo, "../fxml/people_card.fxml");
        getStyleClass().add("peopleCard");

    }

    public void onHover(boolean isHover) {
        deleteButton.setVisible(isHover);
        detailButton.setVisible(isHover);
    }
}

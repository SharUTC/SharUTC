package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;

public class PeopleCard extends UserCard {

    public PeopleCard(UserInfo userInfo) {
        super(userInfo, "../fxml/people_card.fxml");
        getStyleClass().add("peopleCard");
    }
}

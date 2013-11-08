package fr.utc.lo23.sharutc.ui.custom;


import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GroupCard extends SimpleCard {

    @FXML
    public Button deleteButton;

    @FXML
    public Button editButton;

    @FXML
    public Button rightsButton;


    public GroupCard() {
        super("../fxml/group_card.fxml");
        getStyleClass().add("groupCard");
    }

    public void onHover(boolean isHover) {
        deleteButton.setVisible(isHover);
        editButton.setVisible(isHover);
        rightsButton.setVisible(isHover);
    }
}

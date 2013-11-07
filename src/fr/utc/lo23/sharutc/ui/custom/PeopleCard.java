package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class PeopleCard extends UserCard implements EventHandler<MouseEvent> {

    private IPeopleCard mInterface;

    public Button deleteButton;
    public Button detailButton;

    public PeopleCard(UserInfo userInfo) {
        super(userInfo, "../fxml/people_card.fxml");
        getStyleClass().add("peopleCard");

    }

    public PeopleCard(UserInfo userInfo, IPeopleCard i) {
        super(userInfo, "../fxml/people_card.fxml");
        getStyleClass().add("peopleCard");
        mInterface = i;
        deleteButton.setOnMouseClicked(this);
        detailButton.setOnMouseClicked(this);
    }

    public void onHover(boolean isHover) {
        deleteButton.setVisible(isHover);
        detailButton.setVisible(isHover);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        final Object source = mouseEvent.getSource();
        if (source.equals(deleteButton)) {
            mInterface.onDeletetionRequested(PeopleCard.this);
        } else if (source.equals(detailButton)) {
            mInterface.onDetailsRequested(PeopleCard.this.getModel());
        }
    }

    public interface IPeopleCard {
        public void onDeletetionRequested(PeopleCard peopleCard);

        public void onDetailsRequested(UserInfo userInfo);
    }
}

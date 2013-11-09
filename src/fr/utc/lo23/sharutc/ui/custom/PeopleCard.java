package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class PeopleCard extends UserCard implements EventHandler<Event> {

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
        setOnMouseClicked(this);
        setOnMouseEntered(this);
        setOnMouseExited(this);
    }

    public void onHover(boolean isHover) {
        deleteButton.setVisible(isHover);
        detailButton.setVisible(isHover);
    }

    @Override
    public void handle(Event event) {
        final Object source = event.getSource();
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (source.equals(deleteButton)) {
                mInterface.onDeletetionRequested(PeopleCard.this);
            } else if (source.equals(detailButton)) {
                mInterface.onDetailsRequested(PeopleCard.this.getModel());
            } else if (source.equals(this)) {
                this.adaptStyle((MouseEvent) event);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            if (source.equals(this)) {
                this.onHover(true);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            if (source.equals(this)) {
                this.onHover(false);
            }
        }
    }

    public interface IPeopleCard {
        public void onDeletetionRequested(PeopleCard peopleCard);

        public void onDetailsRequested(UserInfo userInfo);
    }
}

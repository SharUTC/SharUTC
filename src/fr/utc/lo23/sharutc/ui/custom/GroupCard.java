package fr.utc.lo23.sharutc.ui.custom;


import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class GroupCard extends SimpleCard implements EventHandler<Event> {

    @FXML
    public Button deleteButton;

    @FXML
    public Button editButton;

    @FXML
    public Button rightsButton;


    public GroupCard() {
        super("../fxml/group_card.fxml");
        getStyleClass().add("groupCard");
        this.setOnMouseEntered(this);
        this.setOnMouseExited(this);
    }

    public void onHover(boolean isHover) {
        deleteButton.setVisible(isHover);
        editButton.setVisible(isHover);
        rightsButton.setVisible(isHover);
    }

    @Override
    public void handle(Event event) {
        final Object source = event.getSource();
        if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            if (source.equals(this)) {
                this.onHover(true);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            if (source.equals(this)) {
                this.onHover(false);
            }
        }
    }
}

package fr.utc.lo23.sharutc.ui.custom;


import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;


public class GroupCard extends SimpleCard implements EventHandler<Event> {

    @FXML
    public Button deleteButton;

    @FXML
    public Button editButton;

    @FXML
    public Button rightsButton;

    @FXML
    public Label groupName;

    @FXML
    public Label groupMembers;


    public GroupCard() {
        super("../fxml/group_card.fxml");
        getStyleClass().add("groupCard");
        this.setOnMouseEntered(this);
        this.setOnMouseExited(this);
        this.setOnDragOver(this);
        this.setOnDragEntered(this);
        this.setOnDragExited(this);
        this.setOnDragDropped(this);
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
        } else if (event.getEventType() == DragEvent.DRAG_OVER) {
            onDragOver((DragEvent) event);
        } else if (event.getEventType() == DragEvent.DRAG_ENTERED) {
            onDragEntered((DragEvent) event);
        } else if (event.getEventType() == DragEvent.DRAG_EXITED) {
            onDragExited((DragEvent) event);
        } else if (event.getEventType() == DragEvent.DRAG_DROPPED) {
            onDragDropped((DragEvent) event);
        }
    }

    /**
     * Accept PeopleCard as Droppable
     *
     * @param dragEvent
     */
    private void onDragOver(DragEvent dragEvent) {
        final Object gestureSource = dragEvent.getGestureSource();
        if (gestureSource instanceof PeopleCard) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }

    /**
     * Inform user with a message when an accepted droppable enter
     *
     * @param dragEvent
     */
    private void onDragEntered(DragEvent dragEvent) {
        final Object gestureSource = dragEvent.getGestureSource();
        if (gestureSource instanceof PeopleCard) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            groupName.setText("ADD");
            groupMembers.setText("a new contact");
        }
        dragEvent.consume();
    }

    /**
     * Delete the message when user leave the card
     *
     * @param dragEvent
     */
    private void onDragExited(DragEvent dragEvent) {
        final Object gestureSource = dragEvent.getGestureSource();
        if (gestureSource instanceof PeopleCard) {
            groupName.setText("My Friends");
            groupMembers.setText("34");
        }
        dragEvent.consume();
    }

    /**
     * retrieve content of the drag
     *
     * @param dragEvent
     */
    private void onDragDropped(DragEvent dragEvent) {
        final Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        if (db.hasString() && db.getString().equals(PeopleCard.DROP_KEY)) {
            final UserInfo userDropped = ((PeopleCard) dragEvent.getGestureSource()).getModel();
            success = true;
            System.out.println("User dropped : " + userDropped.getLogin());
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }
}

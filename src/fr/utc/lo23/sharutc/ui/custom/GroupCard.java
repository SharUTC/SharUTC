package fr.utc.lo23.sharutc.ui.custom;


import fr.utc.lo23.sharutc.model.userdata.Category;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GroupCard extends SimpleCard implements EventHandler<Event> {

    private static final Logger log = LoggerFactory.getLogger(GroupCard.class);

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

    private Category mModel;


    public GroupCard(Category category) {
        super("../fxml/group_card.fxml");
        getStyleClass().add("groupCard");
        setOnMouseEntered(this);
        setOnMouseExited(this);
        setOnDragEntered(this);
        setOnDragOver(this);
        setOnDragExited(this);
        setOnDragDropped(this);
        mModel = category;
        groupName.setText(mModel.getName());
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

    public Category getModel() {
        return mModel;
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
            groupName.setText(mModel.getName());
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
            log.info(userDropped.getLogin() + " added to " + mModel.getName());
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    /**
     * interface for GroupCard's callback
     */
    public interface IGroupCard {
        /**
         * user clicked on deleted button
         *
         * @param g
         */
        public void onGroupDeletionRequested(GroupCard g);

        /**
         * user requested details
         *
         * @param category
         */
        public void onGroupDetailsRequested(Category category);

        /**
         * user requested rights manager
         *
         * @param category
         */
        public void onGroupRightsRequested(Category category);

        /**
         * user added people
         */
        public void onUsersAdded();
    }
}

package fr.utc.lo23.sharutc.ui.custom.card;


import fr.utc.lo23.sharutc.model.userdata.Category;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
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

    @FXML
    public Region dropOverlay;

    @FXML
    public Label dropOverlayLabel;

    private Category mModel;
    private IGroupCard mInterface;


    public GroupCard(Category category, IGroupCard i) {
        super("/fr/utc/lo23/sharutc/ui/fxml/group_card.fxml");
        getStyleClass().add("groupCard");

        mInterface = i;
        mModel = category;
        groupName.setText(mModel.getName());

        setOnMouseEntered(this);
        setOnMouseExited(this);
        setOnMouseClicked(this);
        setOnDragEntered(this);
        setOnDragOver(this);
        setOnDragExited(this);
        setOnDragDropped(this);

        deleteButton.setOnMouseClicked(this);
        editButton.setOnMouseClicked(this);
        rightsButton.setOnMouseClicked(this);

        displayDropOverlay(false);
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
        } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (source.equals(deleteButton)) {
                mInterface.onGroupDeletionRequested(this);
            } else if (source.equals(editButton)) {
                mInterface.onGroupEditionRequested(mModel);
            } else if (source.equals(rightsButton)) {
                mInterface.onGroupRightsRequested(mModel);
            } else {
                mInterface.onGroupSelected(mModel);
            }
        }
    }

    public Category getModel() {
        return mModel;
    }

    /**
     * display drop overlay
     *
     * @param isShow true set drop overlay Visible, false will hide it
     */
    private void displayDropOverlay(boolean isShow) {
        dropOverlay.setVisible(isShow);
        dropOverlayLabel.setVisible(isShow);
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
        //don't consume because root uses it to relocate preview
        //dragEvent.consume();
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
            displayDropOverlay(true);
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
            displayDropOverlay(false);
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
            final PeopleCard droppedCard = (PeopleCard) dragEvent.getGestureSource();
            mInterface.onUsersAdded(mModel);
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
        public void onGroupEditionRequested(Category category);

        /**
         * user requested rights manager
         *
         * @param category
         */
        public void onGroupRightsRequested(Category category);

        /**
         * user selects this category
         *
         * @param category
         */
        public void onGroupSelected(Category category);

        /**
         * user added people
         */
        public void onUsersAdded(Category category);
    }
}

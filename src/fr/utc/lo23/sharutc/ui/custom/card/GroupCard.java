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

/**
 * A {@link SimpleCard} that is used to display a {@link Category}.
 * This class notifies a {@link IGroupCard} when the user interacts with the
 * card.
 */
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

    /**
     * Card use to display a {@link Category} to the user
     * //TODO improve the Category model, sad fix
     * Since {@link Category} doesn't provide number of contacts, use the parameter
     *
     * @param category     model of the card
     * @param i            interface used for the callback
     * @param memberNumber number of contact in this category //TODO remove after fix
     */
    public GroupCard(Category category, int memberNumber, IGroupCard i) {
        super("/fr/utc/lo23/sharutc/ui/fxml/group_card.fxml");
        getStyleClass().add("groupCard");

        mInterface = i;
        mModel = category;

        //Hideous hack to display "Friends" instead of "Public" in the interface only
        groupName.setText(mModel.getName().equals("Public") ? "Friends" : mModel.getName());
        groupMembers.setText(String.valueOf(memberNumber));

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

    /**
     * Set the visibility of the buttons.
     *
     * @param isVisible the visibility to be set.
     */
    public void setButtonVisibility(boolean isVisible) {
        deleteButton.setVisible(isVisible);
        editButton.setVisible(isVisible);
        rightsButton.setVisible(isVisible);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Event event) {
        final Object source = event.getSource();
        if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            if (source.equals(this)) {
                this.setButtonVisibility(true);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            if (source.equals(this)) {
                this.setButtonVisibility(false);
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

    /**
     * Return the model from which the data was extracted.
     *
     * @return {@link Category}
     */
    public Category getModel() {
        return mModel;
    }

    /**
     * Set model of the card
     * //TODO improve the Category model, sad fix
     * Since {@link Category} doesn't provide number of contacts, use the parameter
     *
     * @param category
     * @param membersNumber
     */
    public void setModel(Category category, int membersNumber) {
        mModel = category;
        groupMembers.setText(String.valueOf(membersNumber));

    }

    /**
     * Display drop overlay.
     *
     * @param isShow true set drop overlay Visible, false will hide it
     */
    private void displayDropOverlay(boolean isShow) {
        dropOverlay.setVisible(isShow);
        dropOverlayLabel.setVisible(isShow);
    }

    /**
     * Accept PeopleCard as Droppable.
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
     * Inform user with a message when an accepted droppable enter.
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
     * Delete the message when user leave the card.
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
     * Retrieve content of the drag.
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
     * A simple interface used by {@link GroupCard} to notify that the user has
     * just interacted with the card.
     */
    public interface IGroupCard {

        /**
         * The {@link IGroupCard} is being asked to delete a group.
         *
         * @param g the {@link GroupCard} to be deleted.
         */
        public void onGroupDeletionRequested(GroupCard g);

        /**
         * The {@link IGroupCard} is being asked to edit a {@link Category}.
         *
         * @param category the {@link Category} to be edited.
         */
        public void onGroupEditionRequested(Category category);

        /**
         * The {@link IGroupCard} is being notified that the user wants to view
         * the detailed rights of a {@link Category}
         *
         * @param category The {@link Category} to be viewed.
         */
        public void onGroupRightsRequested(Category category);

        /**
         * The {@link IGroupCard} is being notified that the user has just
         * selected a {@link Category}
         *
         * @param category The {@link Category} being selected.
         */
        public void onGroupSelected(Category category);

        /**
         * The {@link IGroupCard} is being asked to add selected users to a
         * {@link Category}
         *
         * @param category
         */
        public void onUsersAdded(Category category);
    }
}

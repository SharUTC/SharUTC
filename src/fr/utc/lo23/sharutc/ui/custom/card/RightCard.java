package fr.utc.lo23.sharutc.ui.custom.card;

import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;

/**
 * A {@link SimpleCard} that can display the name of a right. This class
 * notifies a {@link IRightCard} when a {@link SongCard} is dropped.
 */
public class RightCard extends SimpleCard implements EventHandler<Event> {

    @FXML
    public Label rightName;
    @FXML
    public Region dropOverlay;
    @FXML
    public Label dropOverlayLabel;
    private IRightCard mInterface;

    /**
     * Card displayed to represent a right
     *
     * @param rightText text displayed to the user
     * @param i         interface to get the callback
     */
    public RightCard(String rightText, IRightCard i) {
        super("/fr/utc/lo23/sharutc/ui/fxml/right_card.fxml");

        mInterface = i;

        rightName.setText(rightText);

        setOnDragEntered(this);
        setOnDragOver(this);
        setOnDragExited(this);
        setOnDragDropped(this);
        setOnMouseClicked(this);

        displayDropOverlay(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Event event) {
        final Object source = event.getSource();
        if (event.getEventType() == DragEvent.DRAG_OVER) {
            onDragOver((DragEvent) event);
        } else if (event.getEventType() == DragEvent.DRAG_ENTERED) {
            onDragEntered((DragEvent) event);
        } else if (event.getEventType() == DragEvent.DRAG_EXITED) {
            onDragExited((DragEvent) event);
        } else if (event.getEventType() == DragEvent.DRAG_DROPPED) {
            onDragDropped((DragEvent) event);
        } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            mInterface.onRightCardClicked(this, (MouseEvent) event);
        }
    }

    /**
     * Accept SongCard as Droppable
     *
     * @param dragEvent
     */
    private void onDragOver(DragEvent dragEvent) {
        final Object gestureSource = dragEvent.getGestureSource();
        if (gestureSource instanceof SongRightCard) {
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
        if (gestureSource instanceof SongRightCard) {
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
        if (gestureSource instanceof SongRightCard) {
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
        if (db.hasString() && db.getString().equals(SongRightCard.DROP_KEY)) {
            final SongRightCard songRightCard = (SongRightCard) dragEvent.getGestureSource();
            mInterface.onSongAdded(this);
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
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
     * A simple callback used by a {@link RightCard} to notify that a
     * {@link SongCard} has been dropped.
     */
    public interface IRightCard {

        /**
         * The {@link IRightCard} is being ask to add the right of the
         * {@link RightCard} to the selected pieces of {@link Music}.
         *
         * @param card the {@link RighCard} that represent the right.
         */
        public void onSongAdded(RightCard card);

        /**
         * The {@link IRightCard} is being informed that the
         * {@link RightCard} has been clicked
         *
         * @param card       the {@link RightCard} that represent the right.
         * @param mouseEvent {@link MouseEvent} mouse event
         */
        public void onRightCardClicked(RightCard card, MouseEvent mouseEvent);
    }
}

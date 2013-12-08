package fr.utc.lo23.sharutc.ui.custom.card;

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
 * A simple card that displays a tag and its weight.
 */
public class TagCard extends SimpleCard implements EventHandler<Event> {

    @FXML
    public Label tagNameLabel;
    @FXML
    public Label tagWeightLabel;
    @FXML
    public Region dropOverlayBlue;
    @FXML
    public Label dropOverlayLabel;
    private String mTagName;
    private ITagCard mCallBack;

    public TagCard(String tagName, ITagCard callBack) {
        super("/fr/utc/lo23/sharutc/ui/fxml/tag_card.fxml");
        mCallBack = callBack;
        mTagName = tagName;
        tagNameLabel.setText(mTagName);

        setOnMouseClicked(this);
        setOnDragEntered(this);
        setOnDragOver(this);
        setOnDragExited(this);
        setOnDragDropped(this);

        setDropOverlayVisibility(false);
    }

    /**
     * Set the weight of the tag.
     * 
     * @param weight the weight of the tag
     */
    public void setTagWeight(final Integer weight) {
        tagWeightLabel.setText(String.valueOf(weight));
    }

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
            mCallBack.onTagSelected(mTagName);
        }
    }

    /**
     * Set the visibility of the drop overlay.
     *
     * @param isShow true set drop overlay Visible, false will hide it
     */
    private void setDropOverlayVisibility(boolean isVisible) {
        dropOverlayBlue.setVisible(isVisible);
        dropOverlayLabel.setVisible(isVisible);
    }

    /**
     * Accept PeopleCard as Droppable
     *
     * @param dragEvent
     */
    private void onDragOver(DragEvent dragEvent) {
        final Object gestureSource = dragEvent.getGestureSource();
        if (gestureSource instanceof SongCard) {
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
        if (gestureSource instanceof SongCard) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            setDropOverlayVisibility(true);
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
        if (gestureSource instanceof SongCard) {
            setDropOverlayVisibility(false);
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
        if (db.hasString() && db.getString().equals(SongCard.DROP_KEY)) {
            mCallBack.onMusicDropOnTag(mTagName);
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    /**
     * interface for tag card callback
     */
    public interface ITagCard {

        public void onTagSelected(String tagName);

        public void onMusicDropOnTag(String tagName);
    }
}

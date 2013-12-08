package fr.utc.lo23.sharutc.ui.custom.card;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.*;

/**
 * A simple card that can be dragged. The card notifies a
 * {@link IDraggableCardListener} on drag start and on drag done.
 */
public abstract class DraggableCard extends SimpleCard {

    private String mDropKey;
    private IDraggableCardListener mInterface;
    private EventHandler<Event> mHandler;

    public DraggableCard(String resourceFXML, String dropKey, IDraggableCardListener i) {
        super(resourceFXML);
        mDropKey = dropKey;
        mInterface = i;

        mHandler = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                final Object source = event.getSource();
                if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
                    if (source.equals(DraggableCard.this)) {
                        onDragStart((MouseEvent) event);
                        mInterface.onDragStart((MouseEvent) event, DraggableCard.this);
                    }
                } else if (event.getEventType() == DragEvent.DRAG_DONE) {
                    if (source.equals(DraggableCard.this)) {
                        onDragDone((DragEvent) event);
                        mInterface.onDragStop(DraggableCard.this);
                    }
                }
            }
        };

        this.setOnDragDetected(mHandler);
        this.setOnDragDone(mHandler);
    }

    /**
     * Change the style of the card and add the model to the Dragboard when the
     * drag start.
     *
     * @param mouseEvent
     */
    private void onDragStart(MouseEvent mouseEvent) {
        this.getStyleClass().add("cardDragged");
        Dragboard db = this.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(mDropKey);
        db.setContent(content);
        mouseEvent.consume();
    }

    /**
     * Reset the default style after drag.
     *
     * @param dragEvent
     */
    private void onDragDone(DragEvent dragEvent) {
        this.getStyleClass().remove("cardDragged");
        dragEvent.consume();
    }

    /**
     * Reset the default style after drop.
     */
    public void dropped() {
        final ObservableList<String> style = getStyleClass();
        style.remove("cardDragged");
        style.remove("simpleCardClicked");
        mState = STATE_NORMAL;

    }

    /**
     * Set a dragged style.
     */
    public void dragged() {
        final ObservableList<String> style = getStyleClass();
        style.add("cardDragged");

    }

    public interface IDraggableCardListener {

        public void onDragStart(MouseEvent event, DraggableCard draggableCard);

        public void onDragStop(DraggableCard draggableCard);
    }
}

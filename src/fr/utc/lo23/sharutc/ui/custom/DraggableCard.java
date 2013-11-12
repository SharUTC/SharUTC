package fr.utc.lo23.sharutc.ui.custom;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.*;

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
     * Change style and add the model to the Dragboard
     *
     * @param mouseEvent
     */
    private void onDragStart(MouseEvent mouseEvent) {
        this.getStyleClass().add("peopleCardDrag");
        Dragboard db = this.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(mDropKey);
        db.setContent(content);
        mouseEvent.consume();
    }

    /**
     * Retrieve original style whe drag gesture is done
     *
     * @param dragEvent
     */
    private void onDragDone(DragEvent dragEvent) {

        this.getStyleClass().remove("peopleCardDrag");
        dragEvent.consume();
    }

    /**
     * inform that this card as been dropped due to multi selection Drag&Drop
     */
    public void dropped() {
        final ObservableList<String> style = getStyleClass();
        style.remove("peopleCardDrag");
        style.remove("simpleCardClicked");
        mState = STATE_NORMAL;

    }

    /**
     * inform is being dragged due to multi selection Drag&Drop
     */
    public void dragged() {
        final ObservableList<String> style = getStyleClass();
        style.add("peopleCardDrag");

    }


    public interface IDraggableCardListener {
        public void onDragStart(MouseEvent event, DraggableCard draggableCard);

        public void onDragStop(DraggableCard draggableCard);

    }
}

package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.*;

public class PeopleCard extends UserCard implements EventHandler<Event> {

    /**
     * key used for the drag event to identify the content
     */
    public static final String DROP_KEY = "PeopleCardContent";

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
        setOnDragDetected(this);
        setOnDragDone(this);
    }

    public void onHover(boolean isHover) {
        deleteButton.setVisible(isHover);
        detailButton.setVisible(isHover);
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

    @Override
    public void handle(Event event) {
        final Object source = event.getSource();
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (source.equals(deleteButton)) {
                mInterface.onPeopleDeletetionRequested(PeopleCard.this);
            } else if (source.equals(detailButton)) {
                mInterface.onPeopleDetailsRequested(PeopleCard.this.getModel());
            } else if (source.equals(this)) {
                this.adaptStyle((MouseEvent) event);
                mInterface.onPeopleCardSelected(this);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            if (source.equals(this)) {
                this.onHover(true);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            if (source.equals(this)) {
                this.onHover(false);
            }
        } else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
            if (source.equals(this)) {
                onDragStart((MouseEvent) event);
                mInterface.onCardBeingDragged(true);
            }
        } else if (event.getEventType() == DragEvent.DRAG_DONE) {
            if (source.equals(this)) {
                onDragDone((DragEvent) event);
                mInterface.onCardBeingDragged(false);
            }
        }
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
        content.putString(DROP_KEY);
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
     * interface for PeopleCard's callback
     */
    public interface IPeopleCard {
        /**
         * user requested the deletion
         *
         * @param peopleCard
         */
        public void onPeopleDeletetionRequested(PeopleCard peopleCard);

        /**
         * user requested more details
         *
         * @param userInfo
         */
        public void onPeopleDetailsRequested(UserInfo userInfo);

        /**
         * card has been selected
         *
         * @param peopleCard
         */
        public void onPeopleCardSelected(PeopleCard peopleCard);

        /**
         * inform that a PeopleCard is being dragged
         *
         * @param isDragged true if drag start, false if drag failed
         */
        public void onCardBeingDragged(boolean isDragged);
    }
}

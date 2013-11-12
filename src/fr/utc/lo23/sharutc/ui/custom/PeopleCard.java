package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class PeopleCard extends DraggableCard implements EventHandler<Event> {

    /**
     * key used for the drag event to identify the content
     */
    public static final String DROP_KEY = "PeopleCardContent";

    private IPeopleCard mInterface;
    private UserInfo mUserInfo;

    public Label userLogin;
    public Label userName;

    public Button deleteButton;
    public Button detailButton;


    public PeopleCard(UserInfo userInfo, IPeopleCard i) {
        super("../fxml/people_card.fxml", DROP_KEY, i);
        initModel(userInfo);
        getStyleClass().add("peopleCard");
        mInterface = i;
        deleteButton.setOnMouseClicked(this);
        detailButton.setOnMouseClicked(this);
        setOnMouseClicked(this);
        setOnMouseEntered(this);
        setOnMouseExited(this);
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

    public UserInfo getModel() {
        return mUserInfo;
    }

    private void initModel(UserInfo userInfo) {
        mUserInfo = userInfo;
        userLogin.setText(mUserInfo.getLogin());
        userName.setText(mUserInfo.getFirstName() + " " + mUserInfo.getLastName());
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
        }
    }

    /**
     * interface for PeopleCard's callback
     */
    public interface IPeopleCard extends IDraggableCardListener {

        /**
         * user requested more details
         *
         * @param userInfo
         */
        public void onPeopleDetailsRequested(UserInfo userInfo);

        /**
         * user requested the deletion
         *
         * @param peopleCard
         */
        public void onPeopleDeletetionRequested(PeopleCard peopleCard);


        /**
         * card has been selected
         *
         * @param peopleCard
         */
        public void onPeopleCardSelected(PeopleCard peopleCard);

    }
}

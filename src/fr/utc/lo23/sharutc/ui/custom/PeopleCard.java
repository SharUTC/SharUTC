package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class PeopleCard extends DraggableCard implements EventHandler<Event> {

    /**
     * key used for the drag event to identify the content
     */
    public static final String DROP_KEY = PeopleCard.class + "DropKey";
    public static final int USAGE_CATEGORY = 0x00000001;
    public static final int USAGE_CONNECTED = 0x00000002;

    private IPeopleCard mInterface;
    private UserInfo mUserInfo;
    private int mUsage;

    public Label userLogin;
    public Label userName;

    public Button deleteButton;
    public Button detailButton;


    public PeopleCard(UserInfo userInfo, IPeopleCard i, int usage) {
        super("/fr/utc/lo23/sharutc/ui/fxml/people_card.fxml", DROP_KEY, i);
        initModel(userInfo);
        getStyleClass().add("peopleCard");
        mInterface = i;
        detailButton.setOnMouseClicked(this);
        setOnMouseClicked(this);
        setOnMouseEntered(this);
        setOnMouseExited(this);
        mUsage = usage;
        switch (usage) {
            case USAGE_CATEGORY:
                deleteButton.setOnMouseClicked(this);
                break;
            case USAGE_CONNECTED:
                break;
        }
    }

    public void onHover(boolean isHover) {
        switch (mUsage) {
            case USAGE_CATEGORY:
                deleteButton.setVisible(isHover);
                break;
        }
        detailButton.setVisible(isHover);
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

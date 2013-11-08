package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.GroupCard;
import fr.utc.lo23.sharutc.ui.custom.PeopleCard;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class PeopleHomeController implements Initializable, EventHandler<MouseEvent>, PeopleCard.IPeopleCard {

    private static final Logger log = LoggerFactory.getLogger(PeopleHomeController.class);
    private IPeopleHomeController mInterface;

    @FXML
    public FlowPane peopleContainer;
    @FXML
    public FlowPane groupContainer;
    @FXML
    public ScrollPane groupScrollPane;

    public PeopleHomeController(IPeopleHomeController i) {
        mInterface = i;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupScrollPane.getStyleClass().add("myScrollPaneWithTopBorder");
        populate();
    }

    public void setInterface(IPeopleHomeController i) {
        mInterface = i;
    }

    //TODO Remove once we get a real list of user
    private void populate() {
        for (int i = 0; i < 50; i++) {
            final UserInfo userInfo = new UserInfo();
            userInfo.setLogin("Login " + String.valueOf(i));
            userInfo.setLastName("LastName");
            userInfo.setFirstName("FirstName");
            PeopleCard newCard = new PeopleCard(userInfo, this);
            newCard.setOnMouseClicked(this);
            newCard.setOnMouseEntered(this);
            newCard.setOnMouseExited(this);
            peopleContainer.getChildren().add(newCard);
        }

        for (int i = 0; i < 10; i++) {
            //TODO add GroupCard with color attribute
            GroupCard newCard = new GroupCard();
            groupContainer.getChildren().add(newCard);
            newCard.setOnMouseEntered(this);
            newCard.setOnMouseExited(this);
        }
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (mouseEvent.getSource() instanceof PeopleCard) {
                log.info("UserCard clicked");
                final PeopleCard user = (PeopleCard) mouseEvent.getSource();
                user.adaptStyle(mouseEvent);
            }
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED) {
            if (mouseEvent.getSource() instanceof PeopleCard) {
                final PeopleCard user = (PeopleCard) mouseEvent.getSource();
                user.onHover(true);
            }
            if (mouseEvent.getSource() instanceof GroupCard) {
                final GroupCard group = (GroupCard) mouseEvent.getSource();
                group.onHover(true);
            }
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED) {
            if (mouseEvent.getSource() instanceof PeopleCard) {
                final PeopleCard user = (PeopleCard) mouseEvent.getSource();
                user.onHover(false);
            }
            if (mouseEvent.getSource() instanceof GroupCard) {
                final GroupCard group = (GroupCard) mouseEvent.getSource();
                group.onHover(false);
            }
        }

    }

    @Override
    public void onDeletetionRequested(PeopleCard peopleCard) {
        log.info("onDeletetionRequested " + peopleCard.getModel().getLogin());
        peopleContainer.getChildren().remove(peopleCard);
    }

    @Override
    public void onDetailsRequested(UserInfo userInfo) {
        log.info("onDetailsRequested " + userInfo.getLogin());
        mInterface.onPeopleDetailRequested(userInfo);
    }

    public interface IPeopleHomeController {

        void onPeopleDetailRequested(UserInfo user);

        void onGroupDetailRequested();
    }
}

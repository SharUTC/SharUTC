package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.GroupCard;
import fr.utc.lo23.sharutc.ui.custom.PeopleCard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class PeopleHomeController implements Initializable, PeopleCard.IPeopleCard {

    private static final Logger log = LoggerFactory.getLogger(PeopleHomeController.class);
    private IPeopleHomeController mInterface;

    @FXML
    public FlowPane peopleContainer;
    @FXML
    public FlowPane groupContainer;
    @FXML
    public ScrollPane groupScrollPane;

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
            peopleContainer.getChildren().add(newCard);
        }

        for (int i = 0; i < 10; i++) {
            //TODO add GroupCard with color attribute
            final Category category = new Category();
            category.setName("Category " + i);
            GroupCard newCard = new GroupCard(category);
            groupContainer.getChildren().add(newCard);
        }
    }

    @Override
    public void onPeopleDeletetionRequested(PeopleCard peopleCard) {
        log.info("onPeopleDeletetionRequested " + peopleCard.getModel().getLogin());
        peopleContainer.getChildren().remove(peopleCard);
    }

    @Override
    public void onPeopleDetailsRequested(UserInfo userInfo) {
        log.info("onPeopleDetailsRequested " + userInfo.getLogin());
        mInterface.onPeopleDetailRequested(userInfo);
    }

    public interface IPeopleHomeController {

        /**
         * display user details
         *
         * @param user
         */
        void onPeopleDetailRequested(UserInfo user);

        /**
         * display group details
         */
        void onGroupDetailRequested();
    }
}

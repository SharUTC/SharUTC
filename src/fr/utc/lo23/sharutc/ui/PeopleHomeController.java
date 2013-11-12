package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.DraggableCard;
import fr.utc.lo23.sharutc.ui.custom.GroupCard;
import fr.utc.lo23.sharutc.ui.custom.PeopleCard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PeopleHomeController extends DragPreviewDrawer implements Initializable, PeopleCard.IPeopleCard, GroupCard.IGroupCard {

    private static final Logger log = LoggerFactory.getLogger(PeopleHomeController.class);
    private IPeopleHomeController mInterface;
    /**
     * Card selected by the user
     */
    private ArrayList<PeopleCard> mPeopleCardSelected;

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
        mPeopleCardSelected = new ArrayList<PeopleCard>();
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
            GroupCard newCard = new GroupCard(category, this);
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

    @Override
    public void onPeopleCardSelected(PeopleCard peopleCard) {
        if (mPeopleCardSelected.contains(peopleCard)) {
            mPeopleCardSelected.remove(peopleCard);
        } else {
            mPeopleCardSelected.add(peopleCard);
        }
    }

    @Override
    public void onGroupDeletionRequested(GroupCard g) {
        log.info("onGroupDeletionRequested " + g.getModel().getName());
        groupContainer.getChildren().remove(g);
    }

    @Override
    public void onGroupEditionRequested(Category category) {
        //TODO create Group Edition View
    }

    @Override
    public void onGroupRightsRequested(Category category) {
        //TODO create Rights Edition View
    }

    @Override
    public void onUsersAdded(Category category) {
        for (PeopleCard people : mPeopleCardSelected) {
            //add all selected card to the new category
            final UserInfo user = people.getModel();
            log.info(user.getLogin() + " added to " + category.getName());
            people.dropped();
        }
        //clean the selection
        mPeopleCardSelected.clear();
        hideDragPreview();
    }

    /**
     * Display PeopleCard selected as Drag preview
     *
     * @param event
     */
    protected void updateDragPreview(MouseEvent event) {
        super.updateDragPreview(event);
        int i = 0;
        for (PeopleCard people : mPeopleCardSelected) {
            final ImageView preview = new ImageView(people.snapshot(null, null));
            StackPane.setMargin(preview, new Insets(20 * i, 20 * i, 0, 0));
            mDragPreview.getChildren().add(preview);
            i++;
        }
    }

    @Override
    public void onDragStart(MouseEvent event, DraggableCard draggableCard) {
        if (draggableCard instanceof PeopleCard) {
            final PeopleCard draggedCard = (PeopleCard) draggableCard;
            //had to checked if this card is already selected because user
            //can drag a selected one or a new one
            mPeopleCardSelected.remove(draggedCard);
            mPeopleCardSelected.add(draggedCard);

            //drag event start, inform all selected card
            updateDragPreview(event);
            for (PeopleCard peopleCard : mPeopleCardSelected) {
                peopleCard.dragged();
            }
        }
    }

    @Override
    public void onDragStop(DraggableCard draggableCard) {
        if (draggableCard instanceof PeopleCard) {
            //drag event failed, inform all selected card
            for (PeopleCard peopleCard : mPeopleCardSelected) {
                peopleCard.dropped();
            }
            //clean the selection
            mPeopleCardSelected.clear();
            hideDragPreview();
        }
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

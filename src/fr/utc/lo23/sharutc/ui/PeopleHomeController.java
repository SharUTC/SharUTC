package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelImpl;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.HorizontalScrollHandler;
import fr.utc.lo23.sharutc.ui.custom.card.DraggableCard;
import fr.utc.lo23.sharutc.ui.custom.card.GroupCard;
import fr.utc.lo23.sharutc.ui.custom.card.PeopleCard;
import fr.utc.lo23.sharutc.ui.custom.card.SimpleCard;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PeopleHomeController extends DragPreviewDrawer implements Initializable, PeopleCard.IPeopleCard, GroupCard.IGroupCard, PropertyChangeListener {

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
    @Inject
    private AppModel mAppModel;

    private Category mCurrentCategory;
    private final Category mVirtualConnectedCategory = new Category();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupScrollPane.getStyleClass().add("myScrollPaneWithTopBorder");
        HorizontalScrollHandler scrollHandler = new HorizontalScrollHandler(groupScrollPane);

        mPeopleCardSelected = new ArrayList<PeopleCard>();
        mAppModel.addPropertyChangeListener(this);

        //initialize the connected category
        mVirtualConnectedCategory.setName("Connected ");
        displayUserGroup();
        displayActivePeers(mVirtualConnectedCategory);

    }

    public void setInterface(IPeopleHomeController i) {
        mInterface = i;
    }

    @Override
    public void onPeopleDeletetionRequested(PeopleCard peopleCard) {
        log.info("onPeopleDeletetionRequested " + peopleCard.getModel().getLogin());
        peopleContainer.getChildren().remove(peopleCard);
    }

    @Override
    public void onPeopleDetailsRequested(UserInfo userInfo) {
        log.info("onPeopleDetailsRequested " + userInfo.getLogin());
        mAppModel.removePropertyChangeListener(this);
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
        log.info("onGroupEditionRequested " + category.getName());
        mInterface.onGroupEditionRequested(category);
        //TODO create Group Edition View
    }

       @Override
    public void onGroupRightsRequested(Category category) {
        log.info("onGroupRightsRequested " + category.getName());
        mInterface.onGroupRightsRequested(category);
    }

    @Override
    public void onGroupSelected(Category category) {
        log.info("onGroupSelected " + category.getName());
        if (!mCurrentCategory.equals(category)) {
            mCurrentCategory = category;
            displayActivePeers(category);
        }
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

    /**
     * call at the beginning and each time propertyChange ACTIVE_PEERS
     */
    private void displayActivePeers(Category c) {
        peopleContainer.getChildren().clear();


        if (c.equals(mVirtualConnectedCategory)) {
            //display all user

            //TODO Remove once we get the real peersList
            for (int i = 0; i < 50; i++) {
                final UserInfo userInfo = new UserInfo();
                userInfo.setLogin("Login " + String.valueOf(i));
                userInfo.setLastName("LastName");
                userInfo.setFirstName("FirstName");
                PeopleCard newCard = new PeopleCard(userInfo, this, PeopleCard.USAGE_CONNECTED);
                peopleContainer.getChildren().add(newCard);
            }

        } else {


            //TODO Remove once we get the real peersList
            for (int i = 10; i < 25; i++) {
                final UserInfo userInfo = new UserInfo();
                userInfo.setLogin("Login " + String.valueOf(i));
                userInfo.setLastName("LastName");
                userInfo.setFirstName("FirstName");
                PeopleCard newCard = new PeopleCard(userInfo, this, PeopleCard.USAGE_CATEGORY);
                peopleContainer.getChildren().add(newCard);
            }

            //check if user are in mCurrentCategory
//            Contact c = new Contact();
//            if(c.getCategoryIds().contains(mCurrentCategory.getId())){
//                peopleContainer.getChildren().add()   ;
//            }
        }
    }

    /**
     * call at the beginning and each time a group change
     */
    private void displayUserGroup() {
        groupContainer.getChildren().clear();

        //Display the virtual category for find connected people
        GroupCard virtualConnectedCard = new GroupCard(mVirtualConnectedCategory, this);
        //disable deletion, edit and rights for this virtual groupCard, needs improvement
        virtualConnectedCard.setOnMouseEntered(null);
        //disable drop behavior for this virtual groupCard, needs improvement
        virtualConnectedCard.setOnDragEntered(null);
        virtualConnectedCard.setOnDragOver(null);
        groupContainer.getChildren().add(virtualConnectedCard);
        mCurrentCategory = mVirtualConnectedCategory;


        //TODO Remove once we get the groupList
        for (int i = 0; i < 10; i++) {
            //TODO add GroupCard with color attribute
            final Category category = new Category();
            category.setName("Category " + i);
            GroupCard newCard = new GroupCard(category, this);
            groupContainer.getChildren().add(newCard);
        }

        //Add the + card for create a new group
        SimpleCard createNewGroup = new SimpleCard("/fr/utc/lo23/sharutc/ui/fxml/simple_card.fxml",
                180, 108, Pos.CENTER);
        final Label plusText = new Label("+");
        plusText.getStyleClass().addAll("plusText");
        createNewGroup.getChildren().add(plusText);
        createNewGroup.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    //create a new group
                    log.info("group creation requested");
                }
            }
        });
        groupContainer.getChildren().add(createNewGroup);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String propertyName = evt.getPropertyName();
        if (AppModelImpl.Property.ACTIVE_PEERS.name().equals(propertyName)) {
            //retrieve the ACTIVE_PEERS list
        }
    }

    @Override
    public void onDetach() {}

    public interface IPeopleHomeController {

        /**
         * display user details
         *
         * @param user
         */
        void onPeopleDetailRequested(UserInfo user);
        void onGroupEditionRequested(Category category);
        /**
         * display group details
         */
        void onGroupDetailRequested();
        void onGroupRightsRequested (Category category) ;
    }
}

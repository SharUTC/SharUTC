package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.profile.*;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.HorizontalScrollHandler;
import fr.utc.lo23.sharutc.ui.custom.card.DraggableCard;
import fr.utc.lo23.sharutc.ui.custom.card.GroupCard;
import fr.utc.lo23.sharutc.ui.custom.card.PeopleCard;
import fr.utc.lo23.sharutc.ui.custom.card.SimpleCard;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import fr.utc.lo23.sharutc.util.DialogBoxBuilder;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;

public class PeopleHomeController extends DragPreviewDrawer implements Initializable, PeopleCard.IPeopleCard, GroupCard.IGroupCard, CollectionChangeListener {

    private static final Logger log = LoggerFactory.getLogger(PeopleHomeController.class);
    private IPeopleHomeController mInterface;
    /**
     * Card selected by the user
     */
    private ArrayList<PeopleCard> mPeopleCardSelected;
    @FXML
    public FlowPane peopleContainer;
    @FXML
    public HBox groupContainer;
    @FXML
    public ScrollPane groupScrollPane;
    @FXML
    public StackPane contentContainer;
    @Inject
    private AppModel mAppModel;
    @Inject
    private AddContactCommand addContactCommand;
    @Inject
    private RemoveContactFromCategoryCommand removeContactFromCategoryCommand;
    @Inject
    private DeleteContactCommand deleteContactCommand;
    @Inject
    private CreateCategoryCommand createCategoryCommand;
    @Inject
    private DeleteCategoryCommand deleteCategoryCommand;
    @Inject
    private EditCategoryNameCommand editCategoryNameCommand;
    @Inject
    private AddContactToCategoryCommand addContactToCategoryCommand;
    /**
     * Display message to the user
     */
    private Label mPlaceHolderLabel;
    /**
     * Manage Category
     */
    private Category mCurrentCategory;
    private GroupCard mVirtualConnectedGroup;
    private GroupCard mAskForDeletionCard;
    private GroupCard mAllContactCard;
    /**
     * + card for create a new category
     */
    private SimpleCard mCreateNewGroupCard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupScrollPane.getStyleClass().add("myScrollPaneWithTopBorder");
        HorizontalScrollHandler scrollHandler = new HorizontalScrollHandler(groupScrollPane);

        mPeopleCardSelected = new ArrayList<PeopleCard>();

        //listen for group change
        mAppModel.getProfile().getCategories().addPropertyChangeListener(this);

        //listent for users events
        mAppModel.getActivePeerList().addPropertyChangeListener(this);

        //initialize virtual categories
        mVirtualConnectedGroup = createVirtualGroup("Connected");

        //set current category to the virtualConnectedOne
        mCurrentCategory = mVirtualConnectedGroup.getModel();

        //display GroupCard
        displayUserGroup();

        //display UserCard
        displayActivePeers(mCurrentCategory);

    }

    public void setInterface(IPeopleHomeController i) {
        mInterface = i;
    }

    @Override
    public void onPeopleDeletetionRequested(PeopleCard peopleCard) {
        log.info("onPeopleDeletetionRequested " + peopleCard.getModel().getLogin());
        peopleContainer.getChildren().remove(peopleCard);

        //TODO improve, create card with Contact model
        final UserInfo user = peopleCard.getModel();

        if (mCurrentCategory.getId() == 0) {
            //from Public, remove contact from all category
            deleteContactCommand.setContact(mAppModel.getProfile().getContacts().findById(user.getPeerId()));
            deleteContactCommand.execute();
        } else {
            removeContactFromCategoryCommand.setContact(mAppModel.getProfile().getContacts().findById(user.getPeerId()));
            removeContactFromCategoryCommand.setCategory(mCurrentCategory);
            removeContactFromCategoryCommand.execute();
        }
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
    public void onGroupDeletionRequested(final GroupCard g) {
        log.info("onGroupDeletionRequested " + g.getModel().getName());
        mAskForDeletionCard = g;
        DialogBoxBuilder.createConfirmBox("Do you want to delete the category " + g.getModel().getName() + " ?",
                this.getClass().getResource("/fr/utc/lo23/sharutc/ui/css/modal.css").toExternalForm(),
                groupContainer.getScene().getRoot(),
                new DialogBoxBuilder.IConfirmBox() {
                    @Override
                    public void onChoiceMade(boolean answer) {
                        if (answer) {
                            deleteCategoryCommand.setCategory(g.getModel());
                            deleteCategoryCommand.execute();
                        }
                    }
                }).show();
    }

    @Override
    public void onGroupEditionRequested(final Category category) {
        log.info("onGroupEditionRequested " + category.getName());
        //Create a dialogBox to let the user edit the name
        DialogBoxBuilder.createEditBox("Category Name : ", category.getName(),
                this.getClass().getResource("/fr/utc/lo23/sharutc/ui/css/modal.css").toExternalForm(),
                groupContainer.getScene().getRoot(),
                new DialogBoxBuilder.IEditBox() {
                    @Override
                    public void onValidate(String value) {
                        //set the new name
                        editCategoryNameCommand.setCategoryId(category.getId());
                        editCategoryNameCommand.setCategoryName(value);
                        editCategoryNameCommand.execute();
                    }
                }).show();
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

            //add contact to the model
            addContactToCategoryCommand.setContact(new Contact(user));
            addContactToCategoryCommand.setCategory(category);
            addContactToCategoryCommand.execute();
        }
        //clean the selection
        hideDragPreview(mPeopleCardSelected);
        mPeopleCardSelected.clear();
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
            updateDragPreview(event, mPeopleCardSelected);
        }
    }

    @Override
    public void onDragStop(DraggableCard draggableCard) {
        if (draggableCard instanceof PeopleCard) {
            //drag event failed, inform all selected card
            hideDragPreview(mPeopleCardSelected);
            //clean the selection
            mPeopleCardSelected.clear();
        }
    }

    /**
     * Display the message in the place Holder
     */
    private void showPlaceHolder(String message) {
        mPlaceHolderLabel = new Label(message);
        mPlaceHolderLabel.getStyleClass().add("placeHolderLabel");
        mPlaceHolderLabel.setWrapText(true);
        mPlaceHolderLabel.setTextAlignment(TextAlignment.CENTER);
        contentContainer.getChildren().add(mPlaceHolderLabel);
    }

    /**
     * hide the place holder if it's displayed
     */
    private void hidePlaceHolder() {
        if (mPlaceHolderLabel != null) {
            contentContainer.getChildren().remove(mPlaceHolderLabel);
            mPlaceHolderLabel = null;
        }
    }

    /**
     * call at the beginning and each time propertyChange ACTIVE_PEERS
     */
    private void displayActivePeers(Category c) {
        peopleContainer.getChildren().clear();
        hidePlaceHolder();

        if (c.equals(mVirtualConnectedGroup.getModel())) {
            //display connected user
            final HashMap<UserInfo, Date> currentConnectedPeer = mAppModel.getActivePeerList().getActivePeers();
            if (currentConnectedPeer.size() == 0) {
                //TODO display no user connected
                showPlaceHolder("Currently, there is no connected user.");
                log.info("No user connected");

                //TODO Remove once we get the real peersList
//                for (int i = 10; i < 25; i++) {
//                    final UserInfo userInfo = new UserInfo();
//                    userInfo.setLogin("Login " + String.valueOf(i));
//                    userInfo.setLastName("LastName");
//                    userInfo.setFirstName("FirstName");
//                    userInfo.setPeerId((long) i);
//                    PeopleCard newCard = new PeopleCard(userInfo, this, PeopleCard.USAGE_CONNECTED);
//                    peopleContainer.getChildren().add(newCard);
//                }
            } else {
                for (UserInfo userInfo : currentConnectedPeer.keySet()) {
                    peopleContainer.getChildren().add(new PeopleCard(userInfo, this, PeopleCard.USAGE_CONNECTED));
                }
            }
        } else if (c.getId().equals(0)) {
            //display contact from all Categories
            ArrayList<Contact> allContact = mAppModel.getProfile().getContacts().getContacts();
            if (allContact.size() == 0) {
                showPlaceHolder("You have no contact in \"" + c.getName() + "\". Select \"Connected\" and Drag&Drop a user to a category.");
            } else {
                for (Contact contact : allContact) {
                    PeopleCard newCard = new PeopleCard(contact.getUserInfo(), this, PeopleCard.USAGE_CATEGORY);
                    peopleContainer.getChildren().add(newCard);
                }
            }

        } else {
            //check if user are in mCurrentCategory
            final ArrayList<Contact> contacts = mAppModel.getProfile().getContacts().getContacts();
            for (Contact contact : contacts) {
                //TODO Why not manage contactList in a Category ? would be far more efficient, Category only contain an id and a name...
                if (contact.getCategoryIds().contains(c.getId())) {
                    peopleContainer.getChildren().add(new PeopleCard(contact.getUserInfo(), this, PeopleCard.USAGE_CATEGORY));
                }
            }
            if (peopleContainer.getChildren().size() == 0) {
                showPlaceHolder("You have no contact in \"" + c.getName() + "\". Select \"Connected\" and Drag&Drop a user to a category.");
            }
        }
    }

    /**
     * call at the beginning and each time a group change
     */
    private void displayUserGroup() {
        groupContainer.getChildren().clear();

        final ArrayList<Category> categories = mAppModel.getProfile().getCategories().getCategories();

        //Display the virtual category for find connected people
        addSimpleCardIngroupContainer(mVirtualConnectedGroup);

        //Add the + card for create a new group
        displayAddNewGroupCard();

        //Display Existing Category
        for (Category c : categories) {
            if (c.getId().equals(0)) {
                mAllContactCard = addNewGroupCard(c, mAppModel.getProfile().getContacts().size(), false);
            } else {
                addNewGroupCard(c, getMembersNumbers(c), true);
            }
        }
    }

    /**
     * Retrieve the number of contact for a given category
     * //TODO remove when Category model will offer a such feature
     * Hot fix as it's impossible to know the number of contact in each category...
     *
     * @param category
     * @return
     */
    private int getMembersNumbers(Category category) {
        //init result
        int result = 0;
        ArrayList<Contact> allContact = mAppModel.getProfile().getContacts().getContacts();

        //For all contact
        for (Contact contact : allContact) {
            final Set<Integer> contactCategoryIds = contact.getCategoryIds();

            //For all category ids of the contact
            for (Integer contactCategoryId : contactCategoryIds) {

                //increase number if ids match
                if (category.getId().intValue() == contactCategoryId.intValue()) {
                    result++;
                }
            }
        }

        return result;
    }

    /**
     * add a GroupCard to the GroupCard list
     * //TODO improve the Category model, sad fix
     * Since {@link Category} doesn't provide number of contacts, use the parameter
     *
     * @param category      the model og this card
     * @param editable      true is the card can be deleted and edited
     * @param membersNumber number of contact in this category //TODO remove after fix
     */
    private GroupCard addNewGroupCard(Category category, int membersNumber, boolean editable) {
        hideAddNewGroupCard();
        final GroupCard newGroupCard = new GroupCard(category, membersNumber, PeopleHomeController.this);
        //need some improvement, remove mouse enter behaviour which display buttons for edition
        if (!editable) {
            newGroupCard.setOnMouseEntered(null);
        }
        addSimpleCardIngroupContainer(newGroupCard);
        displayAddNewGroupCard();

        return newGroupCard;

    }

    /**
     * show the + card used to create a new Category
     */
    private void displayAddNewGroupCard() {
        mCreateNewGroupCard = new SimpleCard("/fr/utc/lo23/sharutc/ui/fxml/simple_card.fxml",
                180, 108, Pos.CENTER);
        final Label plusText = new Label("+");
        plusText.getStyleClass().addAll("plusText");
        mCreateNewGroupCard.setMinWidth(180);
        mCreateNewGroupCard.setMinHeight(100);
        mCreateNewGroupCard.setMaxHeight(100);
        mCreateNewGroupCard.getChildren().add(plusText);
        mCreateNewGroupCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    //create a new group
                    final int id = mAppModel.getProfile().getCategories().size();
                    log.info("group creation requested " + id);
                    //Create a dialogBox to let the user choose the name
                    DialogBoxBuilder.createEditBox("Category Name : ", "Category" + id,
                            this.getClass().getResource("/fr/utc/lo23/sharutc/ui/css/modal.css").toExternalForm(),
                            groupContainer.getScene().getRoot(),
                            new DialogBoxBuilder.IEditBox() {
                                @Override
                                public void onValidate(String value) {
                                    //create the category with the entered name
                                    createCategoryCommand.setCategoryName(value);
                                    createCategoryCommand.execute();
                                }
                            }).show();
                }
            }
        });
        addSimpleCardIngroupContainer(mCreateNewGroupCard);
    }

    private void addSimpleCardIngroupContainer(SimpleCard simpleCard) {
        HBox.setMargin(simpleCard, new Insets(0, 5, 0, 5));
        groupContainer.getChildren().add(simpleCard);
    }

    /**
     * hide the + card used to create a new Category
     */
    private void hideAddNewGroupCard() {
        if (mCreateNewGroupCard != null) {
            groupContainer.getChildren().remove(mCreateNewGroupCard);
        }
    }

    /**
     * Create virtual category of users
     *
     * @param groupName category name which will be displayed
     * @return a GroupCard corresponding to the virtual category
     */
    private GroupCard createVirtualGroup(String groupName) {
        Category virtualCategory = new Category();
        virtualCategory.setName(groupName);
        GroupCard virtualGroup = new GroupCard(virtualCategory, mAppModel.getActivePeerList().size(), this);
        //disable deletion, edit and rights for this virtual groupCard, needs improvement
        virtualGroup.setOnMouseEntered(null);
        //disable drop behavior for this virtual groupCard, needs improvement
        virtualGroup.setOnDragEntered(null);
        virtualGroup.setOnDragOver(null);
        return virtualGroup;
    }

    @Override
    public void onDetach() {
        mAppModel.getProfile().getCategories().removePropertyChangeListener(this);
        mAppModel.getActivePeerList().removePropertyChangeListener(this);
    }

    @Override
    public void collectionChanged(CollectionEvent ev) {
        final CollectionEvent.Type type = ev.getType();
        final Object item = ev.getItem();
        log.info(type.name());
        if (type.equals(CollectionEvent.Type.ADD)) {
            //ADD EVENT
            if (item instanceof Category) {
                //new category added callback
                final Category c = (Category) ev.getItem();
                addNewGroupCard(c, 0, true);
            } else if (item instanceof UserInfo) {
                //new user connected
                if (mCurrentCategory.equals(mVirtualConnectedGroup.getModel())) {
                    //refresh only if current category is connected
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            final UserInfo newConnectedUser = (UserInfo) item;
                            PeopleCard newCard = new PeopleCard(newConnectedUser, PeopleHomeController.this, PeopleCard.USAGE_CONNECTED);
                            peopleContainer.getChildren().add(newCard);
                            displayUserGroup();
                        }
                    });
                }
            }
        } else if (type.equals(CollectionEvent.Type.REMOVE)) {
            //REMOVE EVENT
            if (item instanceof Category) {
                //removed category callback
                groupContainer.getChildren().remove(mAskForDeletionCard);
            } else if (item instanceof UserInfo) {
                //user disconnected callback
                log.info("one user disconnected");
                final ObservableList<Node> children = peopleContainer.getChildren();
                for (Node n : children) {
                    if (n instanceof PeopleCard) {
                        final PeopleCard userCard = (PeopleCard) n;
                        //TODO equals between two UserInfo doesn't work here, use only peerId
                        if (((PeopleCard) n).getModel().getPeerId().equals(((UserInfo) item).getPeerId())) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    children.remove(userCard);
                                    displayUserGroup();
                                }
                            });
                        }
                    }
                }
            }
        } else if (type.equals(CollectionEvent.Type.UPDATE)) {
            if (item instanceof Category) {
                final Category c = (Category) item;
                //update only the concerning category
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for (Node n : groupContainer.getChildren()) {
                            if (n instanceof GroupCard && ((GroupCard) n).getModel().equals(c)) {
                                ((GroupCard) n).setModel(c, getMembersNumbers(c));
                            }
                        }
                        mAllContactCard.setMembersNumbers(mAppModel.getProfile().getContacts().size());
                    }
                });
            }
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
         * display right edition view for the selected category
         *
         * @param category
         */
        void onGroupRightsRequested(Category category);
    }
}

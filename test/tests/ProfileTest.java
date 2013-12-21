package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.account.AccountCreationCommand;
import fr.utc.lo23.sharutc.controler.command.account.ConnectionRequestCommand;
import fr.utc.lo23.sharutc.controler.command.account.DisconnectionCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateDisconnectionCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateUserInfoCommand;
import fr.utc.lo23.sharutc.controler.command.music.AddToLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.AddToLocalCatalogCommandImpl;
import fr.utc.lo23.sharutc.controler.command.profile.AddContactCommand;
import fr.utc.lo23.sharutc.controler.command.profile.AddContactToCategoryCommand;
import fr.utc.lo23.sharutc.controler.command.profile.CreateCategoryCommand;
import fr.utc.lo23.sharutc.controler.command.profile.DeleteCategoryCommand;
import fr.utc.lo23.sharutc.controler.command.profile.DeleteContactCommand;
import fr.utc.lo23.sharutc.controler.command.profile.ManageRightsCommand;
import fr.utc.lo23.sharutc.controler.command.profile.RemoveContactFromCategoryCommand;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Rights;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.io.File;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({ProfileTestModule.class})
public class ProfileTest {

    private static String TEST_MP3_FOLDER;
    private static final String[] TEST_MP3_FILENAMES = {"Sting & The Police - The Very Best Of Sting & The Police - 17 - Roxanne.mp3", "14 - End Credit Score.mp3", "Air - Moon Safari - Sexy Boy.mp3"};
    private static final Logger log = LoggerFactory
            .getLogger(ProfileTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private UserService userService;
    @Inject
    private MusicService musicService;
    @Inject
    private FileService fileService;
    @Inject
    private NetworkService networkService;
    @Inject
    private AccountCreationCommand accountCreationCommand;
    @Inject
    private ConnectionRequestCommand connectionRequestCommand;
    @Inject
    private DisconnectionCommand disconnectionCommand;
    @Inject
    private IntegrateUserInfoCommand integrateUserInfoCommand;
    @Inject
    private IntegrateDisconnectionCommand integrateDisconnectionCommand;
    @Inject
    private AddContactCommand addContactCommand;
    @Inject
    private CreateCategoryCommand createCategoryCommand;
    @Inject
    private AddContactToCategoryCommand addContactToCategoryCommand;
    @Inject
    private RemoveContactFromCategoryCommand removeContactFromCategoryCommand;
    @Inject
    private DeleteCategoryCommand deleteCategoryCommand;
    @Inject
    private DeleteContactCommand deleteContactCommand;
    @Inject
    private ManageRightsCommand manageRightsCommand;
    @Inject
    private AddToLocalCatalogCommand addToLocalCatalogCommand;
    private AppModelBuilder appModelBuilder = null;

    /**
     *
     */
    @Before
    public void before() {
        log.trace("building appModel");
        if (appModelBuilder == null) {
            appModelBuilder = new AppModelBuilder(appModel, musicService, userService, fileService, networkService);
        }
        //appModelBuilder.mockAppModel();
    }

    @After
    public void after() {
        log.trace("cleaning appModel");
        //appModelBuilder.clearAppModel();
        appModelBuilder.deleteFolders();
    }

    @Test
    public void global() {
        try {
            TEST_MP3_FOLDER = new File(".").getCanonicalPath() + File.separator + "test" + File.separator + "mp3" + File.separator;
        } catch (Exception ex) {
            log.error(ex.toString());
        }

        Assert.assertNull(appModel.getProfile()); // Check if profile is already empty

        ///////////////////
        // Account creation
        ///////////////////
        UserInfo info = new UserInfo();
        info.setAge(25);
        info.setFirstName("firstname");
        info.setLastName("lastname");
        info.setLogin("testlogin");
        info.setPassword("pwd");
        accountCreationCommand.setUserInfo(info);
        accountCreationCommand.execute();

        disconnectionCommand.execute();

        ///////////////////
        // Connection to an unexisting account
        ///////////////////
        connectionRequestCommand.setLogin("oijzifjeorjfe");
        connectionRequestCommand.setPassword("pwd");
        connectionRequestCommand.execute();
        Assert.assertNull(appModel.getProfile());

        ///////////////////
        // Connection to an existing account
        ///////////////////
        connectionRequestCommand.setLogin("testlogin");
        connectionRequestCommand.setPassword("pwd");
        connectionRequestCommand.execute();
        Assert.assertNotNull(appModel.getProfile());

        Assert.assertTrue("accountCreationCommand failed", appModel.getProfile().getUserInfo().equals(info));

        ///////////////////
        // Connected peers test
        ///////////////////
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setLogin("LocalPeer Mock (id=4)");
        userInfo1.setPeerId(4L);
        integrateUserInfoCommand.setUserInfo(userInfo1);
        integrateUserInfoCommand.execute();

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setLogin("LocalPeer Mock (id=5)");
        userInfo2.setPeerId(5L);
        integrateUserInfoCommand.setUserInfo(userInfo2);
        integrateUserInfoCommand.execute();

        Assert.assertEquals("2 peers added to the list: failed.", 2, appModel.getActivePeerList().getActivePeers().size());

        ///////////////////
        // Disconnected peers test
        ///////////////////
        Peer removedPeer1 = new Peer(4L, "Peer Mock (id=4)");
        integrateDisconnectionCommand.setPeerId(removedPeer1.getId());
        integrateDisconnectionCommand.execute();

        // Removing Peer with ID = 3
        Peer removedPeer2 = new Peer(5L, "Peer Mock (id=5)");
        integrateDisconnectionCommand.setPeerId(removedPeer2.getId());
        integrateDisconnectionCommand.execute();

        Assert.assertEquals("2 Peers removed form the connected list: failed.", 0, appModel.getActivePeerList().getActivePeers().size());

        ///////////////////
        // Add contact test
        ///////////////////
        UserInfo uiContact = new UserInfo();
        uiContact.setAge(20);
        uiContact.setFirstName("contact1FN");
        uiContact.setLastName("contact1LN");
        uiContact.setLogin("contact1L");
        uiContact.setPassword("contact1pwd");
        uiContact.setPeerId(1L);

        Contact cCree = new Contact(uiContact);

        Assert.assertEquals("addContactCommand failed", 0, appModel.getProfile().getContacts().getContacts().size());
        addContactCommand.setContact(cCree);
        addContactCommand.execute();
        Assert.assertEquals("addContactCommand failed", 1, appModel.getProfile().getContacts().getContacts().size());
        addContactCommand.execute();
        Assert.assertEquals("addContactCommand failed", 1, appModel.getProfile().getContacts().getContacts().size());

        Assert.assertTrue("addContactCommand failed", appModel.getProfile().getContacts().findById(cCree.getUserInfo().getPeerId()).getUserInfo().equals(uiContact));

        Assert.assertTrue("addContactCommand failed", appModel.getProfile().getContacts().findById(cCree.getUserInfo().getPeerId()).isInPublic());

        ///////////////////
        // Add category test
        ///////////////////
        Assert.assertEquals(1, appModel.getProfile().getCategories().size());

        createCategoryCommand.setCategoryName("testGlobalCat");
        createCategoryCommand.execute();
        Assert.assertEquals(2, appModel.getProfile().getCategories().size());

        boolean testCat = false;
        for (Category cat : appModel.getProfile().getCategories().getCategories()) {
            if ("testGlobalCat".equals(cat.getName())) {
                testCat = true;
                break;
            }
        }
        Assert.assertTrue("createCategoryCommand failed", testCat);

        createCategoryCommand.execute();
        Assert.assertEquals(2, appModel.getProfile().getCategories().size());

        createCategoryCommand.setCategoryName("testGlobalCat2");
        createCategoryCommand.execute();
        Assert.assertEquals(3, appModel.getProfile().getCategories().size());

        testCat = false;
        for (Category cat : appModel.getProfile().getCategories().getCategories()) {
            if ("testGlobalCat2".equals(cat.getName())) {
                testCat = true;
                break;
            }
        }
        Assert.assertTrue("createCategoryCommand failed", testCat);

        ///////////////////
        // Add contact to category test
        ///////////////////
        UserInfo uiContactCat = new UserInfo();
        uiContactCat.setAge(21);
        uiContactCat.setFirstName("contactCat1FN");
        uiContactCat.setLastName("contactCat1LN");
        uiContactCat.setLogin("contactCat1L");
        uiContactCat.setPassword("contactCat1pwd");
        uiContactCat.setPeerId(17L);

        Contact contactCat = new Contact(uiContactCat);
        Category catCree = new Category(22, "testGlobalCat22");

        // To check that the contact is added to the right category
        appModel.getProfile().getCategories().add(catCree);
        addContactToCategoryCommand.setContact(contactCat);
        addContactToCategoryCommand.setCategory(catCree);
        addContactToCategoryCommand.execute();

        testCat = false;
        for (Contact c : appModel.getProfile().getContacts().getContacts()) {
            if (c.getUserInfo().getPeerId().equals(contactCat.getUserInfo().getPeerId())) {
                testCat = true;
                break;
            }
        }
        Assert.assertTrue("addContactToCategoryCommand failed", testCat);

        /* Check that the contact is only contained in one category (and not in category 0 for instance)
         * when we created it for the first time
         */
        Assert.assertEquals("addContactToCategoryCommand failed : the contact is not only added in one category",
                1, contactCat.getCategoryIds().size());

        // To check that the category public is deleted if the contact was in the category public at the beginning
        Integer idCat = null;
        for (Category cat : appModel.getProfile().getCategories().getCategories()) {
            System.out.println(cat.getId());
            if ("testGlobalCat22".equals(cat.getName())) {
                idCat = cat.getId();
                break;
            }
        }

        Assert.assertEquals("addContactToCategoryCommand failed",
                idCat, contactCat.getCategoryIds().iterator().next());

        // To check if the contact is added to the right category even if it is in other categories
        UserInfo uiContactCat2 = new UserInfo();
        uiContactCat2.setAge(21);
        uiContactCat2.setFirstName("contactCat2FN");
        uiContactCat2.setLastName("contactCat2LN");
        uiContactCat2.setLogin("contactCat2L");
        uiContactCat2.setPassword("contactCat2pwd");
        uiContactCat2.setPeerId(18L);
        Contact contactCat2 = new Contact(uiContactCat2);

        addContactCommand.setContact(contactCat2);
        addContactCommand.execute();

        addContactToCategoryCommand.setContact(contactCat2);
        addContactToCategoryCommand.setCategory(catCree);
        addContactToCategoryCommand.execute();

        Assert.assertFalse("addContactToCategoryCommand failed : the category public is not deleted",
                contactCat2.getCategoryIds().contains(Category.PUBLIC_CATEGORY_ID));

        UserInfo uiContactCat3 = new UserInfo();
        uiContactCat3.setAge(21);
        uiContactCat3.setFirstName("contactCat3FN");
        uiContactCat3.setLastName("contactCat3LN");
        uiContactCat3.setLogin("contactCat3L");
        uiContactCat3.setPassword("contactCat3pwd");
        uiContactCat3.setPeerId(19L);
        Contact contactCat3 = new Contact(uiContactCat3);

        Category catCree2 = new Category(23, "testGlobalCat23");
        appModel.getProfile().getCategories().add(catCree2);

        addContactToCategoryCommand.setContact(contactCat3);
        addContactToCategoryCommand.setCategory(catCree);
        addContactToCategoryCommand.execute();

        addContactToCategoryCommand.setCategory(catCree2);
        addContactToCategoryCommand.execute();

        Assert.assertEquals("addContactToCategoryCommand failed : the contact is not added in a second category",
                2, contactCat3.getCategoryIds().size());

        Assert.assertTrue("addContactToCategoryCommand failed : the contact is not added to the right category",
                contactCat3.getCategoryIds().contains(23));

        Assert.assertTrue("addContactToCategoryCommand failed : the contact is not added to the right category",
                contactCat3.getCategoryIds().contains(22));

        ///////////////////
        // Remove contact from category test
        ///////////////////

        /* Check if the contact is removed from the category amis but it is still in the category famille
         the contact muss not be in the category Public */
        Assert.assertTrue("addContactToCategoryCommand failed", contactCat3.getCategoryIds().contains(22));

        removeContactFromCategoryCommand.setContact(contactCat3);
        removeContactFromCategoryCommand.setCategory(catCree);
        removeContactFromCategoryCommand.execute();

        Assert.assertFalse("removeContactFromCategoryCommand failed", contactCat3.getCategoryIds().contains(22));
        Assert.assertFalse("removeContactFromCategoryCommand failed", contactCat3.getCategoryIds().contains(0));
        Assert.assertTrue("removeContactFromCategoryCommand failed", contactCat3.getCategoryIds().contains(23));
        Assert.assertEquals("removeContactFromCategoryCommand failed", contactCat3.getCategoryIds().size(), 1);

        // Check if the contact is removed from the category amis and added to the category Public
        UserInfo uiContactCat4 = new UserInfo();
        uiContactCat4.setAge(21);
        uiContactCat4.setFirstName("contactCat4FN");
        uiContactCat4.setLastName("contactCat4LN");
        uiContactCat4.setLogin("contactCat4L");
        uiContactCat4.setPassword("contactCat4pwd");
        uiContactCat4.setPeerId(20L);
        Contact contactCat4 = new Contact(uiContactCat4);

        addContactToCategoryCommand.setContact(contactCat4);
        addContactToCategoryCommand.setCategory(catCree);
        addContactToCategoryCommand.execute();

        Assert.assertFalse("removeContactFromCategoryCommand failed", contactCat4.getCategoryIds().contains(0));

        Assert.assertTrue("removeContactFromCategoryCommand failed", contactCat4.getCategoryIds().contains(22));

        removeContactFromCategoryCommand.setContact(contactCat4);
        removeContactFromCategoryCommand.setCategory(catCree);
        removeContactFromCategoryCommand.execute();

        Assert.assertFalse("removeContactFromCategoryCommand failed", contactCat4.getCategoryIds().contains(22));
        Assert.assertTrue("removeContactFromCategoryCommand failed", contactCat4.getCategoryIds().contains(0));
        Assert.assertEquals("removeContactFromCategoryCommand failed", contactCat4.getCategoryIds().size(), 1);

        // Check that a contact can't be removed from the category Public
        UserInfo uiContactCat5 = new UserInfo();
        uiContactCat5.setAge(21);
        uiContactCat5.setFirstName("contactCat5FN");
        uiContactCat5.setLastName("contactCat5LN");
        uiContactCat5.setLogin("contactCat5L");
        uiContactCat5.setPassword("contactCat5pwd");
        uiContactCat5.setPeerId(21L);
        Contact contactCat5 = new Contact(uiContactCat5);

        addContactCommand.setContact(contactCat5);
        addContactCommand.execute();

        Category catCree3 = new Category(Category.PUBLIC_CATEGORY_ID, Category.PUBLIC_CATEGORY_NAME);

        removeContactFromCategoryCommand.setContact(contactCat5);
        removeContactFromCategoryCommand.setCategory(catCree3);
        removeContactFromCategoryCommand.execute();

        Assert.assertTrue("removeContactFromCategoryCommand failed : the contact is removed from the category Public",
                contactCat5.getCategoryIds().contains(0));

        Assert.assertEquals("removeContactFromCategoryCommand failed : number of categoris for the contacts is wrong",
                contactCat5.getCategoryIds().size(), 1);

        ///////////////////
        // Delete contact test
        ///////////////////

        UserInfo uiContactCat8 = new UserInfo();
        uiContactCat8.setAge(21);
        uiContactCat8.setFirstName("contactCat8FN");
        uiContactCat8.setLastName("contactCat8LN");
        uiContactCat8.setLogin("contactCat8L");
        uiContactCat8.setPassword("contactCat8pwd");
        uiContactCat8.setPeerId(24L);
        Contact contactCat8 = new Contact(uiContactCat8);

        addContactCommand.setContact(contactCat8);
        addContactCommand.execute();

        Assert.assertTrue("addContactCommand failed", appModel.getProfile().getContacts().contains(contactCat8));

        addContactToCategoryCommand.setContact(contactCat8);
        addContactToCategoryCommand.setCategory(catCree);
        addContactToCategoryCommand.execute();

        addContactToCategoryCommand.setCategory(catCree2);
        addContactToCategoryCommand.execute();

        Assert.assertTrue("deleteContactCommand failed", appModel.getProfile().getContacts().contains(contactCat8));

        deleteContactCommand.setContact(contactCat8);
        deleteContactCommand.execute();

        Assert.assertFalse("deleteContactCommand failed", appModel.getProfile().getContacts().contains(contactCat8));

        ///////////////////
        // Delete category test
        ///////////////////

        UserInfo uiContactCat6 = new UserInfo();
        uiContactCat6.setAge(21);
        uiContactCat6.setFirstName("contactCat6FN");
        uiContactCat6.setLastName("contactCat6LN");
        uiContactCat6.setLogin("contactCat6L");
        uiContactCat6.setPassword("contactCat6pwd");
        uiContactCat6.setPeerId(22L);
        Contact contactCat6 = new Contact(uiContactCat6);

        Category catCree4 = new Category(24, "testGlobalCat24");

        appModel.getProfile().getCategories().add(catCree4);

        addContactToCategoryCommand.setContact(contactCat6);
        addContactToCategoryCommand.setCategory(catCree4);
        addContactToCategoryCommand.execute();

        int bSize = appModel.getProfile().getCategories().getCategories().size();
        deleteCategoryCommand.setCategory(catCree4);
        deleteCategoryCommand.execute();
        // Check if the category is deleted of categories list
        Assert.assertNotEquals("deleteCategoryCommand failed", appModel.getProfile().getCategories().getCategories().size(), bSize);

        // Check if the contact is not in the category anymore but is in the category Public
        Assert.assertFalse("deleteCategoryCommand failed : the first contact is still in the category",
                contactCat6.getCategoryIds().contains(24));

        Assert.assertTrue("deleteCategoryCommand failed : the first contact is not in the category Public",
                contactCat6.getCategoryIds().contains(0));

        UserInfo uiContactCat7 = new UserInfo();
        uiContactCat7.setAge(21);
        uiContactCat7.setFirstName("contactCat7FN");
        uiContactCat7.setLastName("contactCat7LN");
        uiContactCat7.setLogin("contactCat7L");
        uiContactCat7.setPassword("contactCat7pwd");
        uiContactCat7.setPeerId(23L);
        Contact contactCat7 = new Contact(uiContactCat7);

        Category catCree5 = new Category(25, "testGlobalCat25");

        appModel.getProfile().getCategories().add(catCree4);
        appModel.getProfile().getCategories().add(catCree5);

        addContactToCategoryCommand.setContact(contactCat7);
        addContactToCategoryCommand.setCategory(catCree4);
        addContactToCategoryCommand.execute();

        addContactToCategoryCommand.setCategory(catCree5);
        addContactToCategoryCommand.execute();

        deleteCategoryCommand.setCategory(catCree4);
        deleteCategoryCommand.execute();

        Assert.assertFalse("deleteCategoryCommand failed", contactCat7.getCategoryIds().contains(24));
        Assert.assertFalse("deleteCategoryCommand failed", contactCat7.getCategoryIds().contains(0));
        Assert.assertTrue("deleteCategoryCommand failed", contactCat7.getCategoryIds().contains(25));

        // Check that this command does not delete the category Public

        Assert.assertTrue("deleteCategoryCommand failed : the test can't start",
                appModel.getProfile().getCategories().contains(catCree3));

        int idsCount = appModel.getProfile().getCategories().size();

        deleteCategoryCommand.setCategory(catCree3);
        deleteCategoryCommand.execute();

        Assert.assertTrue("deleteCategoryCommand failed : the category Public is deleted (1)",
                appModel.getProfile().getCategories().contains(catCree3));
        Assert.assertEquals("deleteCategoryCommand failed : the category Public is deleted (2)",
                appModel.getProfile().getCategories().size(), idsCount);

        ///////////////////
        // Manage rights test
        ///////////////////
        appModel.setLocalCatalog(new Catalog());

        ArrayList<File> files = new ArrayList<File>();
        for (String mp3File : TEST_MP3_FILENAMES) {
            files.add(new File(TEST_MP3_FOLDER + mp3File));
        }
        addToLocalCatalogCommand = new AddToLocalCatalogCommandImpl(musicService);
        addToLocalCatalogCommand.setFiles(files);
        addToLocalCatalogCommand.execute();


        Rights rights = appModel.getRightsList().getByMusicIdAndCategoryId(1L, Category.PUBLIC_CATEGORY_ID);
        Assert.assertTrue(rights.getMayReadInfo());
        Assert.assertTrue(rights.getMayListen());
        Assert.assertTrue(rights.getMayNoteAndComment());
        Category cat = appModel.getProfile().getCategories().findCategoryById(Category.PUBLIC_CATEGORY_ID);

        manageRightsCommand.setCategory(cat);
        manageRightsCommand.setMusic(appModel.getLocalCatalog().findMusicById(1l));
        manageRightsCommand.setMayReadInfo(false);
        manageRightsCommand.setMayListen(false);
        manageRightsCommand.setMayCommentAndScore(true);
        manageRightsCommand.execute();

        Rights rights2 = appModel.getRightsList().getByMusicIdAndCategoryId(1L, Category.PUBLIC_CATEGORY_ID);
        Assert.assertFalse(rights2.getMayReadInfo());
        Assert.assertFalse(rights2.getMayListen());
        Assert.assertTrue(rights2.getMayNoteAndComment());
    }
}

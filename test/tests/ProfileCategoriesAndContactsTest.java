/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.profile.AddContactCommand;
import fr.utc.lo23.sharutc.controler.command.profile.AddContactToCategoryCommand;
import fr.utc.lo23.sharutc.controler.command.profile.CreateCategoryCommand;
import fr.utc.lo23.sharutc.controler.command.profile.DeleteCategoryCommand;
import fr.utc.lo23.sharutc.controler.command.profile.DeleteContactCommand;
import fr.utc.lo23.sharutc.controler.command.profile.RemoveContactFromCategoryCommand;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Olivier
 */
@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({ProfileCategoriesAndContactsTestModule.class})
public class ProfileCategoriesAndContactsTest {

    private static final Logger log = LoggerFactory
            .getLogger(ProfileCategoriesAndContactsTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private UserService userService;
    @Inject
    private MusicService musicService;
    @Inject
    private AddContactCommand addContactCommand;
    @Inject
    private AddContactToCategoryCommand addContactToCategoryCommand;
    @Inject
    private CreateCategoryCommand createCategoryCommand;
    @Inject
    private DeleteCategoryCommand deleteCategoryCommand;
    @Inject
    private DeleteContactCommand deleteContactCommand;
    @Inject
    private RemoveContactFromCategoryCommand removeContactFromCategoryCommand;
    private AppModelBuilder appModelBuilder = null;

    @Before
    public void before() {
        log.trace("building appModel");
        if (appModelBuilder == null) {
            appModelBuilder = new AppModelBuilder(appModel, musicService, userService);
        }
        appModelBuilder.mockAppModel();
    }

    @After
    public void after() {
        log.trace("cleaning appModel");
        appModelBuilder.clearAppModel();
    }

    @Test
    public void addContactCommand() {

        UserInfo u = new UserInfo();
        u.setAge(22);
        u.setFirstName("Mathilde");
        u.setLastName("ALL");
        u.setLogin("mathilde");
        u.setPassword("ccc");
        u.setPeerId(1L);

        Contact cCree = new Contact(u);

        addContactCommand.setContact(cCree);
        addContactCommand.execute();
        // to check if a contact is added only once
        addContactCommand.execute();

        Contact cTest = new Contact(u);
        // the command automatically add the default category to the contact
        cTest.addCategoryId(0);

        Contact resultat = null;
        for (Contact c : appModel.getProfile().getContacts().getContacts()) {
            if (c.getUserInfo().getPeerId().equals(cCree.getUserInfo().getPeerId())) {
                resultat = c;
                break;
            }
        }

        Assert.assertEquals("addContactCommand failed", cTest, resultat);

        Assert.assertNotEquals("addContactCommand failed : Contact was added twice", 2,
                appModel.getProfile().getContacts().getContacts().size());
    }

    @Test
    public void addContactToCategoryCommand() {

        // A - to check that the contact is added to the right category
        UserInfo u = new UserInfo();
        u.setAge(22);
        u.setFirstName("Mathilde");
        u.setLastName("ALL");
        u.setLogin("mathilde");
        u.setPassword("ccc");
        u.setPeerId(1L);

        Contact cCree = new Contact(u);
        Category catCree = new Category(1, "amis");
        addContactToCategoryCommand.setContact(cCree);
        addContactToCategoryCommand.setCategory(catCree);
        addContactToCategoryCommand.execute();

        Contact cTest = new Contact(u);
        cTest.addCategoryId(1);

        Contact resultat = null;
        for (Contact c : appModel.getProfile().getContacts().getContacts()) {
            if (c.getUserInfo().getPeerId().equals(cCree.getUserInfo().getPeerId())) {
                resultat = c;
                break;
            }
        }
        Assert.assertEquals("addContactToCategoryCommand failed : the contact is not added",
                cTest, resultat);


        /* B - check that the contact is only contained in one category (and not in category 0 for instance)
         * when we created it for the first time
         */
        Assert.assertEquals("addContactToCategoryCommand failed : the contact is not only added in one category",
                1, cCree.getCategoryIds().size());


        // C - to check that the category public is deleted if the contact was in the category public at the beginning
        UserInfo u2 = new UserInfo();
        u2.setAge(22);
        u2.setFirstName("Mathilde");
        u2.setLastName("ALL");
        u2.setLogin("mathilde");
        u2.setPassword("ccc");
        u2.setPeerId(2L);
        Contact cCree2 = new Contact(u2);
        addContactCommand.setContact(cCree2);
        addContactCommand.execute();
        addContactToCategoryCommand.setContact(cCree2);
        addContactToCategoryCommand.setCategory(catCree);
        addContactToCategoryCommand.execute();

        Assert.assertFalse("addContactToCategoryCommand failed : the category public is not deleted",
                cCree2.getCategoryIds().contains(0));

        // D - to check if the contact is added to the right category even if it is in other categories
        UserInfo u3 = new UserInfo();
        u3.setAge(22);
        u3.setFirstName("Mathilde");
        u3.setLastName("ALL");
        u3.setLogin("mathilde");
        u3.setPassword("ccc");
        u3.setPeerId(3L);
        Contact cCree3 = new Contact(u3);
        Category catCree2 = new Category(2, "famille");
        addContactToCategoryCommand.setContact(cCree3);
        addContactToCategoryCommand.setCategory(catCree);
        addContactToCategoryCommand.execute();
        addContactToCategoryCommand.setCategory(catCree2);
        addContactToCategoryCommand.execute();

        Assert.assertEquals("addContactToCategoryCommand failed : the contact is not added in a second category",
                2, cCree3.getCategoryIds().size());
        Assert.assertTrue("addContactToCategoryCommand failed : the contact is not added to the right category",
                cCree3.getCategoryIds().contains(2));
    }

    @Test
    public void createCategoryCommand() {

        // A - to check that the category is created with id = 1
        createCategoryCommand.setCategoryName("amis");
        createCategoryCommand.execute();
        Category catTest = new Category(1, "amis");

        Category resultat = null;
        for (Category cat : appModel.getProfile().getCategories().getCategories()) {
            if (cat.getName().equals(catTest.getName())) {
                resultat = cat;
                break;
            }
        }
        Assert.assertEquals("createCategoryCommand failed : the first category is not well created",
                catTest, resultat);

        // B - to check the user can't create two category with the same name
        int oldCategoriesSize = appModel.getProfile().getCategories().size();
        createCategoryCommand.execute();

        Assert.assertEquals("createCategoryCommand failed : the categorie amis is added twice", oldCategoriesSize,
                appModel.getProfile().getCategories().size());

        // C - to check if the incrementation of category ID is done
        createCategoryCommand.setCategoryName("famille");
        createCategoryCommand.execute();

        Category catTest2 = new Category(2, "famille");

        Category resultat2 = null;
        for (Category cat : appModel.getProfile().getCategories().getCategories()) {
            if (cat.getName().equals("famille")) {
                resultat2 = cat;
                break;
            }
        }
        Assert.assertEquals("createCategoryCommand failed : the second category is not well created",
                catTest2, resultat2);
    }

    @Test
    public void deleteCategoryCommand() {
        UserInfo u = new UserInfo();
        u.setAge(22);
        u.setFirstName("Mathilde");
        u.setLastName("ALL");
        u.setLogin("mathilde");
        u.setPassword("ccc");
        u.setPeerId(1L);

        Contact cCree = new Contact(u);
        Category catCree = new Category(1, "amis");

        createCategoryCommand.setCategoryName("amis");
        createCategoryCommand.execute();
        // Categories = {{0,PUBLIC}, {1,amis}}
        addContactToCategoryCommand.setContact(cCree);
        addContactToCategoryCommand.setCategory(catCree);
        addContactToCategoryCommand.execute();

        Assert.assertEquals("deleteCategoryCommand failed : the test can't start",
                appModel.getProfile().getCategories().getCategories().size(), 2);

        deleteCategoryCommand.setCategory(catCree);
        deleteCategoryCommand.execute();

        // A - Check if the category is deleted of categories list

        Assert.assertEquals("deleteCategoryCommand failed : the categry is not deleted",
                appModel.getProfile().getCategories().getCategories().size(), 1);


        // B - Check if the contact is not in the category anymore but is in the category Public
        Assert.assertFalse("deleteCategoryCommand failed : the first contact is still in the category",
                cCree.getCategoryIds().contains(1));

        Assert.assertTrue("deleteCategoryCommand failed : the first contact is not in the category Public",
                cCree.getCategoryIds().contains(0));


        // C - Check if the contact is not in the category anymore, neither in the category Public
        UserInfo u2 = new UserInfo();
        u2.setAge(22);
        u2.setFirstName("Mathilde");
        u2.setLastName("ALL");
        u2.setLogin("mathilde");
        u2.setPassword("ccc");
        u2.setPeerId(2L);

        Contact cCree2 = new Contact(u2);
        Category catCree2 = new Category(2, "famille");
        addContactToCategoryCommand.setContact(cCree2);
        addContactToCategoryCommand.setCategory(catCree);
        addContactToCategoryCommand.execute();
        addContactToCategoryCommand.setCategory(catCree2);
        addContactToCategoryCommand.execute();

        deleteCategoryCommand.setCategory(catCree);
        deleteCategoryCommand.execute();

        Assert.assertFalse("deleteCategoryCommand failed : the second contact is still in the category",
                cCree2.getCategoryIds().contains(1));

        Assert.assertFalse("deleteCategoryCommand failed : the second contact is in the category Public",
                cCree2.getCategoryIds().contains(0));

        Assert.assertTrue("deleteCategoryCommand failed : the second contact is not anymore in the category famille",
                cCree2.getCategoryIds().contains(2));

        // D - Check that this command does not delete the category Public
        Category catCree3 = new Category(Category.PUBLIC_CATEGORY_ID, Category.PUBLIC_CATEGORY_NAME);
        appModel.getProfile().getCategories().getCategories().add(catCree3);

        Assert.assertTrue("deleteCategoryCommand failed : the test can't start",
                appModel.getProfile().getCategories().contains(catCree3));

        deleteCategoryCommand.setCategory(catCree3);
        deleteCategoryCommand.execute();

        Assert.assertTrue("deleteCategoryCommand failed : the category Public is deleted (1)",
                appModel.getProfile().getCategories().contains(catCree3));
        Assert.assertEquals("deleteCategoryCommand failed : the category Public is deleted (2)",
                appModel.getProfile().getCategories().size(), 1);
    }

    @Test
    public void deleteContactCommand() {
        UserInfo u = new UserInfo();
        u.setAge(22);
        u.setFirstName("Mathilde");
        u.setLastName("ALL");
        u.setLogin("mathilde");
        u.setPassword("ccc");
        u.setPeerId(1L);

        Contact cCree = new Contact(u);

        addContactCommand.setContact(cCree);
        addContactCommand.execute();

        Assert.assertTrue("deleteContactCommand failed : the test can't start",
                appModel.getProfile().getContacts().contains(cCree));

        deleteContactCommand.setContact(cCree);
        deleteContactCommand.execute();

        Assert.assertFalse("deleteContactCommand failed : the is not deleted",
                appModel.getProfile().getContacts().contains(cCree));
    }

    @Test
    public void removeContactFromCategoryCommand() {
        UserInfo u = new UserInfo();
        u.setAge(22);
        u.setFirstName("Mathilde");
        u.setLastName("ALL");
        u.setLogin("mathilde");
        u.setPassword("ccc");
        u.setPeerId(1L);

        Contact cCree = new Contact(u);
        Category catCree = new Category(1, "amis");
        Category catCree2 = new Category(2, "famille");
        addContactToCategoryCommand.setContact(cCree);
        addContactToCategoryCommand.setCategory(catCree);
        addContactToCategoryCommand.execute();
        addContactToCategoryCommand.setCategory(catCree2);
        addContactToCategoryCommand.execute();

        removeContactFromCategoryCommand.setContact(cCree);
        removeContactFromCategoryCommand.setCategory(catCree);
        removeContactFromCategoryCommand.execute();

        /* A - Check if the contact is removed from the category amis but it is still in the category famille
         * the contact muss not be in the category Public
         */
        Assert.assertFalse("removeContactFromCategoryCommand failed : A - the contact is not removed from the category amis",
                cCree.getCategoryIds().contains(1));

        Assert.assertTrue("removeContactFromCategoryCommand failed : A - the contact is not anymore in the category famille",
                cCree.getCategoryIds().contains(2));

        Assert.assertFalse("removeContactFromCategoryCommand failed : A - the contact is in the category Public",
                cCree.getCategoryIds().contains(0));

        Assert.assertEquals("removeContactFromCategoryCommand failed : A - number of categoris for the contacts is wrong",
                cCree.getCategoryIds().size(), 1);


        // B - Check if the contact is removed from the category amis and added to the category Public

        UserInfo u2 = new UserInfo();
        u2.setAge(22);
        u2.setFirstName("Mathilde");
        u2.setLastName("ALL");
        u2.setLogin("mathilde");
        u2.setPassword("ccc");
        u2.setPeerId(2L);

        Contact cCree2 = new Contact(u2);
        addContactToCategoryCommand.setContact(cCree2);
        addContactToCategoryCommand.setCategory(catCree);
        addContactToCategoryCommand.execute();

        removeContactFromCategoryCommand.setContact(cCree2);
        removeContactFromCategoryCommand.setCategory(catCree);
        removeContactFromCategoryCommand.execute();

        Assert.assertFalse("removeContactFromCategoryCommand failed : B - the contact is not removed from the category amis",
                cCree2.getCategoryIds().contains(1));

        Assert.assertTrue("removeContactFromCategoryCommand failed : B - the contact is not added to the category Public",
                cCree2.getCategoryIds().contains(0));

        Assert.assertEquals("removeContactFromCategoryCommand failed : B - number of categoris for the contact is wrong",
                cCree2.getCategoryIds().size(), 1);


        // C - Check that a contact can't be removed from the category Public
        UserInfo u3 = new UserInfo();
        u3.setAge(22);
        u3.setFirstName("Mathilde");
        u3.setLastName("ALL");
        u3.setLogin("mathilde");
        u3.setPassword("ccc");
        u3.setPeerId(3L);

        Contact cCree3 = new Contact(u3);
        addContactCommand.setContact(cCree3);
        addContactCommand.execute();

        Category catCree3 = new Category(Category.PUBLIC_CATEGORY_ID, Category.PUBLIC_CATEGORY_NAME);

        removeContactFromCategoryCommand.setContact(cCree3);
        removeContactFromCategoryCommand.setCategory(catCree3);
        removeContactFromCategoryCommand.execute();

        Assert.assertTrue("removeContactFromCategoryCommand failed : C - the contact is removed from the category Public",
                cCree3.getCategoryIds().contains(0));

        Assert.assertEquals("removeContactFromCategoryCommand failed : C - number of categoris for the contacts is wrong",
                cCree3.getCategoryIds().size(), 1);

    }
}

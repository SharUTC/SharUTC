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

        Assert.assertEquals("addContactCommand failed",cTest,resultat);
    }

    @Test
    public void addContactToCategoryCommand() {
        //TODO test
        Assert.assertTrue(false);
    }

    @Test
    public void createCategoryCommand() {
        //TODO test
        Assert.assertTrue(false);
    }

    @Test
    public void deleteCategoryCommand() {
        //TODO test
        Assert.assertTrue(false);
    }

    @Test
    public void deleteContactCommand() {
        //TODO test
        Assert.assertTrue(false);
    }

    @Test
    public void removeContactFromCategoryCommand() {
        //TODO test
        Assert.assertTrue(false);
    }
}

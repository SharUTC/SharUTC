package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.account.AccountCreationCommand;
import fr.utc.lo23.sharutc.controler.command.account.ConnectionRequestCommand;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.userdata.Profile;
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
@GuiceJUnitRunner.GuiceModules({ProfileCreateAccountTestModule.class})
public class ProfileCreateAccountTest {

    private static final Logger log = LoggerFactory
            .getLogger(ProfileCreateAccountTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private UserService userService;
    @Inject
    private MusicService musicService;
    @Inject
    private FileService fileService;
    @Inject
    private AccountCreationCommand accountCreationCommand;
    @Inject
    private ConnectionRequestCommand connectionRequestCommand;
    private AppModelBuilder appModelBuilder = null;

    @Before
    public void before() {
        log.trace("building appModel");

        if (appModelBuilder == null) {
            appModelBuilder = new AppModelBuilder(appModel, musicService, userService);
        }/*
         appModelBuilder.mockAppModel();
         */
    }

    @After
    public void after() {
        log.trace("cleaning appModel");
        /*   appModelBuilder.clearAppModel();*/
        appModelBuilder.deleteFolders();
    }

    @Test
    public void accountCreationCommand() {
        // FIXME: le problème n'a rien a voir avec la commande de création, il faut corriger l'envoi de la connexion au niveau du réseau
        UserInfo info = new UserInfo();
        info.setAge(25);
        info.setFirstName("firstname");
        info.setLastName("lastname");
        info.setLogin("LOGIN");
        info.setPassword("pwd");
        accountCreationCommand.setUserInfo(info);
        accountCreationCommand.execute();

        Profile pTest = fileService.readProfileFile(info.getLogin());
        Assert.assertTrue("accountCreationCommand failed: the account is not created.", pTest.getUserInfo().equals(info));

        info.setAge(25);
        info.setFirstName("first");
        info.setLastName("last");
        info.setLogin("LOGIN");
        info.setPassword("pwd");
        accountCreationCommand.setUserInfo(info);
        accountCreationCommand.execute();

        Profile pTest2 = fileService.readProfileFile(info.getLogin());
        Assert.assertFalse("accountCreationCommand failed: the account is created, while login already exists.", pTest2.getUserInfo().equals(info));
        //test both commands
        connectionRequestCommand.setLogin("LOGIN");
        connectionRequestCommand.setPassword("pwd");
        connectionRequestCommand.execute();
        Profile pTest3 = appModel.getProfile();
        Assert.assertTrue("connectionRequestCommand failed: the account is not connected.", pTest3.getUserInfo().equals(pTest.getUserInfo()));
        //TODO validate datas


    }
}

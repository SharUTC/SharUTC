package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.account.AccountCreationCommand;
import fr.utc.lo23.sharutc.controler.command.account.ConnectionRequestCommand;
import fr.utc.lo23.sharutc.controler.command.account.DisconnectionCommand;
import fr.utc.lo23.sharutc.controler.network.Message;
import fr.utc.lo23.sharutc.controler.network.NetworkServiceMock;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
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
 *
 *
 */
@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({NetworkConnectionDisconnectionTestModule.class})
public class ConnectionDisconnectionTest {

    private static final Logger log = LoggerFactory
        .getLogger(ConnectionDisconnectionTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private UserService userService;
    @Inject
    private MusicService musicService;
    @Inject
    private NetworkServiceMock networkService;
    @Inject
    private ConnectionRequestCommand connectionRequestCommand;
    @Inject
    private DisconnectionCommand disconnectionCommand;
    @Inject
    private AccountCreationCommand accountCreationCommand;
    private AppModelBuilder appModelBuilder = null;

    /**
     *
     */
    @Before
    public void before() {
        log.trace("building appModel");
        if (appModelBuilder == null) {
            appModelBuilder = new AppModelBuilder(appModel, musicService, userService);
        }
        appModelBuilder.mockAppModel();
    }

    /**
     *
     */
    @After
    public void after() {
        log.trace("cleaning appModel");
        appModelBuilder.clearAppModel();
    }

    /**
     *
     */
    @Test
    public void connectionRequestCommand() {
        // add first user
        UserInfo userInfo = new UserInfo();
        userInfo.setLogin("tudorluchy1");
        userInfo.setPassword("password1");
        userInfo.setPeerId(4L);
        userInfo.setFirstName("Tudor");
        userInfo.setLastName("Luchiancenco");
        userInfo.setAge(22);
        
        // create account
        accountCreationCommand.setUserInfo(userInfo);
        accountCreationCommand.execute();

        // call command
        connectionRequestCommand.setLogin("tudorluchy1");
        connectionRequestCommand.setPassword("password1");
        connectionRequestCommand.execute();

        // tests
        Message m = networkService.getSentMessage();
        Assert.assertNotNull("Message null", m);
    }

    /**
     *
     */
    @Test
    public void disconnectionCommand() {
        disconnectionCommand.execute();

        // tests
        Message m = networkService.getSentMessage();
        Assert.assertNotNull("Message null", m);
    }
}

package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateDisconnectionCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateUserInfoAndReplyCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateUserInfoCommand;
import fr.utc.lo23.sharutc.controler.network.Message;
import fr.utc.lo23.sharutc.controler.network.MessageParser;
import fr.utc.lo23.sharutc.controler.network.MessageType;
import fr.utc.lo23.sharutc.controler.network.NetworkServiceMock;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.userdata.Peer;
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
public class NetworkConnectionDisconnectionTest {

    private static final Logger log = LoggerFactory
        .getLogger(NetworkConnectionDisconnectionTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private UserService userService;
    @Inject
    private MusicService musicService;
    @Inject
    private NetworkServiceMock networkService;
    @Inject
    private IntegrateUserInfoCommand integrateUserInfoCommand;
    @Inject
    private IntegrateUserInfoAndReplyCommand integrateUserInfoAndReplyCommand;
    @Inject
    private IntegrateDisconnectionCommand integrateDisconnectionCommand;
    @Inject
    private MessageParser messageParser;
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
    public void integrateUserInfoCommand() {
        // add first user
        UserInfo userInfo = new UserInfo();
        userInfo.setLogin("tudorluchy (id=1)");
        userInfo.setPassword("password");
        userInfo.setPeerId(16L);
        userInfo.setFirstName("Tudor");
        userInfo.setLastName("Luchiancenco");
        userInfo.setAge(22);

        // call command
        integrateUserInfoCommand.setUserInfo(userInfo);
        integrateUserInfoCommand.execute();

        // tests
        Assert.assertNotNull("Known doesn't peer exists", appModel.getProfile().getKnownPeerList().getPeerNameById(userInfo.getPeerId()));
        Assert.assertNotNull("Active doesn't peer exists", appModel.getActivePeerList().getByPeerId(userInfo.getPeerId()));
    }

    /**
     *
     */
    @Test
    public void integrateUserInfoAndReplyCommand() {
        // add first user
        UserInfo userInfo = new UserInfo();
        userInfo.setLogin("tudorluchy (id=2)");
        userInfo.setPassword("password");
        userInfo.setPeerId(4L);
        userInfo.setFirstName("Tudor");
        userInfo.setLastName("Luchiancenco");
        userInfo.setAge(22);

        // call command
        integrateUserInfoAndReplyCommand.setUserInfo(userInfo);
        integrateUserInfoAndReplyCommand.execute();

        // tests
        Assert.assertNotNull("Known doesn't peer exists", appModel.getProfile().getKnownPeerList().getPeerNameById(userInfo.getPeerId()));
        Assert.assertNotNull("Active doesn't peer exists", appModel.getActivePeerList().getByPeerId(userInfo.getPeerId()));

        Message m = networkService.getSentMessage();
        Peer p = networkService.getPeer();
        Assert.assertNotNull("Message null", m);
        Assert.assertNotNull("Peer null", p);

        messageParser.read(m);
        Assert.assertEquals("The mesage type must be : CONNECTION_RESPONSE", MessageType.CONNECTION_RESPONSE, m.getType());
    }

    /**
     *
     */
    @Test
    public void integrateDisconnectionCommand() {
        // add peer
        Peer p = new Peer(2L, "tudorluchy");

        // call command
        integrateDisconnectionCommand.setPeerId(p.getId());
        integrateDisconnectionCommand.execute();

        // tests
        Assert.assertNull("Active peer exists", appModel.getActivePeerList().getByPeerId(p.getId()));
    }
}

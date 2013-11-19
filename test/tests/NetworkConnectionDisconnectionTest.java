package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateBroadcastConnectionCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateUserInfoCommand;
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
    private IntegrateBroadcastConnectionCommand integrateBroadcastConnection;
    @Inject
    private IntegrateUserInfoCommand integrateConnection;
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
    public void integrateConnectionCommand() {
        // add first user
        UserInfo userInfo = new UserInfo();
        userInfo.setLogin("tudorluchy (id=1)");
        userInfo.setPassword("password");
        userInfo.setPeerId(4L);
        userInfo.setFirstName("Tudor");
        userInfo.setLastName("Luchiancenco");
        userInfo.setAge(22);
        // call command
        integrateConnection.setUserInfo(userInfo);
        integrateConnection.execute();

        // tests
        Assert.assertNotEquals("Contact exists", null, appModel.getProfile().getContacts().findById(userInfo.getPeerId()));
        Assert.assertNotEquals("Peer exists", null, appModel.getProfile().getKnownPeerList().getPeerNameById(userInfo.getPeerId()));
        Assert.assertNotEquals("Peer exists", null, appModel.getActivePeerList().getByPeerId(userInfo.getPeerId()));
    }
}

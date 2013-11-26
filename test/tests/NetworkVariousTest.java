package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateDisconnectionCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateUserInfoAndReplyCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateUserInfoCommand;
import fr.utc.lo23.sharutc.controler.command.search.SendMusicsCommand;
import fr.utc.lo23.sharutc.controler.network.Message;
import fr.utc.lo23.sharutc.controler.network.MessageParser;
import fr.utc.lo23.sharutc.controler.network.MessageType;
import fr.utc.lo23.sharutc.controler.network.NetworkServiceMock;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.domain.Catalog;
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
public class NetworkVariousTest {

    private static final Logger log = LoggerFactory
        .getLogger(NetworkVariousTest.class);
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
    private SendMusicsCommand sendMusicsCommand;
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
     * Connection, download file, deconnection
     */
    @Test
    public void downloadFile() {
        // add first user
        String login = "tudorluchy1";
        String password = "password1";
        Long peerId = 4L;
        UserInfo userInfo = new UserInfo();
        userInfo.setLogin(login);
        userInfo.setPassword(password);
        userInfo.setPeerId(peerId);
        userInfo.setFirstName("Tudor");
        userInfo.setLastName("Luchiancenco");
        userInfo.setAge(22);
        Peer peer = userInfo.toPeer();
        
        // CONNECTION
        integrateUserInfoCommand.setUserInfo(userInfo);
        integrateUserInfoCommand.execute();
        // tests
        Assert.assertNotNull("Known doesn't peer exists", appModel.getProfile().getKnownPeerList().getPeerNameById(userInfo.getPeerId()));
        Assert.assertNotNull("Active doesn't peer exists", appModel.getActivePeerList().getUserInfoByPeerId(userInfo.getPeerId()));
   
        Catalog dummyCatalog = appModel.getLocalCatalog();

        // DOWNLOAD
        sendMusicsCommand.setCatalog(dummyCatalog);
        sendMusicsCommand.setPeer(peer);
        sendMusicsCommand.execute();
        // tests
        Assert.assertNotNull("No message sent", networkService.getSentMessage());
        messageParser.read(networkService.getSentMessage());
        Assert.assertEquals("Catalog sent is erroneous", dummyCatalog,
                (Catalog) messageParser.getValue(Message.CATALOG));
        
        // DISCONNECTION
        integrateDisconnectionCommand.setPeerId(peer.getId());
        integrateDisconnectionCommand.execute();
        // tests
        Assert.assertNull("Active peer exists", appModel.getActivePeerList().getUserInfoByPeerId(peer.getId()));
    }
}

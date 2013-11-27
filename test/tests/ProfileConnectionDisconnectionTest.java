package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.account.AccountCreationCommand;
import fr.utc.lo23.sharutc.controler.command.account.ConnectionRequestCommand;
import fr.utc.lo23.sharutc.controler.command.account.DisconnectionCommand;
import fr.utc.lo23.sharutc.controler.network.Message;
import fr.utc.lo23.sharutc.controler.network.MessageParser;
import fr.utc.lo23.sharutc.controler.network.MessageType;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.network.NetworkServiceMock;
import fr.utc.lo23.sharutc.controler.service.FileService;
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
public class ProfileConnectionDisconnectionTest {

    private static final Logger log = LoggerFactory
            .getLogger(ProfileConnectionDisconnectionTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private UserService userService;
    @Inject
    private MusicService musicService;
    @Inject
    private FileService fileService;
    @Inject
    private NetworkServiceMock networkService;
    @Inject
    private ConnectionRequestCommand connectionRequestCommand;
    @Inject
    private DisconnectionCommand disconnectionCommand;
    @Inject
    private AccountCreationCommand accountCreationCommand;
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
            appModelBuilder = new AppModelBuilder(appModel, musicService, userService, fileService, networkService);
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
        appModelBuilder.deleteFolders();
    }

    /**
     *
     */
    // FIXME: le problème vient de connectionRequestCommand qui n'est pas encore
    // terminée, j'ai utilisé une méthode pour que le message parte en broadcast
    // mais la méthode est mal écrite ou c'est pas la bonne que j'ai utilisé,
    // si on veut pouvoir tester le message envoyé il faut alors créer
    // et envoyé le message de la même manière que les autres, sinon les tests
    // plantent puisque la couche réseau ne fonctionne pas
    @Test
    public void connectionRequestCommand() {
        // add first user
        String login = "tudorluchy1";
        String password = "password1";
        //  Long peerId = 4L;
        String firstName = "Tudor";
        String lastName = "Luchiancenco";

        UserInfo userInfo = new UserInfo();
        userInfo.setLogin(login);
        userInfo.setPassword(password);
        //   userInfo.setPeerId(peerId);
        userInfo.setFirstName(firstName);
        userInfo.setLastName(lastName);
        userInfo.setAge(22);

        // create account
        accountCreationCommand.setUserInfo(userInfo);
        accountCreationCommand.execute();

        // Clean profile to try connection
        userService.cleanProfile();
        Assert.assertNull(appModel.getProfile());

        // call command (wrong login / pass)
        connectionRequestCommand.setLogin("tudorluchy111");
        connectionRequestCommand.setPassword("password111");
        connectionRequestCommand.execute();
        Assert.assertNull(appModel.getProfile());

        // call command (right login / pass)
        connectionRequestCommand.setLogin("tudorluchy1");
        connectionRequestCommand.setPassword("password1");
        connectionRequestCommand.execute();

        Assert.assertNotNull(appModel.getProfile());
        Assert.assertEquals(appModel.getProfile().getUserInfo().getLogin(), login);
        Assert.assertEquals(appModel.getProfile().getUserInfo().getPassword(), password);
        Assert.assertEquals(appModel.getProfile().getUserInfo().getFirstName(), firstName);
        Assert.assertEquals(appModel.getProfile().getUserInfo().getLastName(), lastName);

        // tests
        Message m = networkService.getSentMessage();
        Assert.assertNotNull("Message null", m);

        messageParser.read(m);
        UserInfo receivedUserInfo = (UserInfo) messageParser.getValue(Message.USER_INFO);
        Assert.assertEquals("The mesage type must be : CONNECTION", MessageType.CONNECTION, m.getType());
        Assert.assertEquals("The login don't match", login, receivedUserInfo.getLogin());
        Assert.assertNull("The password must be null", receivedUserInfo.getPassword());
        Assert.assertEquals("The peerId don't match", appModel.getProfile().getUserInfo().getPeerId(), receivedUserInfo.getPeerId());

    }

    /**
     *
     */
    // FIXME: le problème est similaire, la correction est à faire dans NetworkServiceImpl
    @Test
    public void disconnectionCommand() {
        // add first user
        String login = "tudorluchy1";
        String password = "password1";
        //  Long peerId = 4L;
        String firstName = "Tudor";
        String lastName = "Luchiancenco";

        UserInfo userInfo = new UserInfo();
        userInfo.setLogin(login);
        userInfo.setPassword(password);
        //   userInfo.setPeerId(peerId);
        userInfo.setFirstName(firstName);
        userInfo.setLastName(lastName);
        userInfo.setAge(22);

        // create account
        accountCreationCommand.setUserInfo(userInfo);
        accountCreationCommand.execute();

        // Clean profile to try connection
        userService.cleanProfile();
        Assert.assertNull(appModel.getProfile());
        // call command (right login / pass)
        connectionRequestCommand.setLogin("tudorluchy1");
        connectionRequestCommand.setPassword("password1");
        connectionRequestCommand.execute();

        networkService.clear();

        disconnectionCommand.execute();

        // tests
        Message m = networkService.getSentMessage();
        Assert.assertNotNull("Message null", m);

        messageParser.read(m);
        Assert.assertEquals("The mesage type must be : DISCONNECT", MessageType.DISCONNECT, m.getType());

        Assert.assertNull(appModel.getProfile());
        Assert.assertNull(appModel.getLocalCatalog());
        Assert.assertNull(appModel.getRightsList());

        Assert.assertEquals("ActivePeerList not empty", 0, appModel.getActivePeerList().size());
        Assert.assertEquals("RemoteUserCatalog not empty", 0, appModel.getRemoteUserCatalog().size());
        Assert.assertEquals("SearchResults not empty", 0, appModel.getSearchResults().size());
        Assert.assertEquals("TmpCatalog not empty", 0, appModel.getTmpCatalog().size());
    }
}

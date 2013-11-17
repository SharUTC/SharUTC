package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.network.Message;
import fr.utc.lo23.sharutc.controler.network.MessageHandler;
import fr.utc.lo23.sharutc.controler.network.MessageParser;
import fr.utc.lo23.sharutc.controler.network.MessageType;
import fr.utc.lo23.sharutc.controler.network.NetworkServiceMock;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({TagMapTestModule.class})
public class MessageHandlerTest {

    private static final Logger log = LoggerFactory
            .getLogger(MessageHandlerTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private FileService fileService;
    @Inject
    private MusicService musicService;
    @Inject
    private NetworkServiceMock networkService;
    @Inject
    private UserService userService;
    @Inject
    private MessageHandler messageHandler;
    @Inject
    private MessageParser messageParser;
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

    /**
     *
     */
    @Test
    public void TAG_MAP() {
        appModel.getLocalCatalog().get(0).addTag("ROCK");
        appModel.getLocalCatalog().get(1).addTag("TV");
        appModel.getLocalCatalog().get(2).addTag("Rock");
        appModel.getLocalCatalog().get(2).addTag("Rock Indé");
        TagMap tagMap = musicService.getLocalTagMap();
        Message m = messageParser.write(MessageType.TAG_MAP, new Object[][]{{Message.TAG_MAP, tagMap}, {Message.CONVERSATION_ID, appModel.getCurrentConversationId()}});
        messageHandler.handleMessage(messageParser.toJSON(m));
        messageParser.read(m);
        Assert.assertTrue(true);
        Long convId = (Long) messageParser.getValue(Message.CONVERSATION_ID);
        Peer peer = messageParser.getSource();
        Assert.assertEquals("ConvID is not good",appModel.getCurrentConversationId(), convId);
        Assert.assertEquals("SourcePeer is not good",appModel.getProfile().getUserInfo().toPeer(), peer);
        System.out.println("peer = " + peer);
    }
}

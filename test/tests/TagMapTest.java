package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.GuiceJUnitRunner.GuiceModules;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteTagMapCommand;
import fr.utc.lo23.sharutc.controler.command.music.SendTagMapCommand;
import fr.utc.lo23.sharutc.controler.command.music.ShowTagMapCommand;
import fr.utc.lo23.sharutc.controler.network.Message;
import fr.utc.lo23.sharutc.controler.network.MessageParser;
import fr.utc.lo23.sharutc.controler.network.MessageType;
import fr.utc.lo23.sharutc.controler.network.NetworkServiceMock;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.MusicServiceMock;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@RunWith(GuiceJUnitRunner.class)
@GuiceModules({TagMapTestModule.class})
public class TagMapTest {

    private static final Logger log = LoggerFactory
            .getLogger(TagMapTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private FileService fileService;
    @Inject
    private MusicService musicService;
    @Inject
    private UserService userService;
    @Inject
    private NetworkServiceMock networkService;
    @Inject
    private IntegrateRemoteTagMapCommand integrateRemoteTagMapCommand;
    @Inject
    private SendTagMapCommand sendTagMapCommand;
    @Inject
    private ShowTagMapCommand showTagMapCommand;
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
    public void tagMapMerge() {

        // first step, we create a TagMap instance, and work with it,
        // here I'm testing both merge methods
        TagMap dummyTagMap = new TagMap();

        appModel.getLocalCatalog().get(0).addTag("ROCK");
        TagMap dummyTagMap1 = new TagMap();
        dummyTagMap1.merge("Rock", 1);

        dummyTagMap1.merge(dummyTagMap);
        // here is the first test, it's advised to always put a short
        // description of the test to help other people to understand what is
        // happening, don't hesitate to write a few comments
        Assert.assertEquals("TagMap merge method failed", dummyTagMap1,
                musicService.getLocalTagMap());
    }

    /**
     *
     */
    @Test
    public void tagMapIntegration() {
        // here I'm testing one of the commands of the application,
        // we set its attributes as we would do in the code
        //
        TagMap dummyTagMap = new TagMap();
        dummyTagMap.merge("Rock", 5);
        dummyTagMap.merge("Pop", 3);
        dummyTagMap.merge("Rock", 5);
        dummyTagMap.merge("Disco", 2000);
        //
        integrateRemoteTagMapCommand.setTagMap(dummyTagMap);
        //
        // then we continue to simulate the application, here it should modify
        // the application model but for this it requires to write a few more
        // code lines to first mock a complete and usable appModel with a mocked
        // account and catalog, that other developper could also reuse or
        // complete at the beginning
        //
        integrateRemoteTagMapCommand.execute();

        TagMap dummyTagMap1 = new TagMap();
        dummyTagMap1.merge("Rock", 5);
        dummyTagMap1.merge("Pop", 3);
        dummyTagMap1.merge("Rock", 5);
        dummyTagMap1.merge("Disco", 2000);
        //
        Assert.assertEquals("IntegrateRemoteTagMapCommand failed", dummyTagMap1,
                appModel.getNetworkTagMap());
        //
        // here is a second test, the same as the previous but with different
        // instances, it is highly preferable to create a new instance of the
        // TagMap here, don't use the previous instances
        //
        TagMap dummyTagMap2 = new TagMap();
        dummyTagMap2.merge("Rock", 10);
        dummyTagMap2.merge("Pop", 3);
        dummyTagMap2.merge("Jazz", 5);
        integrateRemoteTagMapCommand.setTagMap(dummyTagMap2);
        integrateRemoteTagMapCommand.execute();
        //
        TagMap dummyTagMap3 = new TagMap();
        dummyTagMap3.merge("Rock", 20);
        dummyTagMap3.merge("Pop", 6);
        dummyTagMap3.merge("Jazz", 5);
        dummyTagMap3.merge("Disco", 2000);
        Assert.assertEquals("IntegrateRemoteTagMapCommand failed", dummyTagMap3,
                appModel.getNetworkTagMap());
    }

    @Test
    public void sendTagMapCommand() {
        appModel.getLocalCatalog().get(0).addTag("ROCK");
        appModel.getLocalCatalog().get(1).addTag("TV");
        appModel.getLocalCatalog().get(2).addTag("Rock");
        appModel.getLocalCatalog().get(2).addTag("Rock Indé");
        ((MusicServiceMock) musicService).setTagMapDirty();
        long conversationId = 0L;
        sendTagMapCommand.setConversationId(conversationId);
        sendTagMapCommand.setPeer(appModel.getActivePeerList().getByPeerId(1L));
        sendTagMapCommand.execute();
        Assert.assertNotNull("No message sent", networkService.getSentMessage());
        // extract values from created message and validate them
        messageParser.read(networkService.getSentMessage());
        TagMap tm = (TagMap) messageParser.getValue(Message.TAG_MAP);
        Assert.assertNotNull("No tagMap in message", tm);
        Assert.assertEquals(musicService.getLocalTagMap(), tm);
    }

    @Test
    public void showTagMapCommand() {
        // Add test values in the local tag map
        Catalog cat = appModel.getLocalCatalog();

        cat.get(0).addTag("ROCK");
        cat.get(1).addTag("TV");
        cat.get(2).addTag("Rock");
        cat.get(2).addTag("Rock Indé");
        ((MusicServiceMock) musicService).setTagMapDirty();

        showTagMapCommand.execute();

        // Test if network tag map has been merged with the local catalog
        Assert.assertEquals("The network tag map has not been merged with the local tag map", new TagMap(cat), appModel.getNetworkTagMap());

        // Test Type and message content
        Message msgSent = networkService.getSentMessage();
        messageParser.read(msgSent);
        // Test Message Type
        Assert.assertEquals("The message type must be TAG_GET_MAP", MessageType.TAG_GET_MAP, msgSent.getType());
        // Test Conversation Id
        Long conversationId = (Long) messageParser.getValue(Message.CONVERSATION_ID);
        Assert.assertNotNull("the conversation id is null", conversationId);
    }
}

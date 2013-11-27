package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.search.IntegrateMusicSearchCommand;
import fr.utc.lo23.sharutc.controler.command.search.MusicSearchCommand;
import fr.utc.lo23.sharutc.controler.command.search.PerformMusicSearchCommand;
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
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
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
@GuiceJUnitRunner.GuiceModules({MusicSearchTestModule.class})
public class MusicSearchTest {

    private static final Logger log = LoggerFactory
            .getLogger(MusicScoreTest.class);
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
    private PerformMusicSearchCommand performMusicSearchCommand;
    @Inject
    private MusicSearchCommand musicSearchCommand;
    @Inject
    private IntegrateMusicSearchCommand integrateMusicSearchCommand;
    @Inject
    private AppModelBuilder appModelBuilder = null;
    @Inject
    private MessageParser messageParser;

    @Before
    public void before() {
        log.trace("building appModel");
        if (appModelBuilder == null) {
            appModelBuilder = new AppModelBuilder(appModel, musicService, userService, fileService, networkService);
        }
        appModelBuilder.mockAppModel();
        networkService.clear();

    }

    @After
    public void after() {
        log.trace("cleaning appModel");
        appModelBuilder.clearAppModel();
    }

    @Test
    public void sendSearch() {
        Assert.assertNull("Sent message not null before test", networkService.getSentMessage());
        musicSearchCommand.setSearchCriteria(new SearchCriteria("air"));
        musicSearchCommand.execute();
        Message message = networkService.getSentMessage();
        Assert.assertNotNull("Search not sent", message);
        messageParser.read(message);
        // Test Message Type
        Assert.assertEquals("The message type must be MUSIC_SEARCH", MessageType.MUSIC_SEARCH, message.getType());
        // Test Conversation Id
        Long conversationId = (Long) messageParser.getValue(Message.CONVERSATION_ID);
        Assert.assertNotNull("the conversation id is null", conversationId);
    }

    @Test
    public void performSearchAndReturnResults() {
        Assert.assertNull("Sent message not null before test", networkService.getSentMessage());

        performMusicSearchCommand.setConversationId(0L);
        performMusicSearchCommand.setPeer(appModel.getActivePeerList().getPeerByPeerId(1L));
        performMusicSearchCommand.setSearchCriteria(new SearchCriteria("air"));
        performMusicSearchCommand.execute();

        Message message = networkService.getSentMessage();
        Assert.assertNotNull("Search not sent", message);
        messageParser.read(message);
        // Test Message Type
        Assert.assertEquals("The message type must be MUSIC_RESULTS", MessageType.MUSIC_RESULTS, message.getType());
        // Test Conversation Id
        Long conversationId = (Long) messageParser.getValue(Message.CONVERSATION_ID);
        Assert.assertNotNull("the conversation id is null", conversationId);
        Assert.assertEquals("Should have found only one music", 1, ((Catalog) messageParser.getValue(Message.CATALOG)).size());
        Assert.assertEquals("Music found isn't the good one", appModel.getLocalCatalog().findMusicById(3L), ((Catalog) messageParser.getValue(Message.CATALOG)).get(0));
    }

    @Test
    public void performSearchAndReturnResults2() {
        Assert.assertNull("Sent message not null before test", networkService.getSentMessage());
        appModel.getLocalCatalog().get(0).addTag("ROCK");
        appModel.getLocalCatalog().get(1).addTag("TV");
        appModel.getLocalCatalog().get(2).addTag("Rock");
        performMusicSearchCommand.setConversationId(0L);
        performMusicSearchCommand.setPeer(appModel.getActivePeerList().getPeerByPeerId(1L));
        performMusicSearchCommand.setSearchCriteria(new SearchCriteria("   rOcK "));
        performMusicSearchCommand.execute();

        Message message = networkService.getSentMessage();
        Assert.assertNotNull("Search not sent", message);
        messageParser.read(message);
        // Test Message Type
        Assert.assertEquals("The message type must be MUSIC_RESULTS", MessageType.MUSIC_RESULTS, message.getType());
        // Test Conversation Id
        Long conversationId = (Long) messageParser.getValue(Message.CONVERSATION_ID);
        Assert.assertNotNull("the conversation id is null", conversationId);
        Assert.assertEquals("Should have found two musics with 'rock' tag", 2, ((Catalog) messageParser.getValue(Message.CATALOG)).size());
        Assert.assertEquals("Music found isn't the good one", appModel.getLocalCatalog().findMusicById(1L), ((Catalog) messageParser.getValue(Message.CATALOG)).get(0));
        Assert.assertEquals("Music found isn't the good one", appModel.getLocalCatalog().findMusicById(3L), ((Catalog) messageParser.getValue(Message.CATALOG)).get(1));
    }

    @Test
    public void integrateSearchResults() {
        Assert.assertEquals(0, appModel.getSearchResults().size());
        Catalog c = new Catalog();
        c.add(appModel.getLocalCatalog().get(0));
        integrateMusicSearchCommand.setResultsCatalog(c);
        integrateMusicSearchCommand.execute();

        Assert.assertEquals(1, appModel.getSearchResults().size());
    }
}

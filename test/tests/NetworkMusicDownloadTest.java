package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.player.PlayMusicCommand;
import fr.utc.lo23.sharutc.controler.command.player.SendMusicToPlayCommand;
import fr.utc.lo23.sharutc.controler.command.search.SendMusicsCommand;
import fr.utc.lo23.sharutc.controler.network.Message;
import fr.utc.lo23.sharutc.controler.network.MessageParser;
import fr.utc.lo23.sharutc.controler.network.NetworkServiceMock;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
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
@GuiceJUnitRunner.GuiceModules({NetworkMusicDownloadTestModule.class})
public class NetworkMusicDownloadTest {
    private static final Logger log = LoggerFactory
            .getLogger(NetworkMusicDownloadTest.class);
    @Inject
    private AppModel appModel;
    private AppModelBuilder appModelBuilder = null;
    @Inject
    private MessageParser messageParser;
    @Inject
    private MusicService musicService;
    @Inject
    private NetworkServiceMock networkService;
    @Inject
    private UserService userService;

    @Inject
    private SendMusicsCommand sendMusicsCommand;
    @Inject
    private PlayMusicCommand playMusicCommand;
    @Inject
    private SendMusicToPlayCommand sendMusicToPlayCommand;

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

    @Test
    public void sendMusicsCommand() {
        Catalog dummyCatalog = appModel.getLocalCatalog();
        Peer dummyPeer = new Peer(42, "John Doe");

        sendMusicsCommand.setCatalog(dummyCatalog);
        sendMusicsCommand.setPeer(dummyPeer);
        sendMusicsCommand.execute();

        Assert.assertNotNull("No message sent", networkService.getSentMessage());
        messageParser.read(networkService.getSentMessage());
        Assert.assertEquals("Catalog sent is erroneous", dummyCatalog,
                (Catalog) messageParser.getValue(Message.CATALOG));
    }

    @Test
    public void playMusicCommand() {
        Music dummyMusic = appModel.getLocalCatalog().get(0);
        dummyMusic.setOwnerPeerId(42l);

        playMusicCommand.setMusic(dummyMusic);
        playMusicCommand.execute();

        Assert.assertNotNull("No message sent", networkService.getSentMessage());
        messageParser.read(networkService.getSentMessage());
        Assert.assertEquals("Music id requested is erroneous", (long) dummyMusic.getId(),
                (long) messageParser.getValue(Message.MUSIC_ID));
    }

    @Test
    public void sendMusicToPlayCommand() {
        Music dummyMusic = appModel.getLocalCatalog().get(0);
        Peer dummyPeer = new Peer(42, "John Doe");

        sendMusicToPlayCommand.setConversationId(1337l);
        sendMusicToPlayCommand.setMusic(dummyMusic);
        sendMusicToPlayCommand.setPeer(dummyPeer);
        sendMusicToPlayCommand.execute();

        Assert.assertNotNull("No message sent", networkService.getSentMessage());
        messageParser.read(networkService.getSentMessage());
        Assert.assertEquals("ConversationId sent is erroneous", 1337l,
                (long) messageParser.getValue(Message.CONVERSATION_ID));
        Music music = (Music) messageParser.getValue(Message.MUSIC);
        Assert.assertNotNull("Music sent is null", music);
        Assert.assertEquals("Music hash mismatch", dummyMusic.getHash(), music.getHash());
        Assert.assertNotNull("Music file is null", music.getFileBytes());
    }
}

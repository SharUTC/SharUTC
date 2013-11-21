package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.music.FetchRemoteCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.SendCatalogCommand;
import fr.utc.lo23.sharutc.controler.network.Message;
import fr.utc.lo23.sharutc.controler.network.MessageType;
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
 *
 *
 */
@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({NetworkCatalogTestModule.class})
public class NetworkCatalogTest {
    
    private static final Logger log = LoggerFactory
        .getLogger(ConnectionDisconnectionTest.class);
    @Inject
    private AppModel appModel;
    private AppModelBuilder appModelBuilder = null;
    @Inject
    private MusicService musicService;
    @Inject
    private UserService userService;
    @Inject 
    private NetworkServiceMock networkService;
    
    @Inject
    private FetchRemoteCatalogCommand fetchRemoteCatalogCommand;
    @Inject
    private SendCatalogCommand sendCatalogCommand;

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
    public void fetchRemoteCatalogCommand(){
        // Create a Peer
        Peer peerTest = new Peer();
        peerTest.setDisplayName("PeerTest");
        peerTest.setId(9875);
        
        // Create a Music
        Music musicTest = new Music();
        musicTest.setFileName("MusicTest");
        
        fetchRemoteCatalogCommand.setPeer(peerTest);
        fetchRemoteCatalogCommand.execute();
        
        Message msgSent = networkService.getSentMessage();
        Assert.assertNotNull("No message sent", msgSent);
        Assert.assertEquals("The mesage type must be : MUSIC_GET", MessageType.MUSIC_GET, msgSent.getType());
    }
    
    @Test
    public void sendCatalogCommand(){
        Music music = new Music();
        music.setTitle("musicTest");
        
        appModel.getLocalCatalog().add(music);
        
        long conversationId = 0L;
        sendCatalogCommand.setConversationId(conversationId);
        sendCatalogCommand.setPeer(appModel.getActivePeerList().getByPeerId(1L));
        sendCatalogCommand.execute();
        
        Message msgSent = networkService.getSentMessage();
        Assert.assertNotNull("No message sent", msgSent);
        
        if(msgSent.getContent().containsKey(Message.CATALOG)){
            Assert.assertNotNull("tha catalog cant't be null",(Catalog) msgSent.getContent().get(Message.CATALOG));
        } else {
            Assert.fail("message have to contain a content as MESSAGE.CATALOG");
        }
        if(msgSent.getContent().containsKey(Message.CONVERSATION_ID)){
            Assert.assertEquals("the conversationId is false", conversationId, msgSent.getContent().get(Message.CONVERSATION_ID));
        } else {
            Assert.fail("the message has to contain a content as Message.CONVERSATION_ID");
        }
    }
}

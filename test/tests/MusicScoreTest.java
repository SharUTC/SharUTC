package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.GuiceJUnitRunner.GuiceModules;
import fr.utc.lo23.sharutc.controler.command.music.SetScoreCommand;
import fr.utc.lo23.sharutc.controler.command.music.UnsetScoreCommand;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.Score;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.util.HashSet;
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
@GuiceModules({MusicScoreTestModule.class})
public class MusicScoreTest {

    private static final Logger log = LoggerFactory
            .getLogger(MusicScoreTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private FileService fileService;
    @Inject
    private UserService userService;
    @Inject
    private MusicService musicService;
    @Inject
    private SetScoreCommand setScoreCommand;
    @Inject
    private UnsetScoreCommand unsetScoreCommand;
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
    public void setScore() {
        Music dummyMusic = new Music();
        dummyMusic.setFileName("Dummy Music");
        setScoreCommand.setMusic(dummyMusic);

        Peer dummyPeer = new Peer();
        dummyPeer.setDisplayName("Dummy Peer");
        dummyPeer.setId(436907);
        // dummyPeer.setIpAddress("192.168.1.1");
        setScoreCommand.setPeer(dummyPeer);

        Integer scoreValue = 4;
        setScoreCommand.setScore(scoreValue);

        setScoreCommand.execute();

        Score score = dummyMusic.getScore(dummyPeer);
        Assert.assertNotNull("SetScoreCommand failed", score);

        if (score != null) {
            Assert.assertSame("SetScoreCommand failed", score.getValue(),
                    scoreValue);
        }
    }

    /**
     *
     */
    @Test
    public void unsetScore() {
        Music dummyMusic = new Music();
        dummyMusic.setFileName("Dummy Music");
        setScoreCommand.setMusic(dummyMusic);

        Peer dummyPeer = new Peer();
        dummyPeer.setDisplayName("Dummy Peer");
        dummyPeer.setId(436907);
        // dummyPeer.setIpAddress("192.168.1.1");
        setScoreCommand.setPeer(dummyPeer);

        Integer scoreValue = 4;
        setScoreCommand.setScore(scoreValue);

        setScoreCommand.execute();

        unsetScoreCommand.setMusic(dummyMusic);
        unsetScoreCommand.setPeer(dummyPeer);
        unsetScoreCommand.execute();

        Score score = dummyMusic.getScore(dummyPeer);
        Assert.assertNull("SetScoreCommand failed", score);
    }
}

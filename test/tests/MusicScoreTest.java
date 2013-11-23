package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.GuiceJUnitRunner.GuiceModules;
import fr.utc.lo23.sharutc.controler.command.music.SetScoreCommand;
import fr.utc.lo23.sharutc.controler.command.music.UnsetScoreCommand;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.Score;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Allow testing of the setting and unsetting of score on musics
 */
@RunWith(GuiceJUnitRunner.class)
@GuiceModules({MusicScoreTestModule.class})
public class MusicScoreTest {

    private static final Logger log = LoggerFactory
            .getLogger(MusicScoreTest.class);
    @Inject
    private AppModel appModel;
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
     * Test on the setting of a score on a music
     */
    @Test
    public void setScore() {
        // FIXME: use music from catalog , and use active or kwown peer to set a score
        // FIXME: don't use dummyMusic in Assert..., even with previous correction, get the music reference from localCatalog directly in Assert or with another var
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
        Assert.assertNotNull("SetScoreCommand failed 0", score);

        if (score != null) {
            Assert.assertSame("SetScoreCommand failed 1", score.getValue(),
                    scoreValue);
        }
    }

    /**
     * Test on the unsetting of a score on a music
     */
    @Test
    public void unsetScore() {
        // FIXME: same remarks for this test, see setScore Test
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

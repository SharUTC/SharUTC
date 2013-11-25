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
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
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
        Music music = appModel.getLocalCatalog().get(0);
        setScoreCommand.setMusic(music);

        UserInfo userInfo = appModel.getActivePeerList().getActivePeers().keySet().iterator().next();
        Peer peer = new Peer(userInfo.getPeerId(), userInfo.getLogin());
        setScoreCommand.setPeer(peer);

        Integer scoreValue = 4;
        setScoreCommand.setScore(scoreValue);

        setScoreCommand.execute();

        Score score = appModel.getLocalCatalog().get(0).getScore(peer);
        Assert.assertNotNull("SetScoreCommand failed 0", score);

        Assert.assertEquals("SetScoreCommand failed 1", score.getValue(),
                scoreValue);

    }

    /**
     * Test on the unsetting of a score on a music
     */
    @Test
    public void unsetScore() {
        Music music = appModel.getLocalCatalog().get(0);
        setScoreCommand.setMusic(music);

        UserInfo userInfo = appModel.getActivePeerList().getActivePeers().keySet().iterator().next();
        Peer peer = new Peer(userInfo.getPeerId(), userInfo.getLogin());
        setScoreCommand.setPeer(peer);

        Integer scoreValue = 4;
        setScoreCommand.setScore(scoreValue);

        setScoreCommand.execute();

        unsetScoreCommand.setMusic(music);
        unsetScoreCommand.setPeer(peer);
        unsetScoreCommand.execute();

        Score score = appModel.getLocalCatalog().get(0).getScore(peer);
        Assert.assertNull("SetScoreCommand failed", score);
    }

    /**
     * Test on the setting of a score on a music
     */
    @Test
    public void setScore2() {
        Music music = appModel.getLocalCatalog().get(0);
        setScoreCommand.setMusic(music);

        UserInfo userInfo = appModel.getActivePeerList().getActivePeers().keySet().iterator().next();
        Peer peer = new Peer(userInfo.getPeerId(), userInfo.getLogin());
        setScoreCommand.setPeer(peer);

        setScoreCommand.setScore(4);
        setScoreCommand.execute();
        Score score = appModel.getLocalCatalog().get(0).getScore(peer);
        Assert.assertNotNull("SetScoreCommand failed", score);
        Assert.assertEquals(new Integer(4), score.getValue());

        setScoreCommand.setScore(0);
        setScoreCommand.execute();
        Score score2 = appModel.getLocalCatalog().get(0).getScore(peer);
        Assert.assertNotNull("SetScoreCommand failed", score2);
        Assert.assertEquals(new Integer(0), score2.getValue());
    }
}

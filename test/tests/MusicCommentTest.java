package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.GuiceJUnitRunner.GuiceModules;
import fr.utc.lo23.sharutc.controler.command.music.AddCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.EditCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveCommentCommand;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.domain.Comment;
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
 * Allow testing of the adding, editing and removing of comments on musics
 */
@RunWith(GuiceJUnitRunner.class)
@GuiceModules({MusicCommentTestModule.class})
public class MusicCommentTest {

    private static final Logger log = LoggerFactory
            .getLogger(MusicCommentTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private MusicService musicService;
    @Inject
    private UserService userService;
    @Inject
    private AddCommentCommand addCommentCommand;
    @Inject
    private EditCommentCommand editCommentCommand;
    @Inject
    private RemoveCommentCommand removeCommentCommand;
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
     * Allow testing of the adding of comments on musics
     */
    @Test
    public void addComment() {
        Music dummyMusic = new Music();
        dummyMusic.setFileName("Dummy Music");
        addCommentCommand.setMusic(dummyMusic);

        Peer dummyAuthorPeer = new Peer();
        dummyAuthorPeer.setDisplayName("Dummy Peer 0");
        dummyAuthorPeer.setId(436907);
        addCommentCommand.setAuthorPeer(dummyAuthorPeer);

        Peer dummyOwnerPeer = new Peer();
        dummyOwnerPeer.setDisplayName("Dummy Peer 1");
        dummyOwnerPeer.setId(907868);
        addCommentCommand.setOwnerPeer(dummyOwnerPeer);

        addCommentCommand.setComment("This is a comment");

        addCommentCommand.execute();

        Comment myComment = dummyMusic.getComment(dummyAuthorPeer, 0);

        Assert.assertNotNull("addComment failed", myComment);

        if (myComment != null) {
            Assert.assertSame("addComment failed", myComment.getText(),
                    "This is a comment");
        }
    }

    /**
     * Allow testing of the editing of comments on musics
     */
    @Test
    public void editComment() {
        Music dummyMusic = new Music();
        dummyMusic.setFileName("Dummy Music");
        addCommentCommand.setMusic(dummyMusic);

        Peer dummyAuthorPeer = new Peer();
        dummyAuthorPeer.setDisplayName("Dummy Peer 0");
        dummyAuthorPeer.setId(436907);
        addCommentCommand.setAuthorPeer(dummyAuthorPeer);

        Peer dummyOwnerPeer = new Peer();
        dummyOwnerPeer.setDisplayName("Dummy Peer 1");
        dummyOwnerPeer.setId(907868);
        addCommentCommand.setOwnerPeer(dummyOwnerPeer);

        addCommentCommand.setComment("This is a comment");

        addCommentCommand.execute();

        editCommentCommand.setMusic(dummyMusic);
        editCommentCommand.setAuthorPeer(dummyAuthorPeer);
        editCommentCommand.setOwnerPeer(dummyOwnerPeer);
        editCommentCommand.setCommentId(0);
        editCommentCommand.setComment("This is the new comment");

        editCommentCommand.execute();

        Comment myComment = dummyMusic.getComment(dummyAuthorPeer, 0);

        Assert.assertNotNull("editComment failed", myComment);

        if (myComment != null) {
            Assert.assertNotSame("editComment failed", myComment.getText(),
                    "This is a comment");
            Assert.assertSame("editComment failed", myComment.getText(),
                    "This is the new comment");
        }
    }

    /**
     * Allow testing of the removing of comments on musics
     */
    @Test
    public void removeComment() {
        Music dummyMusic = new Music();
        dummyMusic.setFileName("Dummy Music");
        addCommentCommand.setMusic(dummyMusic);

        Peer dummyAuthorPeer = new Peer();
        dummyAuthorPeer.setDisplayName("Dummy Peer 0");
        dummyAuthorPeer.setId(436907);
        addCommentCommand.setAuthorPeer(dummyAuthorPeer);

        Peer dummyOwnerPeer = new Peer();
        dummyOwnerPeer.setDisplayName("Dummy Peer 1");
        dummyOwnerPeer.setId(907868);
        addCommentCommand.setOwnerPeer(dummyOwnerPeer);

        addCommentCommand.setComment("This is a comment");

        addCommentCommand.execute();

        removeCommentCommand.setMusic(dummyMusic);
        removeCommentCommand.setPeer(dummyAuthorPeer);
        removeCommentCommand.setCommentId(0);
        removeCommentCommand.execute();

        Comment myComment = dummyMusic.getComment(dummyAuthorPeer, 0);

        Assert.assertNull("removeComment failed", myComment);
    }
}

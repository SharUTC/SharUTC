package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.GuiceJUnitRunner.GuiceModules;
import fr.utc.lo23.sharutc.controler.command.music.AddCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.EditCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveCommentCommand;
import fr.utc.lo23.sharutc.controler.service.FileService;
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
 *
 */
@RunWith(GuiceJUnitRunner.class)
@GuiceModules({MusicCommentTestModule.class})
public class MusicCommentTest {

    private static final Logger log = LoggerFactory
            .getLogger(MusicCommentTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private FileService fileService;
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
            appModelBuilder = new AppModelBuilder(appModel, fileService);
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
    public void addComment() {
        Music dummyMusic = new Music();
        dummyMusic.setFileName("Dummy Music");
        addCommentCommand.setMusic(dummyMusic);

        Peer dummyPeer = new Peer();
        dummyPeer.setDisplayName("Dummy Peer");
        dummyPeer.setId(436907);
        dummyPeer.setIpAddress("192.168.1.1");
        addCommentCommand.setAuthorPeer(dummyPeer);

        addCommentCommand.setComment("This is a comment");

        addCommentCommand.execute();

        Comment myComment = dummyMusic.getComment(dummyPeer,
                Comment.getCurrentIndex());

        Assert.assertNotNull("editComment failed", myComment);

        if (myComment != null) {
            Assert.assertSame("editComment failed", myComment.getText(),
                    "This is a comment");
        }
    }

    /**
     *
     */
    @Test
    public void editComment() {
        Music dummyMusic = new Music();
        dummyMusic.setFileName("Dummy Music");
        addCommentCommand.setMusic(dummyMusic);

        Peer dummyPeer = new Peer();
        dummyPeer.setDisplayName("Dummy Peer");
        dummyPeer.setId(436907);
        dummyPeer.setIpAddress("192.168.1.1");
        addCommentCommand.setAuthorPeer(dummyPeer);

        addCommentCommand.setComment("This is a comment");

        addCommentCommand.execute();

        editCommentCommand.setMusic(dummyMusic);
        editCommentCommand.setAuthorPeer(dummyPeer);
        editCommentCommand.setCommentId(Comment.getCurrentIndex());
        editCommentCommand.setComment("This is the new comment");

        editCommentCommand.execute();

        Comment myComment = dummyMusic.getComment(dummyPeer,
                Comment.getCurrentIndex());

        Assert.assertNotNull("editComment failed", myComment);

        if (myComment != null) {
            Assert.assertNotSame("editComment failed", myComment.getText(),
                    "This is a comment");
            Assert.assertSame("editComment failed", myComment.getText(),
                    "This is the new comment");
        }
    }

    /**
     *
     */
    @Test
    public void removeComment() {
        Music dummyMusic = new Music();
        dummyMusic.setFileName("Dummy Music");
        addCommentCommand.setMusic(dummyMusic);

        Peer dummyPeer = new Peer();
        dummyPeer.setDisplayName("Dummy Peer");
        dummyPeer.setId(436907);
        dummyPeer.setIpAddress("192.168.1.1");
        addCommentCommand.setAuthorPeer(dummyPeer);

        addCommentCommand.setComment("This is a comment");

        addCommentCommand.execute();
        
        removeCommentCommand.setMusic(dummyMusic);
        removeCommentCommand.setPeer(dummyPeer);
        removeCommentCommand.setCommentId(Comment.getCurrentIndex());
        removeCommentCommand.execute();

        Comment myComment = dummyMusic.getComment(dummyPeer,
                Comment.getCurrentIndex());

        Assert.assertNull("editComment failed", myComment);
    }
}

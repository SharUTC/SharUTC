package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.GuiceJUnitRunner.GuiceModules;
import fr.utc.lo23.sharutc.controler.command.music.AddCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.EditCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveCommentCommand;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.AppModelMock;
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
    private FileService fileService;
    @Inject
    private NetworkService networkService;
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
            appModelBuilder = new AppModelBuilder(appModel, musicService, userService, fileService, networkService);
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
        Music music = appModel.getLocalCatalog().get(0);
        music.setFileName("Dummy Music");
        addCommentCommand.setMusic(music);

        Peer authorPeer = appModel.getActivePeerList().getPeerByPeerId(1L);
        addCommentCommand.setAuthorPeer(authorPeer);

        Peer ownerPeer = appModel.getActivePeerList().getPeerByPeerId(AppModelBuilder.LOCAL_ACCOUNT_PEER_ID);
        addCommentCommand.setOwnerPeer(ownerPeer);

        addCommentCommand.setComment("This is a comment");
        
        addCommentCommand.execute();

        Comment myComment = appModel.getLocalCatalog().get(0).getComment(authorPeer, 0);

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
        Music music = appModel.getLocalCatalog().get(0);
        music.setFileName("Dummy Music");
        addCommentCommand.setMusic(music);

        Peer authorPeer = appModel.getActivePeerList().getPeerByPeerId(1L);
        addCommentCommand.setAuthorPeer(authorPeer);

        Peer ownerPeer = appModel.getActivePeerList().getPeerByPeerId(AppModelBuilder.LOCAL_ACCOUNT_PEER_ID);
        addCommentCommand.setOwnerPeer(ownerPeer);

        addCommentCommand.setComment("This is a comment");

        addCommentCommand.execute();

        editCommentCommand.setMusic(appModel.getLocalCatalog().get(0));
        editCommentCommand.setAuthorPeer(authorPeer);
        editCommentCommand.setOwnerPeer(ownerPeer);
        editCommentCommand.setCommentId(0);
        editCommentCommand.setComment("This is the new comment");

        editCommentCommand.execute();

        Comment myComment = appModel.getLocalCatalog().get(0).getComment(authorPeer, 0);

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
        Music music = appModel.getLocalCatalog().get(0);
        music.setFileName("Dummy Music");
        addCommentCommand.setMusic(music);

        Peer authorPeer = appModel.getActivePeerList().getPeerByPeerId(1L);
        addCommentCommand.setAuthorPeer(authorPeer);

        Peer ownerPeer = appModel.getActivePeerList().getPeerByPeerId(AppModelBuilder.LOCAL_ACCOUNT_PEER_ID);
        addCommentCommand.setOwnerPeer(ownerPeer);

        addCommentCommand.setComment("This is a comment");

        addCommentCommand.execute();

        removeCommentCommand.setMusic(appModel.getLocalCatalog().get(0));
        removeCommentCommand.setPeer(authorPeer);
        removeCommentCommand.setCommentId(0);
        removeCommentCommand.execute();

        Comment myComment = appModel.getLocalCatalog().get(0).getComment(authorPeer, 0);

        Assert.assertNull("removeComment failed", myComment);
    }
}

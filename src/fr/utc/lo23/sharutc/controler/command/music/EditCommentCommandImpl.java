package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class EditCommentCommandImpl implements EditCommentCommand {

    private static final Logger log = LoggerFactory
            .getLogger(EditCommentCommandImpl.class);
    private final AppModel appModel;
    private final MusicService musicService;
    private final NetworkService networkService;
    private Peer mOwnerPeer;
    private Peer mAuthorPeer;
    private Music mMusic;
    private String mComment;
    private int mCommentId;

    /**
     * Constructor of EditCommentCommandImpl
     *
     * @param appModel The model of the application
     * @param musicService The service of musics
     * @param networkService The service of the network
     */
    @Inject
    public EditCommentCommandImpl(AppModel appModel, MusicService musicService,
            NetworkService networkService) {
        this.appModel = appModel;
        this.musicService = musicService;
        this.networkService = networkService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Peer getOwnerPeer() {
        return mOwnerPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOwnerPeer(Peer ownerPeer) {
        this.mOwnerPeer = ownerPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Peer getAuthorPeer() {
        return mAuthorPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAuthorPeer(Peer authorPeer) {
        this.mAuthorPeer = authorPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Music getMusic() {
        return mMusic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMusic(Music music) {
        this.mMusic = music;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getComment() {
        return mComment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setComment(String comment) {
        this.mComment = comment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCommentId() {
        return mCommentId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCommentId(int commentId) {
        this.mCommentId = commentId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("EditCommentCommandImpl ...");
        if (mOwnerPeer == null || mMusic == null) {
            Utils.throwMissingParameter(log, new Throwable());
        } else if (appModel.getProfile().getUserInfo().getPeerId().equals(mMusic.getOwnerPeerId())) {
            musicService.editComment(mAuthorPeer, mMusic, mComment, mCommentId); // local
            appModel.getProfile().getKnownPeerList().update(mAuthorPeer);
            musicService.saveUserMusicFile();
        } else {
            networkService.editComment(mOwnerPeer, mAuthorPeer, mMusic, mComment, mCommentId); // distant
        }
        log.info("EditCommentCommandImpl DONE");
    }
}

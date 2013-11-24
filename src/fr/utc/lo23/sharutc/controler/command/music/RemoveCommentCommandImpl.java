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
public class RemoveCommentCommandImpl implements RemoveCommentCommand {

    private static final Logger log = LoggerFactory
            .getLogger(RemoveCommentCommandImpl.class);
    private final AppModel appModel;
    private final MusicService musicService;
    private final NetworkService networkService;
    private Peer mPeer;
    private Music mMusic;
    private int mCommentId;

    /**
     * Constructor of RemoveCommentCommandImpl
     *
     * @param appModel The model of the application
     * @param musicService The service of musics
     * @param networkService The service of the network
     */
    @Inject
    public RemoveCommentCommandImpl(AppModel appModel, MusicService musicService,
            NetworkService networkService) {
        this.appModel = appModel;
        this.musicService = musicService;
        this.networkService = networkService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Peer getPeer() {
        return mPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPeer(Peer peer) {
        this.mPeer = peer;
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
        log.info("RemoveCommentCommandImpl ...");
        if (mPeer == null || mMusic == null) {
            Utils.throwMissingParameter(log, new Throwable());
        } else if (appModel.getProfile().getUserInfo().getPeerId().equals(mMusic.getOwnerPeerId())) {
            musicService.removeComment(mPeer, mMusic, mCommentId); // local
            musicService.removeFromKnownPeersIfUseless(mPeer);
        } else {
            networkService.removeComment(mPeer, mMusic, mCommentId); // distant
        }
        log.info("RemoveCommentCommandImpl DONE");
    }
}

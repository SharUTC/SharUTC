package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class SetScoreCommandImpl implements SetScoreCommand {

    private static final Logger log = LoggerFactory
            .getLogger(SetScoreCommandImpl.class);
    private final AppModel appModel;
    private final MusicService musicService;
    private final UserService userService;
    private final NetworkService networkService;
    private Peer mPeer;
    private Music mMusic;
    private int mScore;

    /**
     * Constructor of SetScoreCommandImpl
     *
     * @param appModel The model of the application
     * @param musicService The service of musics
     * @param networkService The service of the network
     */
    @Inject
    public SetScoreCommandImpl(AppModel appModel, MusicService musicService, UserService userService,
            NetworkService networkService) {
        this.appModel = appModel;
        this.musicService = musicService;
        this.userService = userService;
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
    public int getScore() {
        return mScore;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScore(int score) {
        this.mScore = score;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("SetScoreCommandImpl ...");
        if (mPeer == null || mMusic == null) {
            Utils.throwMissingParameter(log, new Throwable());
        } else if (appModel.getProfile().getUserInfo().getPeerId().equals(mMusic.getOwnerPeerId())) {
            musicService.setScore(mPeer, mMusic, mScore); // local
            appModel.getProfile().getKnownPeerList().update(mPeer);
            musicService.saveUserMusicFile();
            userService.saveProfileFiles();
        } else {
            Peer ownerPeer = appModel.getActivePeerList().getPeerByPeerId(mMusic.getOwnerPeerId());
            networkService.setScore(ownerPeer, mPeer, mMusic, mScore); // distant
        }
        log.info("SetScoreCommandImpl DONE");
    }
}

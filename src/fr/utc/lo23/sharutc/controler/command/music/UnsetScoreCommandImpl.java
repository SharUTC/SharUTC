package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class UnsetScoreCommandImpl implements UnsetScoreCommand {

    private static final Logger log = LoggerFactory
            .getLogger(UnsetScoreCommandImpl.class);
    private final MusicService musicService;
    private Peer mPeer;
    private Music mMusic;

    /**
     * {@inheritDoc}
     */
    @Inject
    public UnsetScoreCommandImpl(MusicService musicService) {
        this.musicService = musicService;
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
    public void execute() {
        log.info("UnsetScoreCommandImpl ...");
        // FIXME : if mPeer.id is local account (appModel.profile.userInfo.peerId) then use MusicService
        // ELSE use networkService
        musicService.unsetScore(mPeer, mMusic);
        log.info("UnsetScoreCommandImpl DONE");
    }
}

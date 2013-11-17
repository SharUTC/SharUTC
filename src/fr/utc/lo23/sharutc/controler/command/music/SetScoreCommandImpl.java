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
public class SetScoreCommandImpl implements SetScoreCommand {

    private static final Logger log = LoggerFactory
            .getLogger(SetScoreCommandImpl.class);
    private final MusicService musicService;
    private Peer mPeer;
    private Music mMusic;
    private int mScore;

    /**
     * {@inheritDoc}
     */
    @Inject
    public SetScoreCommandImpl(MusicService musicService) {
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
        
        // FIXME: use music service if music is local (peerId = appModel.profile.userInfo.peerId)
        // else, music comes from network, send message to update score value
        // then also change local value to simulate accepted changes without sending more message
        // @See MessageHandlerImpl.java, two switch values are waiting for you !
        // note that if score value is null, score is to be deleted rather than set to null
        musicService.setScore(mPeer, mMusic, mScore);
        log.info("SetScoreCommandImpl DONE");
    }
}

package fr.utc.lo23.sharutc.controler.command.player;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class SendMusicToPlayCommandImpl implements SendMusicToPlayCommand {

    private static final Logger log = LoggerFactory.getLogger(SendMusicToPlayCommandImpl.class);
    private Music mMusic;
    private Peer mPeer;
    private final MusicService musicService;

    @Inject
    public SendMusicToPlayCommandImpl(MusicService musicService) {
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
        log.info("SendMusicToPlayCommand ...");
        Music m = mMusic.clone();
        m.getComments().clear();
        m.getScores().clear();
        m.getCategoryIds().clear();
        m.getTags().clear();
        musicService.loadMusicFile(m);
        log.info("SendMusicToPlayCommand DONE");
    }
}

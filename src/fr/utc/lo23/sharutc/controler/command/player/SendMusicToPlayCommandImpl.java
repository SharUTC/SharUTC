package fr.utc.lo23.sharutc.controler.command.player;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
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
    private Long mConversationId;
    private final MusicService musicService;
    private final NetworkService networkService;

    @Inject
    public SendMusicToPlayCommandImpl(MusicService musicService, NetworkService networkService) {
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
    public Long getConversationId() {
        return mConversationId;
    }

    /**
     * {@inheritDoc}
     */
    public void setConversationId(Long conversationId) {
        this.mConversationId = conversationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("SendMusicToPlayCommand ...");
        Music musicToSend = mMusic.clone();
        musicService.loadMusicFile(musicToSend);
        networkService.sendMusicToPlay(mPeer, mConversationId, musicToSend);
        log.info("SendMusicToPlayCommand DONE");
    }
}

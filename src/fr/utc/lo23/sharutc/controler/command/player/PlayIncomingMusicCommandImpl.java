package fr.utc.lo23.sharutc.controler.command.player;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.PlayerService;
import fr.utc.lo23.sharutc.model.domain.Music;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class PlayIncomingMusicCommandImpl implements PlayIncomingMusicCommand {

    private static final Logger log = LoggerFactory.getLogger(PlayIncomingMusicCommandImpl.class);
    private Music mMusic;
    private final PlayerService playerService;

    @Inject
    public PlayIncomingMusicCommandImpl(PlayerService playerService) {
        this.playerService = playerService;
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
        log.info("PlayIncomingMusicCommand ...");

        playerService.updateAndPlayMusic(mMusic);

        log.info("PlayIncomingMusicCommand DONE");
    }
}

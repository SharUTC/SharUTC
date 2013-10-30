package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.domain.Music;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class AddTagCommandImpl implements AddTagCommand {

    private static final Logger log = LoggerFactory.getLogger(AddTagCommandImpl.class);
    private Music mMusic;
    private String mTag;
    private final MusicService musicService;

    @Inject
    public AddTagCommandImpl(MusicService musicService) {
        this.musicService = musicService;
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
    public String getTag() {
        return mTag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTag(String tag) {
        this.mTag = tag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("AddTagCommand ...");
        musicService.addTag(mMusic, mTag);
        log.info("AddTagCommand DONE");
    }
}

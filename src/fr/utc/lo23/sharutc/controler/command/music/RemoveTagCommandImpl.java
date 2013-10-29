package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Music;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class RemoveTagCommandImpl implements RemoveTagCommand {

    private static final Logger log = LoggerFactory
            .getLogger(RemoveTagCommandImpl.class);
    private Music mMusic;
    private String mTag;

    /**
     * {@inheritDoc}
     */
    public RemoveTagCommandImpl() {
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
        log.info("RemoveTagCommand ...");
        mMusic.removeTag(mTag);
        log.info("RemoveTagCommand DONE");
    }
}

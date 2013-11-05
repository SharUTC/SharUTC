package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Music;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class AddTagCommandImpl implements AddTagCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddTagCommandImpl.class);
    private Music music;
    private String tag;

    /**
     *
     */
    public AddTagCommandImpl() {
    }

    /**
     *
     * @return
     */
    @Override
    public Music getMusic() {
        return music;
    }

    /**
     *
     * @param music
     */
    @Override
    public void setMusic(Music music) {
        this.music = music;
    }

    /**
     *
     * @return
     */
    @Override
    public String getTag() {
        return tag;
    }

    /**
     *
     * @param tag
     */
    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     *
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }
}
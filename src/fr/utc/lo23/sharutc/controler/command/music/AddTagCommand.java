package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;

/**
 *
 */
public interface AddTagCommand extends Command {

    /**
     *
     * @return
     */
    public Music getMusic();

    /**
     *
     * @param music
     */
    public void setMusic(Music music);

    /**
     *
     * @return
     */
    public String getTag();

    /**
     *
     * @param tag
     */
    public void setTag(String tag);
}

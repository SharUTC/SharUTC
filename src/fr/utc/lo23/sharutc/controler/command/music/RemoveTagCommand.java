package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;

/**
 * Removes a tag from a music, local usage ONLY
 *
 */
public interface RemoveTagCommand extends Command {

    /**
     * Return the music to edit
     *
     * @return the music to edit
     */
    public Music getMusic();

    /**
     * Set the music to edit
     *
     * @param music eturn the music to edit
     */
    public void setMusic(Music music);

    /**
     * Return the tag to add
     *
     * @return the tag to add
     */
    public String getTag();

    /**
     * Set the tag to add
     *
     * @param tag the tag to add
     */
    public void setTag(String tag);
}

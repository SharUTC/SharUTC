package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;

/**
 * Add a new tag to a music, local usage ONLY. If the tag already exists,
 * nothing happens, else view is informed of the change
 *
 * Tags must contain at least 1 character. All tag are formatted following
 * 'Xxxxx' (first letter to uppercase, others to lowercase, to have better
 * results when searching by tag
 */
public interface AddTagCommand extends Command {

    /**
     * Return the music to which the tag is added
     *
     * @return the music to which the tag is added
     */
    public Music getMusic();

    /**
     * Set the music to which the tag is added
     *
     * @param music the music to which the tag is added
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

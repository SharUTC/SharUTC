package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Category;

/**
 * Add a music in specific category
 *
 */
public interface AddMusicToCategoryCommand extends Command {

    /**
     * Return the category
     *
     * @return the category
     */
    public Category getCategory();

    /**
     * Set the category
     *
     * @param category
     */
    public void setCategory(Category category);

    /**
     * Return the music
     *
     * @return the music
     */
    public Music getMusic();

    /**
     * Set the music
     *
     * @param music
     */
    public void setMusic(Music music);
}

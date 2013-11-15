/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Category;

/**
 *
 * @author Arnaud
 */
public interface RemoveMusicFromCategoryCommand {
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
     *
     * @return  the music
     */
    public Music getMusic();
  
    /**     
     * Set the music
     *
     * @param music
     */

    public void setMusic(Music music);
}

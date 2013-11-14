/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Amandine
 */
public class RemoveMusicFromCategoryCommandImpl {
    private static final Logger log= LoggerFactory
            .getLogger(RemoveMusicFromCategoryCommandImpl.class);
    private Category mCategory;
    private Music mMusic;
    final private MusicService mMusicService;
    
    /**
     * {@inheritDoc}
     */
    @Inject
    public RemoveMusicFromCategoryCommandImpl(MusicService mMusicService) {
        this.mMusicService = mMusicService;
    }
    
     /**
     * {@inheritDoc}
     */
    
    public Category getCategory() {
        return mCategory;
    }
    
     /**
     * {@inheritDoc}
     */

    public void setCategory(Category category) {
        this.mCategory = category;
    }
    
    
     /**
     * {@inheritDoc}
     * 
     */
  
    public Music getMusic() {
        return mMusic;
    }
    
    /**
     * {@inheritDoc}
     */
   
    public void setMusic(Music music) {
        this.mMusic = music;
    }
    
    /**
     * {@inheritDoc}
     */
    public void execute() {
        log.info("RemoveMusicFromCategoryCommand...");
        mMusicService.removeMusicFromCategory(mMusic,mCategory);
        log.info("RemoveMusicFromCategoryCommand DONE");
    }
    
}

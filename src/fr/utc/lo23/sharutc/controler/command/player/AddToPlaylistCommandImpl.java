/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.lo23.sharutc.controler.command.player;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.PlayerService;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class AddToPlaylistCommandImpl implements AddToPlaylistCommand{
    
    private static final Logger log = LoggerFactory
            .getLogger(AddToPlaylistCommandImpl.class);
    private final PlayerService playerService;
    
    @Inject
    public AddToPlaylistCommandImpl(PlayerService playerService) {
        this.playerService = playerService;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Music> getMusics(){
        return playerService.getPlaylist().getMusics();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMusics(List<Music> musics){
        playerService.getPlaylist().setMusics(musics);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setMusic(Music music){
        List<Music> musics = new ArrayList<Music>();
        musics.add(music);
        playerService.getPlaylist().setMusics(musics);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("AddToPlaylistCommand...");
        
        for(Music m : this.getMusics()){
            playerService.addToPlaylist(m);
        }

        log.info("AddToPlaylistCommand DONE");
    }
}

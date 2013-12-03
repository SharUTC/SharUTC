/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.collections.ObservableList;

/**
 *
 * @author Florian
 */
public class PlayListMusic  {
    
    public Music music;
    private boolean played;
    
    public PlayListMusic(Music music){
        this.music = music;
        this.played = false;
    }

    public void setPlaying(boolean b) {
        played = b;
    }
    public boolean isPlaying(){
        return played;
    }
}

package fr.utc.lo23.sharutc.controler.command.player;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.util.List;

/**
 * Used to add music to the Playlist
 */
public interface AddToPlaylistCommand extends Command {

    /**
     * Return the music to add 
     * 
     * @return the music to add
     */
    public List<Music> getMusics();

    /**
     * Set the music to add (erase previous list in parameter)
     * 
     * @param musics to add
     */
    public void setMusics(List<Music> musics);
    
    /**
     * Add one music to the previous list to add (no erase of the previous list)
     * 
     * @param music to add
     */
    public void setMusic(Music music);
}

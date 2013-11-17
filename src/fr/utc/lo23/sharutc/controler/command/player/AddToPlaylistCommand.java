package fr.utc.lo23.sharutc.controler.command.player;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.util.List;

/**
 * Used to add music to the Playlist
 */
public interface AddToPlaylistCommand extends Command {

    /**
     *
     * @return
     */
    public List<Music> getMusics();

    /**
     *
     * @param musics
     */
    public void setMusics(List<Music> musics);
    
    /**
     *
     * @param music
     */
    public void setMusic(Music music);
}

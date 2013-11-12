package fr.utc.lo23.sharutc.controler.command.player;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.util.List;

/**
 * Used to remove music from the Playlist
 */
public interface RemoveFromPlaylistCommand extends Command {

    /**
     * Return the musics to remove
     *
     * @return the musics to remove
     */
    public List<Music> getMusics();

    /**
     * Set several musics to remove (erase previous list in parameter)
     *
     * @param musics a list of musicss to remove
     */
    public void setMusics(List<Music> musics);

    /**
     * Set a music to be removed, may be used several times for one command
     *
     * @param music a music to remove
     */
    public void setMusic(Music music);
}

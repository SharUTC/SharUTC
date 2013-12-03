package fr.utc.lo23.sharutc.controler.command.player;

import fr.utc.lo23.sharutc.controler.command.Command;
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
    public List<Integer> getMusicsIndex();

    /**
     * Set several musics to remove (erase previous list in parameter)
     *
     * @param musics a list of musicss to remove
     */
    public void setMusicsIndex(List<Integer> musics);

    /**
     * Set a music to be removed, may be used several times for one command
     *
     * @param music a music to remove
     */
    public void setMusicIndex(Integer music);
}

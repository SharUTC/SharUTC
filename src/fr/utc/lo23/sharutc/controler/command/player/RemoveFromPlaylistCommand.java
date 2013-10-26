package fr.utc.lo23.sharutc.controler.command.player;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.util.List;

/**
 *
 */
public interface RemoveFromPlaylistCommand extends Command {

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
}

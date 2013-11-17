package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.util.Collection;

/**
 *
 */
public interface RemoveFromLocalCatalogCommand extends Command {

    /**
     *
     * @return
     */
    public Collection<Music> getMusics();

    /**
     *
     * @param musics
     */
    public void setMusics(Collection<Music> musics);
}

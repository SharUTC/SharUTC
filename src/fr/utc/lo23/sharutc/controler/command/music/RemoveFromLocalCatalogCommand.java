package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.util.Collection;

/**
 *
 * Command for removing musics from user's local catalog.
 * 
 */
public interface RemoveFromLocalCatalogCommand extends Command {

    /**
     *
     * Gets the collection of musics which is set to be removed from catalog when the command is executed
     * 
     * @return
     */
    public Collection<Music> getMusics();

    /**
     *
     * Sets the collection of musics which is set to be removed from catalog when the command is executed
     * 
     * @param musics
     */
    public void setMusics(Collection<Music> musics);
}

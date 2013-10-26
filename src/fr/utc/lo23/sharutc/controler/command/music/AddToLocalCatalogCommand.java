package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import java.io.File;
import java.util.Collection;

/**
 *
 */
public interface AddToLocalCatalogCommand extends Command {

    /**
     *
     * @return
     */
    public Collection<File> getFiles();

    /**
     *
     * @param files
     */
    public void setFiles(Collection<File> files);
}

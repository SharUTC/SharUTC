package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import java.io.File;
import java.util.Collection;

/**
 *
 * Command for adding mp3 files to user's local catalog.
 *
 */
public interface AddToLocalCatalogCommand extends Command {

    /**
     *
     * Gets the collection of files which will be added to local catalog when
     * the command is executed.
     *
     * @return
     */
    public Collection<File> getFiles();

    /**
     *
     * Sets the collection of files which will be added to local catalog when
     * the command is executed.
     *
     * @param files
     */
    public void setFiles(Collection<File> files);
}

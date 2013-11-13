package fr.utc.lo23.sharutc.controler.command.search;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Catalog;

/**
 * Install the file contained in the music as a new local file
 */
public interface InstallRemoteMusicsCommand extends Command {

    /**
     * Return the catalog of music to save on hard drive disk
     *
     * @return the catalog of music to save on hard drive disk
     */
    public Catalog getCatalog();

    /**
     * Set the catalog of music to save on hard drive disk
     *
     * @param catalog the catalog of music to save on hard drive disk
     */
    public void setCatalog(Catalog catalog);
}

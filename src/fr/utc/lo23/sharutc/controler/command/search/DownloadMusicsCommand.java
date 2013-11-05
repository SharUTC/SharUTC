package fr.utc.lo23.sharutc.controler.command.search;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Catalog;

/**
 * Send a request to each owner of the file to download it
 */
public interface DownloadMusicsCommand extends Command {

    /**
     * Return all the musics the user wants to download
     *
     * @return all the musics the user wants to download
     */
    public Catalog getCatalog();

    /**
     * Set all the musics the user wants to download
     *
     * @param catalog all the musics the user wants to download
     */
    public void setCatalog(Catalog catalog);
}

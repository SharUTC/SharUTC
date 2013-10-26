package fr.utc.lo23.sharutc.controler.command.search;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Catalog;

/**
 *
 */
public interface InstallRemoteMusicsCommand extends Command {

    /**
     *
     * @return
     */
    public Catalog getCatalog();

    /**
     *
     * @param catalog
     */
    public void setCatalog(Catalog catalog);
}

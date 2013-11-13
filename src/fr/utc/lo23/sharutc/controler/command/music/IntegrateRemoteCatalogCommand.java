package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 *
 */
public interface IntegrateRemoteCatalogCommand extends Command {

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

    /**
     *
     * @return
     */
    public Peer getPeer();

    /**
     *
     * @param peer
     */
    public void setPeer(Peer peer);
}

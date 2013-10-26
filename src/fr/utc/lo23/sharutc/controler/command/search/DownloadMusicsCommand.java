package fr.utc.lo23.sharutc.controler.command.search;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 *
 */
public interface DownloadMusicsCommand extends Command {

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

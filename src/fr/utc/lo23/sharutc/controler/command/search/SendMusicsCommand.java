package fr.utc.lo23.sharutc.controler.command.search;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 * Use the local data to load file of musics and send them to another peer
 */
public interface SendMusicsCommand extends Command {

    /**
     * Return the destination Peer
     *
     * @return the destination Peer
     */
    public Peer getPeer();

    /**
     * Set the destination Peer
     *
     * @param peer the destination Peer
     */
    public void setPeer(Peer peer);

    /**
     * Return the catalog to send
     *
     * @return the catalog to send
     */
    public Catalog getCatalog();

    /**
     * Set the catalog to send
     *
     * @param catalog the catalog to send
     */
    public void setCatalog(Catalog catalog);
}

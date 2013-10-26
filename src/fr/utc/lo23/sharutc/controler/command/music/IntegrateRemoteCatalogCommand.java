package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Catalog;

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
    public Long getPeerId();

    /**
     *
     * @param peerId
     */
    public void setPeerId(Long peerId);
}

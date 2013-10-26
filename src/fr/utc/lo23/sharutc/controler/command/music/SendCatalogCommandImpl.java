package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SendCatalogCommandImpl implements SendCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(SendCatalogCommandImpl.class);
    private Peer peer;
    private Catalog catalog;

    /**
     *
     */
    public SendCatalogCommandImpl() {
    }

    /**
     *
     * @return
     */
    @Override
    public Peer getPeer() {
        return peer;
    }

    /**
     *
     * @param peer
     */
    @Override
    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    /**
     *
     * @return
     */
    @Override
    public Catalog getCatalog() {
        return catalog;
    }

    /**
     *
     * @param catalog
     */
    @Override
    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    /**
     *
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }
}

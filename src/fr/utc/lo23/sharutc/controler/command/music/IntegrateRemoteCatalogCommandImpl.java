package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class IntegrateRemoteCatalogCommandImpl implements IntegrateRemoteCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(IntegrateRemoteCatalogCommandImpl.class);
    private Catalog catalog;
    private Long peerId;

    /**
     *
     */
    public IntegrateRemoteCatalogCommandImpl() {
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
     * @return
     */
    @Override
    public Long getPeerId() {
        return peerId;
    }

    /**
     *
     * @param peerId
     */
    @Override
    public void setPeerId(Long peerId) {
        this.peerId = peerId;
    }

    /**
     *
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }
}

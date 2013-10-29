package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class IntegrateRemoteCatalogCommandImpl implements IntegrateRemoteCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(IntegrateRemoteCatalogCommandImpl.class);
    private Catalog mCatalog;
    private Long mPeerId;

    /**
     * {@inheritDoc}
     */
    public IntegrateRemoteCatalogCommandImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return mCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCatalog(Catalog catalog) {
        this.mCatalog = catalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getPeerId() {
        return mPeerId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPeerId(Long peerId) {
        this.mPeerId = peerId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }
}

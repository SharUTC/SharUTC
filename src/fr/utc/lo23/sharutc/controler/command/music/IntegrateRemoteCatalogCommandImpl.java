package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class IntegrateRemoteCatalogCommandImpl implements IntegrateRemoteCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(IntegrateRemoteCatalogCommandImpl.class);
    private final MusicService musicService;
    private Catalog mCatalog;
    private Peer mPeer;

    /**
     * {@inheritDoc}
     */
    public IntegrateRemoteCatalogCommandImpl(MusicService musicService) {
        this.musicService = musicService;
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
    public Peer getPeer() {
        return mPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPeer(Peer peer) {
        this.mPeer = peer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("IntegrateRemoteCatalogCommand ...");
        musicService.integrateRemoteCatalog(mPeer, mCatalog);
        log.info("IntegrateRemoteCatalogCommand DONE");
    }
}
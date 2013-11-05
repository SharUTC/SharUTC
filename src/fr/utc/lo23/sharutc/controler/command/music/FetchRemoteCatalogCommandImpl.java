package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class FetchRemoteCatalogCommandImpl implements FetchRemoteCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(FetchRemoteCatalogCommandImpl.class);
    private final NetworkService networkService;
    private Peer mPeer;

    /**
     * {@inheritDoc}
     */
    @Inject
    public FetchRemoteCatalogCommandImpl(NetworkService networkService) {
        this.networkService = networkService;
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
        log.info("FetchRemoteCatalogCommand ...");
        networkService.sendUnicastGetCatalog(mPeer);
        log.info("FetchRemoteCatalogCommand DONE");
    }
}

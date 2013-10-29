package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class FetchRemoteCatalogCommandImpl implements FetchRemoteCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(FetchRemoteCatalogCommandImpl.class);
    private Peer mPeer;

    /**
     * {@inheritDoc}
     */
    public FetchRemoteCatalogCommandImpl() {
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
        log.warn("Not supported yet.");
    }
}

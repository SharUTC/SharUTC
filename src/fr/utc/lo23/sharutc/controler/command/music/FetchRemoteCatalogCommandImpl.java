package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class FetchRemoteCatalogCommandImpl implements FetchRemoteCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(FetchRemoteCatalogCommandImpl.class);
    private Peer peer;

    /**
     *
     */
    public FetchRemoteCatalogCommandImpl() {
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
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }
}

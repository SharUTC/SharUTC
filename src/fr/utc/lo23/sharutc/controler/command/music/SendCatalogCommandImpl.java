package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class SendCatalogCommandImpl implements SendCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(SendCatalogCommandImpl.class);
    private final NetworkService networkService;
    private Peer mPeer;
    private Catalog mCatalog;
    private Long mConversationId;

    /**
     * {@inheritDoc}
     */
    @Inject
    public SendCatalogCommandImpl(NetworkService networkService) {
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
    public Catalog getCatalog() {
        return mCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getConversationId() {
        return mConversationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConversationId(Long conversationId) {
        this.mConversationId = conversationId;
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
    public void execute() {
        log.info("SendCatalogCommand ...");
        networkService.sendUnicastCatalog(mPeer, mConversationId, mCatalog);
        log.info("SendCatalogCommand DONE");
    }
}

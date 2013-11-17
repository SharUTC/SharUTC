package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
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
    private final MusicService musicService;
    private Peer mPeer;
    private Long mConversationId;

    /**
     * {@inheritDoc}
     */
    @Inject
    public SendCatalogCommandImpl(NetworkService networkService, MusicService musicService) {
        this.networkService = networkService;
        this.musicService = musicService;
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
    public void execute() {
        log.info("SendCatalogCommand ...");
        Catalog mCatalog = musicService.getCatalogForPeer(mPeer); 
        
        // FIXME : get local catalog and filter results like perform search command
        // a support class to factorize filter might be nice
        networkService.sendUnicastCatalog(mPeer, mConversationId, mCatalog);
        log.info("SendCatalogCommand DONE");
    }
}

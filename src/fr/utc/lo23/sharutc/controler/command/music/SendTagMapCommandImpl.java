package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class SendTagMapCommandImpl implements SendTagMapCommand {

    private static final Logger log = LoggerFactory
            .getLogger(SendTagMapCommandImpl.class);
    private final NetworkService networkService;
    private final AppModel appModel;
    private Peer mPeer;
    private Long mConversationId;

    @Inject
    public SendTagMapCommandImpl(AppModel appModel, NetworkService networkService) {
        this.appModel = appModel;
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
        log.info("SendTagMapCommand ...");

        // getting local data
        TagMap tagMap = new TagMap(appModel.getLocalCatalog());

        // sending TagMap response in unicast
        networkService.sendUnicastTagMap(mPeer, mConversationId, tagMap);

        log.info("SendTagMapCommand DONE");
    }
}

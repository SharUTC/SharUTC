package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Compute local TagMap and send it to required peer
 */
public class SendTagMapCommandImpl implements SendTagMapCommand {

    private static final Logger log = LoggerFactory
            .getLogger(SendTagMapCommandImpl.class);
    private final NetworkService networkService;
    private final AppModel appModel;
    private Peer peer;
    private Long conversationId;

    /**
     * AppModel is injected
     *
     * @param appModel the local instance of AppModel
     * @param networkService the local instance of NetworkService
     */
    @Inject
    public SendTagMapCommandImpl(AppModel appModel, NetworkService networkService) {
        this.appModel = appModel;
        this.networkService = networkService;
    }

    /**
     *
     * @return the destination Peer
     */
    @Override
    public Peer getPeer() {
        return peer;
    }

    /**
     *
     * @param peer the destination Peer
     */
    @Override
    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    @Override
    public Long getConversationId() {
        return conversationId;
    }

    @Override
    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    /**
     * Compute local TagMap and send it to required peer
     */
    @Override
    public void execute() {
        log.info("SendTagMapCommand ...");

        // getting local data
        TagMap tagMap = new TagMap(appModel.getLocalCatalog());

        // sending TagMap response in unicast
        networkService.sendUnicastTagMap(peer, conversationId, tagMap);

        log.info("SendTagMapCommand DONE");
    }
}

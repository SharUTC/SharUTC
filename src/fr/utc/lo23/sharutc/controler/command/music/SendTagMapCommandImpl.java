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
    @Inject
    private NetworkService networkService;
    @Inject
    private AppModel appModel;
    private Peer peer;

    /**
     *
     */
    public SendTagMapCommandImpl() {
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
        log.info("SendTagMapCommand ...");

        // getting local data
        TagMap tagMap = new TagMap(appModel.getLocalCatalog());

        // sending TagMap response in unicast
        networkService.sendUnicastTagMap(peer, tagMap);

        log.info("SendTagMapCommand DONE");
    }
}

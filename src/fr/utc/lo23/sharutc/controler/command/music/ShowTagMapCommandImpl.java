package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clean the network TagMap, add local music tags and ask other peers's TagMap
 * in a broadcast request
 * uses a new CONVERSATION ID
 */
public class ShowTagMapCommandImpl implements ShowTagMapCommand {

    private static final Logger log = LoggerFactory
            .getLogger(ShowTagMapCommandImpl.class);
    @Inject
    private NetworkService networkService;
    @Inject
    private AppModel appModel;

    @Override
    public void execute() {
        log.info("ShowTagMapCommand ...");
        
        // cleaning  old TagMap
        appModel.getNetworkTagMap().clear();
        
        // loading local data
        TagMap tagMap = new TagMap(appModel.getLocalCatalog());
        
        // sending broadcast request for peer's TagMap (CONVERSATION ID required inside message)
        networkService.sendBroadcastGetTagMap();
        
        log.info("ShowTagMapCommand DONE");
    }
}

package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clean the network TagMap, add local music tags and ask other peers's TagMap
 * in a broadcast request uses a new CONVERSATION ID
 */
public class ShowTagMapCommandImpl implements ShowTagMapCommand {

    private static final Logger log = LoggerFactory
            .getLogger(ShowTagMapCommandImpl.class);
    private final NetworkService networkService;
    private final AppModel appModel;

    /**
     * AppModel is injected
     *
     * @param appModel the local instance of AppModel
     * @param networkService the local instance of NetworkService
     */
    @Inject
    public ShowTagMapCommandImpl(AppModel appModel, NetworkService networkService) {
        this.appModel = appModel;
        this.networkService = networkService;
    }

    /**
     * Clean the network TagMap, add local music tags and ask other peers's
     * TagMap in a broadcast request uses a new CONVERSATION ID
     */
    @Override
    public void execute() {
        log.info("ShowTagMapCommand ...");

        // cleaning  old TagMap
        appModel.getNetworkTagMap().clear();

        // loading local data
        TagMap tagMap = new TagMap(appModel.getLocalCatalog());
        appModel.getNetworkTagMap().merge(tagMap);

        // sending broadcast request for peer's TagMap (CONVERSATION ID required inside message)
        networkService.sendBroadcastGetTagMap();

        log.info("ShowTagMapCommand DONE");
    }
}

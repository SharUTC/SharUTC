package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class ShowTagMapCommandImpl implements ShowTagMapCommand {

    private static final Logger log = LoggerFactory
            .getLogger(ShowTagMapCommandImpl.class);
    private final NetworkService networkService;
    private final AppModel appModel;

    @Inject
    public ShowTagMapCommandImpl(AppModel appModel, NetworkService networkService) {
        this.appModel = appModel;
        this.networkService = networkService;
    }

    /**
     * {@inheritDoc}
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

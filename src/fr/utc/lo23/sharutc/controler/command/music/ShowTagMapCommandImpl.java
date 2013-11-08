package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
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
    private final MusicService musicService;
    private final AppModel appModel;

    @Inject
    public ShowTagMapCommandImpl(AppModel appModel, MusicService musicService, NetworkService networkService) {
        this.appModel = appModel;
        this.musicService = musicService;
        this.networkService = networkService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("ShowTagMapCommand ...");

        // cleaning  old NetworkTagMap, updates ui
        appModel.getNetworkTagMap().clear();

        // loading local data
        TagMap tagMap = musicService.getLocalTagMap();
        appModel.getNetworkTagMap().merge(tagMap);

        // sending broadcast request for peer's TagMap (CONVERSATION ID required inside message)
        networkService.sendBroadcastGetTagMap();

        log.info("ShowTagMapCommand DONE");
    }
}

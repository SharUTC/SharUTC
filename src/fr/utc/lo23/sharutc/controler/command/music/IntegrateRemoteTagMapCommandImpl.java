package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Merge TagMap received from Peers
 */
public class IntegrateRemoteTagMapCommandImpl implements IntegrateRemoteTagMapCommand {

    private static final Logger log = LoggerFactory
            .getLogger(IntegrateRemoteTagMapCommandImpl.class);
    private final AppModel appModel;
    private TagMap tagMap;

    /**
     * AppModel is injected
     *
     * @param appModel the local instance of AppModel
     */
    @Inject
    public IntegrateRemoteTagMapCommandImpl(AppModel appModel) {
        this.appModel = appModel;
    }

    /**
     *
     * @return the TagMap received from a peer
     */
    @Override
    public TagMap getTagMap() {
        return tagMap;
    }

    /**
     *
     * @param tagMap the TagMap received from a peer
     */
    @Override
    public void setTagMap(TagMap tagMap) {
        this.tagMap = tagMap;
    }

    /**
     * Merge TagMap received from Peers
     */
    @Override
    public void execute() {
        log.info("IntegrateRemoteTagMapCommand ...");

        // merging remote and local TagMap in local TagMap
        appModel.getNetworkTagMap().merge(tagMap);

        log.info("IntegrateRemoteTagMapCommand DONE");
    }
}

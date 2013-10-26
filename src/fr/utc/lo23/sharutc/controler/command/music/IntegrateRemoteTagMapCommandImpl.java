package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class IntegrateRemoteTagMapCommandImpl implements IntegrateRemoteTagMapCommand {

    private static final Logger log = LoggerFactory
            .getLogger(IntegrateRemoteTagMapCommandImpl.class);
    private TagMap tagMap;
    
    @Inject
    private AppModel appModel;

    /**
     *
     */
    public IntegrateRemoteTagMapCommandImpl() {
    }

    /**
     *
     * @return
     */
    @Override
    public TagMap getTagMap() {
        return tagMap;
    }

    /**
     *
     * @param tagMap
     */
    @Override
    public void setTagMap(TagMap tagMap) {
        this.tagMap = tagMap;
    }

    @Override
    public void execute() {
        log.info("IntegrateRemoteTagMapCommand ...");
        // merging remote and local TagMap in local TagMap
        appModel.getNetworkTagMap().merge(tagMap);
        log.info("IntegrateRemoteTagMapCommand DONE");
    }
}

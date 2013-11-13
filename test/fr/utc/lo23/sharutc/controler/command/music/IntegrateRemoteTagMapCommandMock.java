package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.TagMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class IntegrateRemoteTagMapCommandMock implements IntegrateRemoteTagMapCommand {

    private static final Logger log = LoggerFactory
            .getLogger(IntegrateRemoteTagMapCommandMock.class);

    /**
     *
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }

    /**
     *
     * @param tagMap
     */
    @Override
    public void setTagMap(TagMap tagMap) {
        log.warn("Not supported yet.");
    }

    /**
     *
     * @return
     */
    @Override
    public TagMap getTagMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

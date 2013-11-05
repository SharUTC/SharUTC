package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class IntegrateRemoteTagMapCommandImpl implements IntegrateRemoteTagMapCommand {

    private static final Logger log = LoggerFactory
            .getLogger(IntegrateRemoteTagMapCommandImpl.class);
    private final MusicService musicService;
    private TagMap mTagMap;

    @Inject
    public IntegrateRemoteTagMapCommandImpl(MusicService musicService) {
        this.musicService = musicService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagMap getTagMap() {
        return mTagMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTagMap(TagMap tagMap) {
        this.mTagMap = tagMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("IntegrateRemoteTagMapCommand ...");
        musicService.integrateRemoteTagMap(mTagMap);
        log.info("IntegrateRemoteTagMapCommand DONE");
    }
}

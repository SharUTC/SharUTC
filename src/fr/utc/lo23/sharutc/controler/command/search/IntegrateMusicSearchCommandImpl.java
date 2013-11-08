package fr.utc.lo23.sharutc.controler.command.search;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class IntegrateMusicSearchCommandImpl implements IntegrateMusicSearchCommand {

    private static final Logger log = LoggerFactory.getLogger(IntegrateMusicSearchCommandImpl.class);
    private Catalog mCatalog;
    private final MusicService musicService;

    /**
     * {@inheritDoc}
     */
    @Inject
    public IntegrateMusicSearchCommandImpl(MusicService musicService) {
        this.musicService = musicService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResultsCatalog(Catalog catalog) {
        this.mCatalog = catalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getResultsCatalog() {
        return mCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("IntegrateMusicSearchCommand ...");

        musicService.integrateMusicSearch(mCatalog);

        log.info("IntegrateMusicSearchCommand DONE");
    }
}

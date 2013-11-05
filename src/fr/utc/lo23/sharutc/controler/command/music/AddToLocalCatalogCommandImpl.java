package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.service.MusicService;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class AddToLocalCatalogCommandImpl implements AddToLocalCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddToLocalCatalogCommandImpl.class);
    private final MusicService musicService;
    private Collection<File> mFiles;

    /**
     * {@inheritDoc}
     */
    public AddToLocalCatalogCommandImpl(MusicService musicService) {
        this.musicService = musicService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<File> getFiles() {
        return Collections.unmodifiableCollection(mFiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFiles(Collection<File> files) {
        this.mFiles = files;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("AddToLocalCatalogCommand ...");
        musicService.addToLocalCatalog(mFiles);
        log.info("AddToLocalCatalogCommand DONE");
    }
}

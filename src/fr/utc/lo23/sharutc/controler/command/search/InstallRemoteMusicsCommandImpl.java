package fr.utc.lo23.sharutc.controler.command.search;

import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class InstallRemoteMusicsCommandImpl implements InstallRemoteMusicsCommand {

    private static final Logger log = LoggerFactory.getLogger(InstallRemoteMusicsCommandImpl.class);
    private final MusicService musicService;
    private Catalog mCatalog;

    public InstallRemoteMusicsCommandImpl(MusicService musicService) {
        this.musicService = musicService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return mCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCatalog(Catalog catalog) {
        this.mCatalog = catalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("InstallRemoteMusicsCommand ...");
        
        // copying all musics from catalog to local data stored on hard drive
        // reads Byte[] and transform it back into a mp3 file
        musicService.installMusics(mCatalog);
        
        log.info("InstallRemoteMusicsCommand DONE");
    }
}

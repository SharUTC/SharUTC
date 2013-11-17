package fr.utc.lo23.sharutc.controler.command.search;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendMusicsCommandImpl implements SendMusicsCommand {

    private static final Logger log = LoggerFactory
            .getLogger(SendMusicsCommandImpl.class);
    private final MusicService musicService;
    private final NetworkService networkService;
    private Peer mPeer;
    private Catalog mCatalog;

    @Inject
    public SendMusicsCommandImpl(MusicService musicService, NetworkService networkService) {
        this.musicService = musicService;
        this.networkService = networkService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Peer getPeer() {
        return mPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPeer(Peer peer) {
        this.mPeer = peer;
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
        log.info("SendMusicsCommand ...");

        // loading mp3 files inside music as Byte[]
        musicService.loadMusicFiles(mCatalog);

        // sending whole catalog to peer
        networkService.sendMusics(mPeer, mCatalog);

        log.info("SendMusicsCommand DONE");
    }
}

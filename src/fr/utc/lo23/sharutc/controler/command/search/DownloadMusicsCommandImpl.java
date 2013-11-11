package fr.utc.lo23.sharutc.controler.command.search;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class DownloadMusicsCommandImpl implements DownloadMusicsCommand {

    private static final Logger log = LoggerFactory
            .getLogger(DownloadMusicsCommandImpl.class);
    private final AppModel appModel;
    private final NetworkService networkService;
    private final MusicService musicService;
    private Catalog mCatalog;

    @Inject
    public DownloadMusicsCommandImpl(AppModel appModel, NetworkService networkService, MusicService musicService) {
        this.appModel = appModel;
        this.networkService = networkService;
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
        log.info("DownloadMusicsCommand ...");

        boolean musicWithHashMoved = false;
        for (Music music : mCatalog.getMusics()) {
            // removing the musics where user doesn't have the right to download (=listen)
            if (music.getMayListen() == null || music.getMayListen() == false) {
                mCatalog.remove(music);
            }
            // removing local musics from download request
            if (music.getOwnerPeerId().equals(appModel.getProfile().getUserInfo().getPeerId())) {
                mCatalog.remove(music);
            }
            // checking temporary catalog to avoid useless transfer
            Music musicWithSameHash = appModel.getTmpCatalog().findMusicByHash(music.getHash());
            if (musicWithSameHash != null && musicWithSameHash.getFileBytes() != null) {
                mCatalog.remove(music);
                //add music to local catalog and put it in local catalog
                appModel.getLocalCatalog().add(musicWithSameHash);
                appModel.getTmpCatalog().remove(musicWithSameHash);
                musicWithHashMoved = true;
            }
        }

        if (musicWithHashMoved) {
            musicService.saveUserMusicFile();
        }

        // splitting catalog following owner of each music
        Map<Long, List<Music>> ownedMusics = new HashMap<Long, List<Music>>();
        for (Music music : mCatalog.getMusics()) {
            if (ownedMusics.containsKey(music.getOwnerPeerId())) {
                ownedMusics.get(music.getOwnerPeerId()).add(music);
            } else {
                List<Music> newList = new ArrayList<Music>();
                newList.add(music);
                ownedMusics.put(music.getOwnerPeerId(), newList);
            }
        }

        // sending each list of musics in a separate message
        for (Map.Entry<Long, List<Music>> entry : ownedMusics.entrySet()) {
            Peer targetPeer = appModel.getActivePeerList().getByPeerId(entry.getKey());
            Catalog catalog = new Catalog();
            catalog.addAll(entry.getValue());
            log.debug("Sending download request to {} ({} file(s)", targetPeer.getDisplayName(), catalog.size());
            networkService.sendDownloadRequest(targetPeer, catalog);
        }

        log.info("DownloadMusicsCommand DONE");
    }
}

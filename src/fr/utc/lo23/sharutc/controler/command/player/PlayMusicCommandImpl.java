package fr.utc.lo23.sharutc.controler.command.player;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.PlayerService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class PlayMusicCommandImpl implements PlayMusicCommand {

    private static final Logger log = LoggerFactory.getLogger(PlayMusicCommandImpl.class);
    private Music mMusic;
    private final AppModel appModel;
    private final PlayerService playerService;
    private final NetworkService networkService;

    @Inject
    public PlayMusicCommandImpl(AppModel appModel, NetworkService networkService, PlayerService playerService) {
        this.appModel = appModel;
        this.playerService = playerService;
        this.networkService = networkService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Music getMusic() {
        return mMusic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMusic(Music music) {
        this.mMusic = music;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("PlayMusicCommand ...");
        if (mMusic.getOwnerPeerId().equals(appModel.getProfile().getUserInfo().getPeerId())) {
            // file is local
            playerService.playerPlay();
        } else {
            // file is remote, music.id belongs to the remote peer
            Peer peer = appModel.getActivePeerList().getByPeerId(mMusic.getOwnerPeerId());
            networkService.downloadMusicForPlaying(peer, mMusic.getId());
        }
        log.info("PlayMusicCommand DONE");
    }
}

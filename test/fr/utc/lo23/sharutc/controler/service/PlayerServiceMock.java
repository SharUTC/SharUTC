package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Singleton
public class PlayerServiceMock extends PlayerServiceImpl implements PlayerService {

    private static final Logger log = LoggerFactory
            .getLogger(PlayerServiceMock.class);

    @Inject
    public PlayerServiceMock(AppModel appModel, FileService fileService, MusicService musicService, NetworkService networkService) {
        super(appModel, fileService, musicService, networkService);
    }

    @Override
    public void addToPlaylist(Music music) {
        super.addToPlaylist(music);
    }

    @Override
    public void removeFromPlaylist(Integer musicIndex) {
        super.removeFromPlaylist(musicIndex);
    }

    @Override
    public void playOneMusic(Music music) {
        super.playOneMusic(music);
    }

    @Override
    public void updateAndPlayMusic(Music music) {
        super.updateAndPlayMusic(music);
    }

    @Override
    public void playerPlay() {
        super.playerPlay();
    }

    @Override
    public void playerStop() {
        super.playerStop();
    }

    @Override
    public void playerNext() {
        super.playerNext();
    }

    @Override
    public void playerPrevious() {
        super.playerPrevious();
    }
}

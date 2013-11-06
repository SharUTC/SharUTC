package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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
    public PlayerServiceMock(AppModel appModel, FileService fileService) {
        super(appModel, fileService);
    }

    @Override
    public void addToPlaylist(Music music) {
        super.addToPlaylist(music);
    }

    @Override
    public void removeFromPlaylist(Music music) {
        super.removeFromPlaylist(music);
    }

    @Override
    public void play(Music music) {
        super.play(music);
    }

    @Override
    public void updatePlaylist(Music music) {
        super.updatePlaylist(music);
    }

    @Override
    public void playerPlay() {
        super.playerPlay();
    }

    @Override
    public void playerPause() {
        super.playerPause();
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

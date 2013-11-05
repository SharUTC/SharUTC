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
    public PlayerServiceMock(AppModel appModel) {
        super(appModel);
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
    public void play() {
        super.play();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void next() {
        super.next();
    }

    @Override
    public void previous() {
        super.previous();
    }
}

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
public class PlayerServiceImpl implements PlayerService {

    private static final Logger log = LoggerFactory
            .getLogger(PlayerServiceImpl.class);
    @Inject
    AppModel model;

    /**
     *
     * @param music
     */
    @Override
    public void addToPlaylist(Music music) {
        log.warn("Not supported yet.");
    }

    /**
     *
     * @param music
     */
    @Override
    public void removeFromPlaylist(Music music) {
        log.warn("Not supported yet.");
    }

    /**
     *
     * @param music
     */
    @Override
    public void play(Music music) {
        log.warn("Not supported yet.");
    }

    /**
     *
     * @param music
     */
    @Override
    public void updatePlaylist(Music music) {
        log.warn("Not supported yet.");
    }

    /**
     *
     */
    @Override
    public void play() {
        log.warn("Not supported yet.");
    }

    /**
     *
     */
    @Override
    public void pause() {
        log.warn("Not supported yet.");
    }

    /**
     *
     */
    @Override
    public void stop() {
        log.warn("Not supported yet.");
    }

    /**
     *
     */
    @Override
    public void next() {
        log.warn("Not supported yet.");
    }

    /**
     *
     */
    @Override
    public void previous() {
        log.warn("Not supported yet.");
    }
}

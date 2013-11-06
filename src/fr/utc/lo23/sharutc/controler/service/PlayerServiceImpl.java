package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
@Singleton
public class PlayerServiceImpl implements PlayerService {

    private static final Logger log = LoggerFactory
            .getLogger(PlayerServiceImpl.class);
    private final AppModel appModel;
    private final FileService fileService;
    private int pausedOnFrame = 0;
    private final PlaybackListener playbackListener;
    private Catalog mPlaylist = new Catalog();
    private Music mCurrentMusic;
    private AdvancedPlayer mPlayer;
    private boolean playing = false;

    @Inject
    public PlayerServiceImpl(AppModel appModel, FileService fileService) {
        this.appModel = appModel;
        this.fileService = fileService;
        this.playbackListener = new PlaybackListener() {
            @Override
            public void playbackStarted(PlaybackEvent event) {
                super.playbackStarted(event);
            }

            @Override
            public void playbackFinished(PlaybackEvent event) {
                super.playbackFinished(event);
                pausedOnFrame = event.getFrame();
            }
        };
    }

    public Catalog getPlaylist() {
        return mPlaylist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToPlaylist(Music music) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromPlaylist(Music music) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void play(Music music) {
        log.info("play music");
        if (music != null) {
            mPlaylist.clear();
            mPlaylist.add(music);
            mCurrentMusic = music;
            playerPlay();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePlaylist(Music music) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playerPlay() {
        log.info("playerPlay");
        if (mPlayer == null) {
            try {
                File tmpFile = fileService.buildTmpMusicFile(mCurrentMusic.getFileBytes());
                mPlayer = new AdvancedPlayer(new FileInputStream(tmpFile));
                mPlayer.setPlayBackListener(playbackListener);
                pausedOnFrame = 0;
            } catch (Exception ex) {
                log.error(ex.toString());
            }
        }
        if (pausedOnFrame != 0) {
            playAt(pausedOnFrame);
        } else {
            playAt(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playerPause() {
        log.info("playerPause");
        if (mPlayer != null && playing) {
            mPlayer.stop();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playerStop() {
        log.info("playerStop");
        if (mPlayer != null && playing) {
            mPlayer.stop();
            pausedOnFrame = 0;
            playing = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playerNext() {
        log.info("playerNext");
        // if playing then stop
        if (playing) {
            playerStop();
        }
        // set current to next or first
        if (!mPlaylist.isEmpty()) {
            int nextIndex = 0;
            if (mCurrentMusic != null) {
                nextIndex = mPlaylist.indexOf(mCurrentMusic) + 1;
                nextIndex %= mPlaylist.size();
            }
            mCurrentMusic = mPlaylist.get(nextIndex);
        } else {
            // no effect
        }
        // play current
        playerPlay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playerPrevious() {
        log.info("playerPrevious");
        // if playing then stop
        if (playing) {
            playerStop();
        }
        // set current to previous or last
        if (!mPlaylist.isEmpty()) {
            int previousIndex = mPlaylist.size() - 1;
            if (mCurrentMusic != null && mPlaylist.indexOf(mCurrentMusic) > 0) {
                previousIndex = mPlaylist.indexOf(mCurrentMusic) - 1;
            }
            mCurrentMusic = mPlaylist.get(previousIndex);
        } else {
            // no effect
        }
        // play current
        playerPlay();
    }

    private void playAt(Integer pausedOnFrame1) {
        try {
            if (pausedOnFrame1 != null) {
                mPlayer.play(pausedOnFrame1.intValue());
            } else {
                mPlayer.play();
            }
            playing = true;
        } catch (JavaLayerException ex) {
            log.error(ex.toString());
        }
    }
}

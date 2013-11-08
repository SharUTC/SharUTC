package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.controler.player.PlayerEvent;
import fr.utc.lo23.sharutc.controler.player.PlaybackListenerImpl;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
@Singleton
public class PlayerServiceImpl implements PlayerService, PropertyChangeListener {

    private static final Logger log = LoggerFactory
            .getLogger(PlayerServiceImpl.class);
    private final FileService fileService;
    private PlaybackListenerImpl player;
    private Catalog mPlaylist = new Catalog();
    private Music mCurrentMusic;
    private Long mCurrentTimeMs = 0L;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private void onMusicEnd() {
        player = null;
        if (mPlaylist.indexOf(mCurrentMusic) < (mPlaylist.size() - 1)) {
            playerNext();
        } else {
            playerStop();
        }
    }

    private void onCurrentTimeUpdate(Long currentTimeMs) {
        Long oldTimeMs = mCurrentTimeMs;
        this.mCurrentTimeMs = currentTimeMs;
        propertyChangeSupport.firePropertyChange(Property.CURRENT_TIME.name(), oldTimeMs, mCurrentTimeMs);
    }

    public enum Property {

        CURRENT_TIME, SELECTED_MUSIC
    }

    @Inject
    public PlayerServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    public Catalog getPlaylist() {
        return mPlaylist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToPlaylist(Music music) {
        mPlaylist.add(music);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromPlaylist(Music music) {
        mPlaylist.remove(music);
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
            setCurrentMusic(music);
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
        if (player == null) {
            if (mCurrentMusic == null) {
                setCurrentMusic(mPlaylist.get(0));
            }
            if (mCurrentMusic != null) {
                try {
                    File tmpFile = fileService.buildTmpMusicFile(mCurrentMusic.getFileBytes());
                    player = new PlaybackListenerImpl(this, fileService, tmpFile.getCanonicalPath()) {
                        @Override
                        public void playbackStarted(PlayerEvent playerEvent) {
                            log.debug("playbackStarted");
                        }

                        @Override
                        public void playbackFinished(PlayerEvent playerEvent) {
                            log.debug("playbackFinished");
                            onMusicEnd();
                        }
                    };
                    player.play();
                } catch (Exception ex) {
                    log.error(ex.toString());
                }
            }

        } else {
            player.pauseToggle();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playerPause() {
        log.info("playerPause");
        if (player != null) {
            player.pauseToggle();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playerStop() {
        log.info("playerStop");
        if (player != null) {
            player.pause();
            player = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playerNext() {
        log.info("playerNext");
        // if playing then stop
        if (player != null) {
            playerStop();
        }
        // set current to next or first
        if (!mPlaylist.isEmpty()) {
            int nextIndex = 0;
            if (mCurrentMusic != null) {
                nextIndex = mPlaylist.indexOf(mCurrentMusic) + 1;
                nextIndex %= mPlaylist.size();
            }
            setCurrentMusic(mPlaylist.get(nextIndex));
        } else {
            // no effect
            log.debug("playerNext on empty list");
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
        if (player != null) {
            playerStop();
        }
        // set current to previous or last
        if (!mPlaylist.isEmpty()) {
            int previousIndex = mPlaylist.size() - 1;
            if (mCurrentMusic != null && mPlaylist.indexOf(mCurrentMusic) > 0) {
                previousIndex = mPlaylist.indexOf(mCurrentMusic) - 1;
            }
            setCurrentMusic(mPlaylist.get(previousIndex));
        } else {
            // no effect
            log.debug("playerPrevious on empty list");
        }
        // play current
        playerPlay();
    }

    /**
     * Return the current position in music if available
     *
     * @return the current position in music if available
     */
    public Long getCurrentTimeMs() {
        Long currentTime = null;
        if (player != null && mCurrentMusic != null) {
            currentTime = mCurrentTimeMs;
        }
        return currentTime;
    }

    /**
     * Change the current position in the music. Used to set position by the
     * user only
     *
     * @param timeInMs the time in the music, must be positive and lower than
     * music total duration
     * @return true if time is effectively set, false if nothing happened
     */
    public boolean setCurrentTimeMs(Long timeInMs) {
        boolean timeSet = false;
        if (timeInMs != null && player != null && mCurrentMusic != null) {
            //TODO: set time in player
            this.mCurrentTimeMs = timeInMs;
            propertyChangeSupport.firePropertyChange(Property.CURRENT_TIME.name(), null, timeInMs);
            timeSet = true;
        }
        return timeSet;
    }

    /**
     * Return the current music duration in milliseconds
     *
     * @return the current music duration in milliseconds, null if no music is
     * currently selected
     */
    public Long getTotalTimeMs() {
        Long totalTime = null;
        if (player != null && mCurrentMusic != null) {
            totalTime = new Long(mCurrentMusic.getTrackLength());
        }
        return totalTime;
    }

    /**
     * only used to auto update ui when playing music (by
     * SharUTCPlayBackListener), auto continue playing playlist when end of
     * music happens, auto stop at the end of the playlist
     *
     * @param evt CURRENT_TIME or MUSIC_END information
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PlaybackListenerImpl.Property.CURRENT_TIME.name()) && !mCurrentTimeMs.equals((Long) evt.getNewValue())) {
            onCurrentTimeUpdate((Long) evt.getNewValue());
        } else if (evt.getPropertyName().equals(PlaybackListenerImpl.Property.MUSIC_END.name())) {
            log.debug("PropertyChangeEvent : onMusicEnd");
            onMusicEnd();
        }
    }

    private void setCurrentMusic(Music music) {
        Music oldMusic = this.mCurrentMusic;
        this.mCurrentMusic = music;
        propertyChangeSupport.firePropertyChange(Property.SELECTED_MUSIC.name(), oldMusic, mCurrentMusic);
    }

    /**
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}

package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.controler.player.PlaybackListener;
import fr.utc.lo23.sharutc.controler.player.PlayerEvent;
import fr.utc.lo23.sharutc.controler.player.PlaybackListenerImpl;
import static fr.utc.lo23.sharutc.controler.service.PlayerServiceImpl.VOLUME_MAX;
import static fr.utc.lo23.sharutc.controler.service.PlayerServiceImpl.VOLUME_MIN;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionEvent;
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
public class PlayerServiceImpl implements PlayerService, PropertyChangeListener, CollectionChangeListener<Music> {

    private static final Logger log = LoggerFactory
            .getLogger(PlayerServiceImpl.class);
    public static final int VOLUME_MAX = 100;
    public static final int VOLUME_MIN = 0;
    private final FileService fileService;
    private PlaybackListener player;
    private Catalog mPlaylist = new Catalog();
    private Music mCurrentMusic;
    private Long mCurrentTimeSec = 0L;
    private Integer volume = 100;
    private boolean mute = false;
    private boolean pause = false;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    @Inject
    public PlayerServiceImpl(FileService fileService) {
        this.fileService = fileService;
        this.mPlaylist.addPropertyChangeListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToPlaylist(Music music) {
        if (music != null && !music.getFileMissing() && music.getFileBytes() != null) {
            mPlaylist.add(music);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromPlaylist(Music music) {
        if (music != null) {
            mPlaylist.remove(music);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playMusicFromPlaylist(Music music) {
        log.info("playMusicFromPlaylist");
        if (music != null && mPlaylist.contains(music)) {
            playerStop();
            mCurrentMusic = mPlaylist.get(mPlaylist.indexOf(music));
            playerPlay();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playOneMusic(Music music) {
        log.info("playOneMusic");
        if (music != null && !music.getFileMissing() && music.getFileBytes() != null) {
            mPlaylist.clear();
            mPlaylist.add(music);
            playerPlay();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAndPlayMusic(Music musicWithBytes) {
        log.info("updateAndPlayMusic");
        if (musicWithBytes != null && mPlaylist.contains(musicWithBytes)) {
            playerStop();
            int musicIndex = mPlaylist.indexOf(musicWithBytes);
            Music musicFromPlaylist = mPlaylist.get(musicIndex);
            musicFromPlaylist.setFileByte(musicWithBytes.getFileBytes());
            setCurrentMusic(musicFromPlaylist);
            playerPlay();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void playerPlay() {
        log.info("playerPlay");
        if (player == null) {
            if (mCurrentMusic == null) {
                setCurrentMusic(mPlaylist.get(0));
            }
            if (mCurrentMusic != null) {
                try {
                    File tmpFile = fileService.buildTmpMusicFile(mCurrentMusic.getFileBytes());
                    player = new PlaybackListenerImpl(this, this, fileService, tmpFile.getCanonicalPath()) {
                        @Override
                        public void playbackStarted(PlayerEvent playerEvent) {
                            log.debug("playbackStarted");
                            onPause(false);
                        }

                        @Override
                        public void playbackPaused(PlayerEvent playerEvent) {
                            log.debug("playbackPaused");
                            onPause(true);
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
    public boolean isPause() {
        return this.pause;
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
        setCurrentTimeSec(0L);
        onPause(false);
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
     * {@inheritDoc}
     */
    @Override
    public Long getCurrentTimeSec() {
        Long currentTime = null;
        if (player != null && mCurrentMusic != null) {
            currentTime = mCurrentTimeSec;
        }
        return currentTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentTimeSec(Long timeInSec) {
        if (mCurrentMusic != null && timeInSec != null) {
            if (timeInSec <= 0L) {
                timeInSec = 0L;
            } else if (timeInSec >= mCurrentMusic.getTrackLength()) {
                timeInSec = (long) mCurrentMusic.getTrackLength();
            }
            this.mCurrentTimeSec = timeInSec;
            if (player != null) {
                player.setCurrentTime(mCurrentTimeSec);
            }
            propertyChangeSupport.firePropertyChange(Property.CURRENT_TIME.name(), null, timeInSec);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getTotalTimeSec() {
        Long totalTime = null;
        if (mCurrentMusic != null) {
            totalTime = new Long(mCurrentMusic.getTrackLength());
        }
        return totalTime;
    }

    /**
     * {@inheritDoc}
     */
    private void setCurrentMusic(Music music) {
        Music oldMusic = this.mCurrentMusic;
        this.mCurrentMusic = music;
        propertyChangeSupport.firePropertyChange(Property.SELECTED_MUSIC.name(), oldMusic, mCurrentMusic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collectionChanged(CollectionEvent<Music> ev) {
        if (ev.getType().name().equals(CollectionEvent.Type.ADD.name())) {
            onPlaylistAdd();
        } else if (ev.getType().name().equals(CollectionEvent.Type.CLEAR.name())) {
            onPlaylistClear();
        } else if (ev.getType().name().equals(CollectionEvent.Type.REMOVE.name())) {
            onPlaylistRemove(ev);
        }
    }

    /**
     * Only used to auto update ui when playing music (by
     * SharUTCPlayBackListener), auto continue playing playlist when end of
     * music happens, auto stop at the end of the playlist
     *
     * @param evt CURRENT_TIME or MUSIC_END information
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PlaybackListenerImpl.Property.CURRENT_TIME.name()) && !mCurrentTimeSec.equals((Long) evt.getNewValue())) {
            onCurrentTimeUpdate((Long) evt.getNewValue());
        } else if (evt.getPropertyName().equals(PlaybackListenerImpl.Property.MUSIC_END.name())) {
            log.debug("PropertyChangeEvent : onMusicEnd");
            onMusicEnd();
        }
    }

    /**
     * For intern usage only, automatically switch on/off pause value for ui
     * usage, doesn't pauses the music
     *
     * @param pause the pause value seen by client
     */
    private void onPause(boolean pause) {
        this.pause = pause;
        propertyChangeSupport.firePropertyChange(Property.PAUSE.name(), !pause, pause);
    }

    private void onMusicEnd() {
        player = null;
        if (mPlaylist.indexOf(mCurrentMusic) < (mPlaylist.size() - 1)) {
            playerNext();
            setCurrentTimeSec(0L);
        } else {
            playerStop();
        }
    }

    private void onCurrentTimeUpdate(Long currentTimeSec) {
        Long oldTimeSec = mCurrentTimeSec;
        this.mCurrentTimeSec = currentTimeSec;
        propertyChangeSupport.firePropertyChange(Property.CURRENT_TIME.name(), oldTimeSec, mCurrentTimeSec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getPlaylist() {
        return mPlaylist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getVolume() {
        return volume;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVolume(int volume) {
        if (volume < VOLUME_MIN) {
            volume = VOLUME_MIN;
        }
        if (volume > VOLUME_MAX) {
            volume = VOLUME_MAX;
        }
        if (this.volume == null || this.volume != volume) {
            Integer oldVolume = this.volume;
            this.volume = volume;
            if (player != null) {
                player.setVolume(this.volume);
            }
            propertyChangeSupport.firePropertyChange(Property.VOLUME.name(), oldVolume, this.volume);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMute() {
        return mute;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMute(boolean mute) {
        if (this.mute != mute) {
            this.mute = mute;
            if (player != null) {
                player.setMute(this.mute);
            }
            propertyChangeSupport.firePropertyChange(Property.MUTE.name(), !this.mute, this.mute);
        }
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

    private void onPlaylistClear() {
        mCurrentTimeSec = 0L;
        mCurrentMusic = null;
    }

    private void onPlaylistRemove(CollectionEvent<Music> ev) {
        mCurrentTimeSec = 0L;
        if (mPlaylist.isEmpty()) {
            mCurrentMusic = null;
        } else {
            if (ev.getIndex() == mPlaylist.size()) {
                mCurrentMusic = mPlaylist.get(ev.getIndex() - 1);
            } else {
                mCurrentMusic = mPlaylist.get(ev.getIndex());
            }
        }
    }

    private void onPlaylistAdd() {
        if (mPlaylist.size() == 1) {
            mCurrentMusic = mPlaylist.get(0);
        }
    }
}

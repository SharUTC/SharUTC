package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.player.PlaybackListener;
import fr.utc.lo23.sharutc.controler.player.PlayerEvent;
import fr.utc.lo23.sharutc.controler.player.PlaybackListenerImpl;
import static fr.utc.lo23.sharutc.controler.service.PlayerServiceImpl.VOLUME_MAX;
import static fr.utc.lo23.sharutc.controler.service.PlayerServiceImpl.VOLUME_MIN;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.logging.Level;
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
    private final AppModel appModel;
    private final FileService fileService;
    private final NetworkService networkService;
    private final MusicService musicService;
    private static PlaybackListener player;
    private final Catalog mPlaylist;
    private Music mCurrentMusic;
    private int mCurrentMusicIndex = -1;
    private Long mCurrentTimeSec = 0L;
    private Integer volume = 100;
    private boolean mute = false;
    private boolean pause = false;
    private PropertyChangeSupport propertyChangeSupport;

    @Inject
    public PlayerServiceImpl(AppModel appModel, FileService fileService, MusicService musicService, NetworkService networkService) {
        this.appModel = appModel;
        this.fileService = fileService;
        this.networkService = networkService;
        this.musicService = musicService;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.mPlaylist = new Catalog();
        this.mPlaylist.addPropertyChangeListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToPlaylist(Music music) {
        if (music != null && !music.getFileMissing()) {
            mPlaylist.add(music);
            log.debug("addToPlaylist : music added");
        } else {
            log.error("addToPlaylist : music NOT added, null or missing music file");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromPlaylist(int index) {
        if (index >= 0 && index < mPlaylist.size()) {
            if (mPlaylist.removeAt(index)) {
                log.debug("removeFromPlaylist : music removed");
            } else {
                log.error("removeFromPlaylist : music NOT removed, not found");
            }
        } else {
            log.warn("removeFromPlaylist : music parameter is null");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearPlaylist() {
        for (int i = 0; i < mPlaylist.size(); i++) {
            mPlaylist.removeAt(i);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void playMusicFromPlaylist(int musicIndexInPlaylist) {
        log.info("playMusicFromPlaylist");
        playerStop();
        setCurrentMusicByIndex(musicIndexInPlaylist);
        playerPlay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void playOneMusic(Music music) {
        log.info("playOneMusic");
        if (music != null && !music.getFileMissing()) {
            mPlaylist.clear();
            mPlaylist.add(music);
            setCurrentTimeSec(0L);
            playerPlay();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void updateAndPlayMusic(Music musicWithBytes) {
        log.info("updateAndPlayMusic");
        if (musicWithBytes != null && mPlaylist.contains(musicWithBytes)) {
            appModel.getTmpCatalog().add(musicWithBytes);
            playerStop();
            int musicIndex = getCurrentMusicIndex();
            Music musicFromPlaylist = mPlaylist.get(musicIndex);
            if (musicFromPlaylist.equals(musicWithBytes)) {
                musicFromPlaylist.setFileBytes(musicWithBytes.getFileBytes());
                setCurrentMusicByIndex(musicIndex);
                playerPlay();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void playerPlay() {
        log.info("playerPlay");
        if (player == null) {
            if (mCurrentMusic == null && !mPlaylist.isEmpty()) {
                setCurrentMusicByIndex(0);
            }
            if (mCurrentMusic != null) {
                try {
                    File tmpFile = fileService.buildTmpMusicFile(mCurrentMusic.getFileBytes());
                    player = new PlaybackListenerImpl(this, this, fileService, tmpFile.getCanonicalPath()) {
                        @Override
                        public void playbackEvent(PlayerEvent playerEvent) {

                            switch (playerEvent.eventType) {
                                case STARTED:
                                    log.debug("playbackStarted");
                                    onPause(false);
                                    break;
                                case PAUSED:
                                    log.debug("playbackPaused");
                                    onPause(true);
                                    break;
                                case STOPPED:
                                    log.debug("playbackFinished");
                                    onMusicEnd();
                                    break;
                            }
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
        log.trace("isPause ({})", this.pause);
        return this.pause;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void playerStop() {
        log.info("playerStop");
        if (player != null) {
            player.stop();
            player = null;
        }
        setCurrentTimeSec(0L);
        onPause(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void playerNext() {
        log.info("playerNext");
        // if playing then stop
        if (player != null) {
            playerStop();
        }
        // set current to next or first
        if (!mPlaylist.isEmpty()) {
            int nextIndex = 0;
            if (mCurrentMusic != null) {
                nextIndex = getCurrentMusicIndex() + 1;
                nextIndex %= mPlaylist.size();
            }
            setCurrentMusicByIndex(nextIndex);
        } else {
            // no effect
            log.trace("playerNext on empty list");
        }
        // play current
        playerPlay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void playerPrevious() {
        log.info("playerPrevious");
        // if playing then stop
        if (player != null) {
            playerStop();
        }
        // set current to previous or last
        if (!mPlaylist.isEmpty()) {
            int previousIndex = mPlaylist.size() - 1;
            if (mCurrentMusic != null && getCurrentMusicIndex() > 0) {
                previousIndex = getCurrentMusicIndex() - 1;
            }
            setCurrentMusicByIndex(previousIndex);
        } else {
            // no effect
            log.trace("playerPrevious on empty list");
        }
        // play current
        playerPlay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getCurrentTimeSec() {
        log.trace("getCurrentTimeSec ...");
        Long currentTime = null;
        if (player != null && mCurrentMusic != null) {
            currentTime = mCurrentTimeSec;
        }
        log.trace("getCurrentTimeSec DONE ({} s)", currentTime);
        return currentTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setCurrentTimeSec(Long timeInSec) {
        log.debug("setCurrentTimeSec ({}) ...", timeInSec);
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
        log.debug("setCurrentTimeSec ({}) DONE", timeInSec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getTotalTimeSec() {
        log.trace("getTotalTimeSec ...");
        Long totalTime = null;
        if (mCurrentMusic != null) {
            totalTime = new Long(mCurrentMusic.getTrackLength());
        }
        log.trace("getTotalTimeSec DONE ({})", totalTime);
        return totalTime;
    }

    /**
     * {@inheritDoc}
     */
    private synchronized void setCurrentMusicByIndex(int musicIndex) {
        log.trace("setCurrentMusic ...");
        int oldMusicIndex = -1;//mCurrentMusicIndex;
        mCurrentMusicIndex = musicIndex;
        if (musicIndex == -1) {
            this.mCurrentMusic = null;
            propertyChangeSupport.firePropertyChange(Property.CURRENT_MUSIC_INDEX.name(), oldMusicIndex, musicIndex);
        } else {
            Music music = mPlaylist.get(musicIndex);
            if (music != null
                    && (music.getFileBytes() == null || music.getFileBytes().length == 0)
                    && !music.getOwnerPeerId().equals(appModel.getProfile().getUserInfo().getPeerId())) {
                log.debug("setCurrentMusic : delayed, fetching remote music data ...");
                fetchMusicDataThenPlay(music);
            } else {
                if (music != null && (music.getFileBytes() == null || music.getFileBytes().length == 0)) {
                    musicService.loadMusicFile(music);
                }
                this.mCurrentMusic = music;
                propertyChangeSupport.firePropertyChange(Property.CURRENT_MUSIC_INDEX.name(), oldMusicIndex, musicIndex);
            }
        }
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

    /**
     * Manage the ending of the music
     */
    private void onMusicEnd() {
        player = null;
        if (mPlaylist.indexOf(mCurrentMusic) < (mPlaylist.size() - 1)) {
            playerNext();
            setCurrentTimeSec(0L);
        } else {
            playerStop();
        }
    }

    /**
     * Manage update of current time
     *
     * @param currentTimeSec The new current time in seconds
     */
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
    public synchronized void setVolume(int volume) {
        log.debug("setVolume ({}) ...", volume);
        if (volume < VOLUME_MIN) {
            volume = VOLUME_MIN;
        } else if (volume > VOLUME_MAX) {
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
        log.debug("setVolume ({}) DONE", volume);
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
    public synchronized void setMute(boolean mute) {
        log.debug("setMute ({}) ...", mute);
        if (this.mute != mute) {
            this.mute = mute;
            if (player != null) {
                player.setMute(this.mute);
            }
            propertyChangeSupport.firePropertyChange(Property.MUTE.name(), !this.mute, this.mute);
        }
        log.debug("setMute ({}) DONE", mute);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Manage emptying of the playlist
     */
    private void onPlaylistClear() {
        playerStop();
        setCurrentMusicByIndex(-1);
    }

    /**
     * Manage the music removal of the playlist
     *
     * @param ev
     */
    private void onPlaylistRemove(CollectionEvent<Music> ev) {
        Integer newIndex = null;
        if (mPlaylist.isEmpty()) {
            playerStop();
            newIndex = -1;
        } else {
            // currentMusic was removed
            if (mCurrentMusic == ev.getItem()) {
                playerStop();
                // auto select next (=id removed) if possible for current music or previous
                // list is NOT EMPTY, mPlaylist has already changed
                if (mPlaylist.size() == ev.getIndex()) {
                    // deleted : last of playlist, no next, use previous
                    newIndex = ev.getIndex() - 1;
                } else {
                    // deleted : one of playlist except last
                    newIndex = ev.getIndex();
                }
            } else if (getCurrentMusicIndex() > ev.getIndex()) {
                mCurrentMusicIndex--;
            } else {
                // other music was removed, no need to stop music or change currentMusic reference
            }
        }
        if (newIndex != null) {
            setCurrentMusicByIndex(newIndex);
        }
    }

    /**
     * Manage the music addition to the playlist
     */
    private void onPlaylistAdd() {
        if (mPlaylist.size() == 1) {
            setCurrentMusicByIndex(0);
        }
    }

    /**
     * Load remote music the play
     *
     * @param music The music to load and play
     */
    private void fetchMusicDataThenPlay(Music music) {
        if (appModel.getTmpCatalog().contains(music)) {
            log.info("Loading data for remote music from TMP_CATALOG : {}", music);
            Music musicWithFile = appModel.getTmpCatalog().findMusicByHash(music.getHash());
            updateAndPlayMusic(musicWithFile);
        } else {
            Peer peer = appModel.getActivePeerList().getPeerByPeerId(music.getOwnerPeerId());
            log.info("Fetching data for remote music from {}: {}", peer, music);
            networkService.downloadMusicForPlaying(peer, music.getId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Music getCurrentMusic() {
        return mCurrentMusic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentMusicIndex() {
        return mCurrentMusicIndex;
    }
}

package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;

/**
 * Contains the playlist, the currently selected music reference (might be null)
 * and methods to playOneMusic music from the playlist
 */
public interface PlayerService {

    /**
     *
     * @return
     */
    public Catalog getPlaylist();

    /**
     * Add a music to playlist, auto-select the added music for player if it is
     * the only music in playlist, updates ui via listener on playlist
     *
     * @param music the music to add to playlist
     */
    public void addToPlaylist(Music music);

    /**
     * Removes the given music from playlist if exist, else do nothing, update
     * ui via listener on playlist
     *
     * @param music the music to remove from playlist
     */
    public void removeFromPlaylist(Music music);

    /**
     * Start playing a music, accepts local music only (with music.FileByte[] !=
     * null)
     *
     * @param music
     */
    public void playOneMusic(Music music);

    /**
     * NOT USED
     *
     * @param music
     */
    public void updatePlaylist(Music music);

    /**
     * Play currently selected music (and for which file is present)
     */
    public void playerPlay();

    /**
     * Pause currently playing music or do nothing
     */
    public void playerPause();

    /**
     * Stop currently playing music or do nothing
     */
    public void playerStop();

    /**
     * Play the next music if it exists in playlist, restart at beginning of the
     * list, do nothing if list is empty
     */
    public void playerNext();

    /**
     * Play the previous music if it exists in playlist, restart at end of the
     * list, do nothing if list is empty
     */
    public void playerPrevious();

    /**
     *
     * @return
     */
    public Integer getVolume();

    /**
     *
     * @param volume
     */
    public void setVolume(int volume);

    /**
     *
     * @return
     */
    public boolean isMute();

    /**
     *
     * @param mute
     */
    public void setMute(boolean mute);

    /**
     * Return the current position in music if available
     *
     * @return the current position in music if available
     */
    public Long getCurrentTimeSec();

    /**
     * Change the current position in the music. Used to set position by the
     * user only
     *
     * @param timeInSec the time in the music, must be positive and lower than
     * music total duration
     * @return true if time is effectively set, false if nothing happened
     */
    public void setCurrentTimeSec(Long currentTimeSec);

    /**
     * Return the current music duration in milliseconds
     *
     * @return the current music duration in milliseconds, null if no music
     * is/was currently selected in player
     */
    public Long getTotalTimeSec();
}

package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;

/**
 * Contains the playlist, the currently selected music reference and methods to
 * play music from the playlist and out of the playlist.
 *
 * As long as there is a music in the playlist, at least the first added, or the
 * last selected/played, music is automatically selected to allow user to clic
 * PLAY.
 *
 * User Interface may use only playerPlay() instead of playerPause() and
 * playerPlay(). PlayerPlay() starts, pauses and continues music, while
 * PlayerPause() only pauses or continues music once it has been already started
 * at least once.
 */
public interface PlayerService {

    /**
     * Return an Observable Catalog containing the playlist
     *
     * @return an Observable Catalog containing the playlist
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
     * Changes the currently selected music in playlist, if music is found, and
     * play it . Updates ui
     *
     * @param music the music to play from playlist
     */
    public void playMusicFromPlaylist(Music music);

    /**
     * Start playing a music, accepts local music only (with music.FileByte[] !=
     * null). First clear playlist, then add passed music and play it
     *
     * @param music
     */
    public void playOneMusic(Music music);

    /**
     * Replace the actual music in playlist by this one, and play it to simulate
     * local file behaviour
     *
     * @param musicToPlay the music to play
     */
    public void updateAndPlayMusic(Music musicToPlay);

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
     * Return the volume value, default value is 100, volume can be set even
     * while music isn't playing, player will read volume value and set it
     * before playing
     *
     * @return the volume value
     */
    public Integer getVolume();

    /**
     * Set the volume value, default value is 100, volume can be set even while
     * music isn't playing, player will read volume value and set it before
     * playing
     *
     * @param volume the volume value to set, must be between 0 and 100
     * included, otherwise value will automatically be set to 0 or 100
     */
    public void setVolume(int volume);

    /**
     * Return true if sound is muted in application, false otherwise. Works the
     * same as volume, value is checked before reading
     *
     * @return true if sound is muted, false otherwise
     */
    public boolean isMute();

    /**
     * Turn ON/OFF mute on volume. Works the same as volume, value is checked
     * before reading
     *
     * @param mute the mute value to set
     */
    public void setMute(boolean mute);

    /**
     * Return the current position in music if available
     *
     * @return the current position in music if available
     */
    public Long getCurrentTimeSec();

    /**
     * Change the current position in the music in seconds. Used to set position
     * by the user. Music doesn't require to be played to set the value but at
     * least one music must be selected in playlist
     *
     * @param currentTimeSec the time in the music, must be positive and lower than
     * music total duration
     */
    public void setCurrentTimeSec(Long currentTimeSec);

    /**
     * Return the current music duration in seconds
     *
     * @return the current music duration in seconds, null if no music is/was
     * currently selected in player
     */
    public Long getTotalTimeSec();

    public boolean isPause();

    /**
     *
     */
    public enum Property {

        /**
         *
         */
        CURRENT_TIME,
        /**
         *
         */
        CURRENT_MUSIC,
        /**
         *
         */
        VOLUME,
        /**
         *
         */
        MUTE,
        /**
         *
         */
        PAUSE
    }
}

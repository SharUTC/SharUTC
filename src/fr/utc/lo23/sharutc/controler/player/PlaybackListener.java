package fr.utc.lo23.sharutc.controler.player;

import fr.utc.lo23.sharutc.model.domain.Music;

/**
 *
 */
public interface PlaybackListener {

    /**
     * Return the currently played music
     *
     * @return the currently played music
     */
    public Music getMusic();

    /**
     * Called each time the player is paused. Current time is passed via
     * parameters , this allows the client to decide what to do at pause
     *
     * @param event
     */
    public void playbackEvent(PlayerEvent event);

    /**
     * Called internally by Mp3Player to inform higher level classes of position
     * in music changes
     *
     * @param currentFrameIndex the new index of currentFrame
     */
    public void setCurrentFrameIndex(int currentFrameIndex);

    /**
     * Initialize the current music in player and/or create and start player
     * thread
     */
    public void play();

    /**
     *
     */
    public void pause();

    /**
     *
     */
    public void stop();

    /**
     * Invoque pause() if player is playing or play()
     */
    public void pauseToggle();

    /**
     * Set gain DURING PLAYING ONLY (gain control isn't available before music
     * has been played, at least one frame) to -80F db, equivalent to mute (no
     * sound at -30db), or restore previous value using volume value
     *
     * @param mute
     */
    public void setMute(boolean mute);

    /**
     * Performs a volume conversion, volume reference used for db is 100, volume
     * goes from 0 to 100, equivalent to -30db to 0db. ONLY USED DURING PLAYING
     * (gain control isn't available before music has been played, at least one
     * frame)
     *
     * @param volume from 0 to 100, value is set to max or min if it exceeds
     * limits
     */
    public void setVolume(int volume);

    /**
     * Set the next frame to read WHILE PLAYING ONLY (time isn't when player
     * thread isn't running)
     *
     * @param timeSec the time to set in seconds, value is set to 0 if negative
     * and to music length if it exceeds music.trackLength
     */
    public void setCurrentTime(long timeSec);
}

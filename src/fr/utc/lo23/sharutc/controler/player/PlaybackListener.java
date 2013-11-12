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
     * Called each time a sound starts to play, at music start and at pause/play
     * during music. Nothing is passed via parameters and nothing is done in
     * PlaybackListener method, this allows the client to decide what to do at
     * playing
     *
     * @param event
     */
    public void playbackStarted(PlayerEvent event);

    /**
     * Called each time the player is paused. Current time is passed via
     * parameters , this allows the client to decide what to do at pause
     *
     * @param event
     */
    public void playbackPaused(PlayerEvent event);

    /**
     * Called only when a music reaches the end of bytes at playing. Nothing is
     * passed via parameters and nothing is done in PlaybackListener method,
     * this allows the client to decide what to do at music end
     *
     * @param event
     */
    public void playbackFinished(PlayerEvent event);

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
     * Pauses the player and destroy its thread
     */
    public void pause();

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

package fr.utc.lo23.sharutc.controler.player;

import fr.utc.lo23.sharutc.model.domain.Music;

/**
 *
 */
public interface PlaybackListener {

    public Music getMusic();

    public void playbackStarted(PlayerEvent event);

    public void playbackFinished(PlayerEvent event);

    public void newFrameIndex(int frameIndexCurrent);

    public void play();

    public void pause();

    public void pauseToggle();

    public void setMute(boolean mute);

    public void setVolume(int volume);

    public void setCurrentTime(long mCurrentTimeMs);
}

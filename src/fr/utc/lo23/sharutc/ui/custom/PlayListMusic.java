package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.domain.Music;

/**
 * A simple model of a piece of music.
 *
 * Encapsulate a piece of {@link Music} and a boolean describing its current
 * playing state.
 */
public class PlayListMusic {

    public Music music;
    private boolean played;

    public PlayListMusic(Music music) {
        this.music = music;
        this.played = false;
    }

    /**
     * Change the current playing state.
     *
     * @param b the new playing state.
     */
    public void setPlaying(boolean b) {
        played = b;
    }

    /**
     * Get the current playing state.
     *
     * @return true if the music is being played, false otherwise.
     */
    public boolean isPlaying() {
        return played;
    }
}

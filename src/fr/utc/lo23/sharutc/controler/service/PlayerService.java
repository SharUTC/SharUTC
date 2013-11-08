package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.domain.Music;

/**
 * Contains the playlist, the currently selected music reference (might be null)
 * and methods to play music from the playlist
 */
public interface PlayerService {

    /**
     *
     * @param music
     */
    public void addToPlaylist(Music music);

    /**
     *
     * @param music
     */
    public void removeFromPlaylist(Music music);

    /**
     * Start playing a music, accepts local music only (with music.FileByte[] !=
     * null)
     *
     * @param music
     */
    public void play(Music music);

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
}

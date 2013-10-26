package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.domain.Music;

/**
 *
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
     *
     * @param music
     */
    public void play(Music music);

    /**
     *
     * @param music
     */
    public void updatePlaylist(Music music);

    /**
     *
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
     *
     */
    public void next();

    /**
     *
     */
    public void previous();
}

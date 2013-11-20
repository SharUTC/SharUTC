package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 * Allow to set the score on a music
 */
public interface SetScoreCommand extends Command {

    /**
     * Get the peer who set the score
     *
     * @return The peer who set the score
     */
    public Peer getPeer();


    /**
     * Set the peer who set the score
     *
     * @param peer The peer who set the score
     */
    public void setPeer(Peer peer);

    /**
     * Get the music to comment
     *
     * @return The music to comment
     */
    public Music getMusic();

    /**
     * Set the music to comment
     *
     * @param music The music to comment
     */
    public void setMusic(Music music);

    /**
     * Get the value of the score
     *
     * @return The value of the score
     */
    public int getScore();

    /**
     * Set the value of the score
     *
     * @param score The value of the score
     */
    public void setScore(int score);
}

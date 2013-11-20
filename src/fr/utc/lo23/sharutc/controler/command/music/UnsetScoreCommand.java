package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 * Allow to unset the score on a music
 */
public interface UnsetScoreCommand extends Command {

    /**
     * Get the peer who unset the score
     *
     * @return The peer who unset the score
     */
    public Peer getPeer();


    /**
     * Set the peer who unset the score
     *
     * @param peer The peer who unset the score
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
}

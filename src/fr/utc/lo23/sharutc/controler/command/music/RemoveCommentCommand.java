package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 * Allow to remove a comment on a music
 */
public interface RemoveCommentCommand extends Command {

    /**
     * Get the owner of the comment
     *
     * @return The owner of the comment
     */
    public Peer getPeer();

    /**
     * Set the owner of the comment
     *
     * @param peer The owner of the comment
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
     * Get the id of the comment for the music
     *
     * @return The id of the comment for the music
     */
    public int getCommentId();

    /**
     * Set the id of the comment for the music
     *
     * @param commentId The id of the comment for the music
     */
    public void setCommentId(int commentId);
}

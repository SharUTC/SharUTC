package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 * Allow to add a comment on a music
 */
public interface AddCommentCommand extends Command {

    /**
     * Get the owner of the music commented
     *
     * @return The owner of the music commented
     */
    public Peer getOwnerPeer();

    /**
     * Set the owner of the music commented
     *
     * @param ownerPeer The owner of the music commented
     */
    public void setOwnerPeer(Peer ownerPeer);

    /**
     * Get the author of the comment
     *
     * @return The author of the comment
     */
    public Peer getAuthorPeer();

    /**
     * Set the author of the comment
     *
     * @param authorPeer The author of the comment
     */
    public void setAuthorPeer(Peer authorPeer);

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
     * Get the comment for the music
     *
     * @return The comment for the music
     */
    public String getComment();

    /**
     * Set the comment for the music
     *
     * @param comment The comment for the music
     */
    public void setComment(String comment);
}

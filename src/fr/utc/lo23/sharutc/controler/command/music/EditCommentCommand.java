package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 *
 */
public interface EditCommentCommand extends Command {

    /**
     *
     * @return
     */
    public Peer getOwnerPeer();

    /**
     *
     * @param ownerPeer
     */
    public void setOwnerPeer(Peer ownerPeer);

    /**
     *
     * @return
     */
    public Peer getAuthorPeer();

    /**
     *
     * @param authorPeer
     */
    public void setAuthorPeer(Peer authorPeer);

    /**
     *
     * @return
     */
    public Music getMusic();

    /**
     *
     * @param music
     */
    public void setMusic(Music music);

    /**
     *
     * @return
     */
    public String getComment();

    /**
     *
     * @param comment
     */
    public void setComment(String comment);

    /**
     *
     * @return
     */
    public int getCommentId();

    /**
     *
     * @param commentId
     */
    public void setCommentId(int commentId);
}

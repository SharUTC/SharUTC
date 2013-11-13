package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class AddCommentCommandMock implements AddCommentCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddCommentCommandMock.class);
    private Peer ownerPeer;
    private Peer authorPeer;
    private Music music;
    private String comment;

    /**
     *
     * @return
     */
    @Override
    public Peer getOwnerPeer() {
        return ownerPeer;
    }

    /**
     *
     * @param ownerPeer
     */
    @Override
    public void setOwnerPeer(Peer ownerPeer) {
        this.ownerPeer = ownerPeer;
    }

    /**
     *
     * @return
     */
    @Override
    public Peer getAuthorPeer() {
        return authorPeer;
    }

    /**
     *
     * @param authorPeer
     */
    @Override
    public void setAuthorPeer(Peer authorPeer) {
        this.authorPeer = authorPeer;
    }

    /**
     *
     * @return
     */
    @Override
    public Music getMusic() {
        return music;
    }

    /**
     *
     * @param music
     */
    @Override
    public void setMusic(Music music) {
        this.music = music;
    }

    /**
     *
     * @return
     */
    @Override
    public String getComment() {
        return comment;
    }

    /**
     *
     * @param comment
     */
    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     *
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }
}

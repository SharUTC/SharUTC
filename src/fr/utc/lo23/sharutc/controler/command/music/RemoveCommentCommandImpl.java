package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class RemoveCommentCommandImpl implements RemoveCommentCommand {

    private static final Logger log = LoggerFactory
            .getLogger(RemoveCommentCommandImpl.class);
    private Peer peer;
    private Music music;
    private int commentId;

    /**
     *
     */
    public RemoveCommentCommandImpl() {
    }

    /**
     *
     * @return
     */
    @Override
    public Peer getPeer() {
        return peer;
    }

    /**
     *
     * @param peer
     */
    @Override
    public void setPeer(Peer peer) {
        this.peer = peer;
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

    @Override
    public int getCommentId() {
        return commentId;
    }

    @Override
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    /**
     *
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }
}

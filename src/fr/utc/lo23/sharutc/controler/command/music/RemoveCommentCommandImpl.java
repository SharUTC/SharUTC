package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class RemoveCommentCommandImpl implements RemoveCommentCommand {

    private static final Logger log = LoggerFactory
            .getLogger(RemoveCommentCommandImpl.class);
    private Peer mPeer;
    private Music mMusic;
    private int mCommentId;

    /**
     * {@inheritDoc}
     */
    public RemoveCommentCommandImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Peer getPeer() {
        return mPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPeer(Peer peer) {
        this.mPeer = peer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Music getMusic() {
        return mMusic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMusic(Music music) {
        this.mMusic = music;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCommentId() {
        return mCommentId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCommentId(int commentId) {
        this.mCommentId = commentId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }
}

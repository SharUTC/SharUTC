package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class AddCommentCommandImpl implements AddCommentCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddCommentCommandImpl.class);
    private final MusicService musicService;
    private Peer mOwnerPeer;
    private Peer mAuthorPeer;
    private Music mMusic;
    private String mComment;

    /**
     * {@inheritDoc}
     */
    @Inject
    public AddCommentCommandImpl(MusicService musicService) {
        this.musicService = musicService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Peer getOwnerPeer() {
        return mOwnerPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOwnerPeer(Peer ownerPeer) {
        this.mOwnerPeer = ownerPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Peer getAuthorPeer() {
        return mAuthorPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAuthorPeer(Peer authorPeer) {
        this.mAuthorPeer = authorPeer;
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
    public String getComment() {
        return mComment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setComment(String comment) {
        this.mComment = comment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("AddCommentCommandImpl ...");
        musicService.addComment(mAuthorPeer, mMusic, mComment);
        log.info("AddCommentCommandImpl DONE");
    }
}

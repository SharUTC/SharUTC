package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SetScoreCommandImpl implements SetScoreCommand {

    private static final Logger log = LoggerFactory
            .getLogger(SetScoreCommandImpl.class);
    private Peer peer;
    private Music music;
    private int score;

    /**
     *
     */
    public SetScoreCommandImpl() {
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

    /**
     *
     * @return
     */
    @Override
    public int getScore() {
        return score;
    }

    /**
     *
     * @param score
     */
    @Override
    public void setScore(int score) {
        this.score = score;
    }

    /**
     *
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }
}

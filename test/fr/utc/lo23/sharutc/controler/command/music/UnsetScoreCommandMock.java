package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class UnsetScoreCommandMock implements UnsetScoreCommand {

    private static final Logger log = LoggerFactory
            .getLogger(UnsetScoreCommandMock.class);

    /**
     *
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }

    /**
     *
     * @return
     */
    @Override
    public Peer getPeer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param peer
     */
    @Override
    public void setPeer(Peer peer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return
     */
    @Override
    public Music getMusic() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param music
     */
    @Override
    public void setMusic(Music music) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Music;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class RemoveFromLocalCatalogCommandMock implements RemoveFromLocalCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(RemoveFromLocalCatalogCommandMock.class);

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
    public Collection<Music> getMusics() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param musics
     */
    @Override
    public void setMusics(Collection<Music> musics) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

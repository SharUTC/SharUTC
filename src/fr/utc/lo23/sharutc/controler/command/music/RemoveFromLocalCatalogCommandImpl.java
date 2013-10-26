package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Music;
import java.util.Collection;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class RemoveFromLocalCatalogCommandImpl implements RemoveFromLocalCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(RemoveFromLocalCatalogCommandImpl.class);
    private Collection<Music> musics;

    /**
     *
     */
    public RemoveFromLocalCatalogCommandImpl() {
    }

    /**
     *
     * @return
     */
    @Override
    public Collection<Music> getMusics() {
        return Collections.unmodifiableCollection(musics);
    }

    /**
     *
     * @param musics
     */
    @Override
    public void setMusics(Collection<Music> musics) {
        this.musics = musics;
    }

    /**
     *
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }
}

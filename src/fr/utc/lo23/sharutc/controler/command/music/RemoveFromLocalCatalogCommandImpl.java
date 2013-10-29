package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.domain.Music;
import java.util.Collection;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class RemoveFromLocalCatalogCommandImpl implements RemoveFromLocalCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(RemoveFromLocalCatalogCommandImpl.class);
    private Collection<Music> mMusics;

    /**
     * {@inheritDoc}
     */
    public RemoveFromLocalCatalogCommandImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Music> getMusics() {
        return Collections.unmodifiableCollection(mMusics);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMusics(Collection<Music> musics) {
        this.mMusics = musics;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }
}

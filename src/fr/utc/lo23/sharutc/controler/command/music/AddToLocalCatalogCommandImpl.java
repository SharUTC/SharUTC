package fr.utc.lo23.sharutc.controler.command.music;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class AddToLocalCatalogCommandImpl implements AddToLocalCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddToLocalCatalogCommandImpl.class);
    private Collection<File> mFiles;

    /**
     * {@inheritDoc}
     */
    public AddToLocalCatalogCommandImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<File> getFiles() {
        return Collections.unmodifiableCollection(mFiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFiles(Collection<File> files) {
        this.mFiles = files;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }
}

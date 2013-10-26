package fr.utc.lo23.sharutc.controler.command.music;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class AddToLocalCatalogCommandImpl implements AddToLocalCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddToLocalCatalogCommandImpl.class);
    private Collection<File> files;

    /**
     *
     */
    public AddToLocalCatalogCommandImpl() {
    }

    /**
     *
     * @return
     */
    @Override
    public Collection<File> getFiles() {
        return Collections.unmodifiableCollection(files);
    }

    /**
     *
     * @param files
     */
    @Override
    public void setFiles(Collection<File> files) {
        this.files = files;
    }

    /**
     *
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }
}

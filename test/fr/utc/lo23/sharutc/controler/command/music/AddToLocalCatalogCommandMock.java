package fr.utc.lo23.sharutc.controler.command.music;

import java.io.File;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class AddToLocalCatalogCommandMock implements AddToLocalCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddToLocalCatalogCommandMock.class);

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
    public Collection<File> getFiles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param files
     */
    @Override
    public void setFiles(Collection<File> files) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

package fr.utc.lo23.sharutc.controler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class FileServiceMock implements FileService {

    private static final Logger log = LoggerFactory
            .getLogger(FileServiceMock.class);

    /**
     *
     * @param path
     * @param password
     */
    @Override
    public void importFile(String path, String password) {
        log.warn("Not supported yet.");
    }

    /**
     *
     * @param path
     */
    @Override
    public void writeExportFile(String path) {
        log.warn("Not supported yet.");
    }
}

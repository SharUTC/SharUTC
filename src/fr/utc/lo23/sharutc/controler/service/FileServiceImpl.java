package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Singleton
public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory
            .getLogger(FileServiceImpl.class);
    @Inject
    AppModel model;
    @Inject
    MusicService musicService;
    @Inject
    UserService userService;

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

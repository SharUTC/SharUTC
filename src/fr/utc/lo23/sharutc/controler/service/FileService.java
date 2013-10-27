package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.domain.Music;
import java.io.File;

/**
 *
 *
 */
public interface FileService {

    /**
     *
     * @param path
     * @param password
     */
    public void importFile(String path, String password);

    /**
     *
     * @param path
     */
    public void writeExportFile(String path);

    /**
     *
     * @param file
     * @return
     */
    public Music readFile(File file) throws Exception;
}

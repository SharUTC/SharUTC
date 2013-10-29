package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.domain.Music;
import java.io.File;

/**
 * Used for import/export all data relative to an acocunt
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
     * Used for reading a local mp3 file and creating a new Music, increments
     * profile's counter If reading id3tag fails, then values are set to null
     *
     * @param file a mp3 file
     * @return music whose filename equals realname
     * @throws Exception if file is null or not an mp3
     */
    public Music readFile(File file) throws Exception;
}

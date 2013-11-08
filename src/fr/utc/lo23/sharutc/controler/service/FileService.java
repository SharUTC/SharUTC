package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.domain.Music;
import java.io.File;
import java.io.IOException;

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
     * Compress the folder srcPath and write it at destPath
     *
     * @param srcPath path of the folder to compress
     * @param destPath path of the destination
     * @throws java.io.IOException
     */
    public void exportFile(String srcPath, String destPath) throws IOException;

    /**
     * Used for reading a local mp3 file and creating a new Music, increments
     * profile's counter If reading id3tag fails, then values are set to null
     *
     * @param file a mp3 file
     * @return music whose filename equals realname
     * @throws Exception if file is null or not an mp3
     */
    public Music readFile(File file) throws Exception;

    /**
     * Read a file and return its content as an array of Bytes
     *
     * @param file the file to read
     * @return the content of the file as an array of Bytes
     */
    public Byte[] getFileAsByteArray(File file) throws IOException;

    /**
     * Give access to a temporary file automatically deleted when application
     * stops, used by musicPlayer
     *
     * @param currentMusic
     * @return the temporary file
     */
    public File buildTmpMusicFile(Byte[] musicBytes) throws Exception;
}

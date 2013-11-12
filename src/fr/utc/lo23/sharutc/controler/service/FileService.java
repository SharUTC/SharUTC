package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.domain.Music;
import java.io.File;
import java.io.IOException;

/**
 * Used for import/export all data relative to an acocunt
 *
 */
public interface FileService {

    public static final String APP_NAME = "SharUTC";
    public static final String ROOT_FOLDER_USERS = "users";
    public static final String FOLDER_MUSICS = "musics";
    public static final String DOT_MP3 = ".mp3";
    public static final String[] AUTHORIZED_MUSIC_FILE_TYPE = {"mp3"};
    public static final int MIN_FILENAME_LENGTH = 1;

    /**
     *
     * @param srcPath
     * @throws java.lang.Exception
     */
    public void importWholeProfile(String srcPath) throws Exception;

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
    public byte[] getFileAsByteArray(File file) throws IOException;

    /**
     * Give access to a temporary file automatically deleted when application
     * stops, used by musicPlayer
     *
     * @param currentMusic
     * @return the temporary file
     */
    public File buildTmpMusicFile(Byte[] musicBytes) throws Exception;

    /**
     * Return a file instance of a local music, null if the file doesn't exist
     *
     * @param localMusic the music from which we want the file
     * @return a file instance of a local music, null if the file doesn't exist
     */
    public File getFileOfLocalMusic(Music localMusic);

    public String computeRealName(String name);

    public String computeFileName(String realName, String realname);

    public void saveToFile(SharUTCFile sharUTCFile, Object objectToSave);

    public <T> T readFile(SharUTCFile sharUTCFile, Class<T> clazz);

    public void createFile(byte[] bytes, String fileName);

    public void createAccountFolder(String login);
}

package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Profile;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Used for import/export all data relative to an acocunt
 *
 */
public interface FileService {

    public static final String APP_NAME = "SharUTC";
    public static final String ROOT_FOLDER_USERS = "users";
    public static final String ROOT_FOLDER_TMP = "tmp";
    public static final String FOLDER_MUSICS = "musics";
    public static final String DOT_MP3 = ".mp3";
    public static final String[] AUTHORIZED_MUSIC_FILE_TYPE = {"mp3"};
    public static final int MIN_FILENAME_LENGTH = 1;

    public String getAppFolder();

    /**
     * Unzip the file find in <i>srcPath</i> in the users folder
     *
     * @param srcPath - path of the file to unzip
     * @param force - <b>True</b> if the profile already exists, it overwrites
     * it
     * <b>False</b> if the profile already exists, throws an Exception
     * @throws java.lang.Exception
     */
    public void importWholeProfile(String srcPath, boolean force) throws Exception;

    /**
     * Compress the folder srcPath into a zip and write it at destPath
     *
     * @param srcPath path of the folder to compress
     * @param destPath path of the destination
     * @throws java.io.IOException
     */
    public void exportFile(String srcPath, String destPath) throws IOException;

    /**
     * Delete every file and forlder under <i>pathname</i>
     *
     * @param pathname
     */
    public void deleteFolderRecursively(String pathname);

    /**
     * Used for reading a local mp3 file and creating a new Music, increments
     * profile's counter. If reading id3tag fails, then tag values are set to
     * null
     *
     * @param file a mp3 file
     * @return music whose filename equals realname
     * @throws Exception if file is null or not an mp3
     */
    public Music createMusicFromFile(File file) throws Exception;

    /**
     * Read a file and return its content as an array of Bytes
     *
     * @param file the file to read
     * @return the content of the file as an array of Bytes
     * @throws java.io.IOException
     */
    public byte[] getFileAsByteArray(File file) throws IOException;

    /**
     * Give access to a temporary file automatically deleted when application
     * stops, used by musicPlayer
     *
     * @param musicBytes
     * @return the temporary file
     * @throws java.lang.Exception
     */
    public File buildTmpMusicFile(Byte[] musicBytes) throws Exception;

    /**
     * Used to install music received as Byte[], create temporary files to then
     * reuse methods that performs verifications on filename
     *
     * @param catalog the list of musics to install
     * @return a list of files to copy in user's folder, these files are
     * automatically deleted when application stops
     * @throws Exception if an error occurs while write files
     */
    public List<File> buildTmpMusicFilesForInstall(Catalog catalog) throws Exception;

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

    public Profile readProfileFile(String login);

    public void createMusicFile(byte[] bytes, String fileName, String login);

    public void createAccountFolder(String login);

    public Music fakeMusicFromFile(File file) throws Exception;
}

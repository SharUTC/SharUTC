package fr.utc.lo23.sharutc.controler.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import static fr.utc.lo23.sharutc.controler.service.FileService.DOT_MP3;
import static fr.utc.lo23.sharutc.controler.service.FileService.ROOT_FOLDER_TMP;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static fr.utc.lo23.sharutc.controler.service.FileService.ROOT_FOLDER_USERS;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.userdata.Profile;
import javax.swing.JFileChooser;

/**
 *
 */
@Singleton
public class FileServiceImpl implements FileService {

    protected static String appFolder;
    private static final int BUFFER_SIZE = 2048;
    private static final int COMPRESSION_LEVEL = 9;
    private static final String ZIP_FORMAT = "zip";
    private static final Logger log = LoggerFactory
            .getLogger(FileServiceImpl.class);
    private final AppModel appModel;
    private File tmpFile;
    protected final ObjectMapper mapper = new ObjectMapper();

    @Inject
    public FileServiceImpl(AppModel appModel) throws IOException {
        this.appModel = appModel;

        appFolder = new File(".").getCanonicalPath();  //JFileChooser().getFileSystemView().getDefaultDirectory().toString();
     /*   mapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        mapper.enable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
       
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);*/
     //   mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        appFolder += File.separator + APP_NAME + File.separator;

        if (!new File(appFolder).exists()) {
            new File(appFolder).mkdir();
            if (!new File(appFolder + ROOT_FOLDER_USERS).exists()) {
                new File(appFolder + ROOT_FOLDER_USERS).mkdir();
            }
        }
    }

    @Override
    public String getAppFolder() {
        log.debug("getAppFolder ({})", appFolder);
        return appFolder;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void importWholeProfile(String srcPath, boolean force) throws Exception {
        log.debug("importWholeProfile ...");
        //check the format
        int lastP = srcPath.lastIndexOf('.');
        if (lastP == -1 || srcPath.lastIndexOf(File.separator) == -1
                || (lastP != -1 && lastP + 1 < srcPath.length() && !srcPath.substring(lastP + 1).equals(ZIP_FORMAT))) {
            throw new Exception("File format is not supported");
        }

        String userName = srcPath.substring(srcPath.lastIndexOf(File.separator), lastP);
        //check if the user already exists
        if (new File(appFolder + ROOT_FOLDER_USERS + File.separator + userName).exists()) {
            if (!force) {
                throw new Exception("This user already exists");
            } else {
                File userFolder = new File(appFolder + ROOT_FOLDER_USERS + File.separator + userName);
                deleteFolderRecursively(userFolder.getAbsolutePath());
                userFolder.delete();
            }
        }

        //chech the structure of the file
        boolean musicJsonExists = false;
        boolean profileJsonExists = false;
        boolean rightsJsonExists = false;
        boolean musicFolderExists = false;

        ZipFile zf = new ZipFile(srcPath);
        Enumeration entries = zf.entries();

        while (entries.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) entries.nextElement();
            String entryName = ze.getName();
            if (entryName.equals(JSON_MUSICS)) {
                musicJsonExists = true;
            } else if (entryName.equals(JSON_PROFILE)) {
                profileJsonExists = true;
            } else if (entryName.equals(JSON_RIGHTS)) {
                rightsJsonExists = true;
            } else if (entryName.indexOf(File.separator) != -1
                    && entryName.substring(0, entryName.indexOf(File.separator)).equals(FOLDER_MUSICS)) {
                musicFolderExists = true;
            }
        }

        if (!musicJsonExists || !profileJsonExists
                || !rightsJsonExists || !musicFolderExists) {
            throw new Exception("Corrupted zip file");
        }

        //Create user folder and musics folder
        new File(appFolder + ROOT_FOLDER_USERS + File.separator + userName + File.separator + FOLDER_MUSICS).mkdirs();

        //Unzip
        byte data[] = new byte[BUFFER_SIZE];
        FileInputStream inStream = new FileInputStream(srcPath);
        BufferedInputStream inBuffer = new BufferedInputStream(inStream);
        ZipInputStream zipInStream = new ZipInputStream(inBuffer);

        ZipEntry entry;
        while ((entry = zipInStream.getNextEntry()) != null) {
            String outputPath = appFolder + ROOT_FOLDER_USERS + File.separator + userName + File.separator + entry.getName();
            FileOutputStream outStream = new FileOutputStream(outputPath);
            BufferedOutputStream outBuffer = new BufferedOutputStream(outStream, BUFFER_SIZE);

            int count;
            while ((count = zipInStream.read(data, 0, BUFFER_SIZE)) != -1) {
                outBuffer.write(data, 0, count);
            }
            outBuffer.flush();
            outBuffer.close();
        }
        zipInStream.close();
        log.debug("importWholeProfile DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportFile(String srcPath, String destPath) throws IOException {
        log.debug("exportFile ...");
        List<String> fileList = new ArrayList<String>();
        byte data[] = new byte[BUFFER_SIZE];

        getFilesRec(fileList, new File(srcPath), srcPath);

        //Output
        FileOutputStream dest = new FileOutputStream(destPath);
        BufferedOutputStream outBuffer = new BufferedOutputStream(dest);
        ZipOutputStream outStream = new ZipOutputStream(outBuffer);
        outStream.setMethod(ZipOutputStream.DEFLATED);
        outStream.setLevel(COMPRESSION_LEVEL);

        for (String path : fileList) {
            //Input
            FileInputStream srcFile = new FileInputStream(srcPath + File.separator + path);
            BufferedInputStream inBuffer = new BufferedInputStream(srcFile, BUFFER_SIZE);
            ZipEntry entry = new ZipEntry(path);
            outStream.putNextEntry(entry);

            int count;
            while ((count = inBuffer.read(data, 0, BUFFER_SIZE)) != -1) {
                outStream.write(data, 0, count);
            }

            inBuffer.close();
            outStream.closeEntry();
        }
        outStream.close();
        log.debug("exportFile DONE");
    }

    /**
     * Traverse a directory and get all files, and add the file into fileList
     *
     * @param fileList list of file names
     * @param node file or directory
     */
    private void getFilesRec(List<String> fileList, File node, String sourceFolder) {
        if (node.isFile()) {
            String filePath = node.getAbsoluteFile().toString();
            //Format the file path for zip
            filePath = filePath.substring(sourceFolder.length() + 1, filePath.length());
            fileList.add(filePath);
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote) {
                getFilesRec(fileList, new File(node, filename), sourceFolder);
            }
        }
    }

    /**
     * Delete every file and forlder under <i>pathname</i> {@inheritDoc}
     *
     * @param pathname
     */
    @Override
    public void deleteFolderRecursively(String pathname) {
        log.debug("deleteFolderRecursively ({})...", pathname);
        File file = new File(pathname);
        if (file.exists()) {
            File[] children = file.listFiles();
            for (int i = 0; i < children.length; ++i) {
                if (children[i].isDirectory()) {
                    deleteFolderRecursively(children[i].getAbsolutePath());
                }
                children[i].delete();
            }
        }
        log.debug("deleteFolderRecursively DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Music createMusicFromFile(File file) throws Exception {
        log.debug("createMusicFromFile ...");
        if (file == null) {
            log.error("File is null");
            throw new Exception("File is null");
        }
        log.info("reading file ({})", file.getCanonicalPath());

        if (!isMusicFile(file)) {
            log.error("File is not a music File");
            throw new Exception("File is not a music File");
        }
        String title;
        String artist;
        String album;
        String year;
        String track;
        Integer trackLength;
        Long frames;
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            AudioHeader ah = audioFile.getAudioHeader();
            MP3AudioHeader mp3AudioHeader = (MP3AudioHeader) ah;
            Tag tag = audioFile.getTag();
            title = tag.getFirst(FieldKey.TITLE);
            artist = tag.getFirst(FieldKey.ARTIST);
            album = tag.getFirst(FieldKey.ALBUM);
            year = tag.getFirst(FieldKey.YEAR);
            track = tag.getFirst(FieldKey.TRACK);
            trackLength = ah.getTrackLength();
            frames = mp3AudioHeader.getNumberOfFrames();
        } catch (Exception ex) {
            log.error("Unable to read music file informations : {}", ex.toString());
            title = null;
            artist = null;
            album = null;
            year = null;
            track = null;
            trackLength = null;
            frames = 0L;
        }
        byte[] byteArray = getFileAsByteArray(file);
        Byte[] bytes = new Byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            bytes[i] = byteArray[i];
        }
        Music music = new Music(appModel.getProfile().getNewMusicId(),
                appModel.getProfile().getUserInfo().getPeerId(), bytes,
                file.getName(), file.getName(), file.hashCode(), title, artist, album, year, track,
                trackLength, frames);
        log.debug("createMusicFromFile DONE");
        return music;
    }

    @Override
    public Music fakeMusicFromFile(File file) throws Exception {
        log.debug("fakeMusicFromFile ...");
        Music music = createMusicFromFile(file);
        appModel.getProfile().decrementMusicId();
        log.debug("fakeMusicFromFile DONE");
        return music;
    }

    /**
     * Return true if the file extension is one of the authorized extensions,
     * false otherwise
     *
     * @param file the file to check
     * @return true if the file extension is one of the authorized extensions,
     * false otherwise
     */
    private boolean isMusicFile(File file) {
        boolean fileIsAMusic = false;
        for (String extension : AUTHORIZED_MUSIC_FILE_TYPE) {
            if (file.getName().endsWith("." + extension)) {
                fileIsAMusic = true;
            }
        }
        return fileIsAMusic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getFileAsByteArray(File file) throws IOException {
        log.debug("getFileAsByteArray ...");
        byte[] bytes;
        ByteArrayOutputStream baos = null;
        InputStream inputStream = null;
        try {
            byte[] buffer = new byte[4096];
            baos = new ByteArrayOutputStream();
            inputStream = new FileInputStream(file);
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
        } catch (Exception e) {
            log.trace("getFileAsByteArray : reading file failed");
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                // swallow, since not that important
                log.trace("getFileAsByteArray : closing ByteArrayOutputStream failed");
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                // swallow, since not that important
                log.trace("getFileAsByteArray : closing InputStream failed");
            }
        }
        if (baos != null) {
            bytes = baos.toByteArray();
            log.debug("Music.Byte[].length = {}", bytes.length);
        } else {
            throw new RuntimeException("Failed to convert File to byte[]");
        }
        return bytes;
    }

    private void resetTmpFile() {
        if (tmpFile != null) {
            tmpFile.delete();
        }
        try {  // .mp3 extendsion required by other libs
            tmpFile = new File(appFolder + File.separator + "tmp" + DOT_MP3);
            tmpFile.createNewFile();
            tmpFile.deleteOnExit();
        } catch (IOException ex) {
            log.error(ex.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File buildTmpMusicFile(Byte[] musicBytes) throws Exception {
        log.debug("buildTmpMusicFile ...");
        resetTmpFile();

        // then we copy mp3 bytes into it
        byte[] bytes = new byte[musicBytes.length];
        for (int i = 0; i < musicBytes.length; i++) {
            bytes[i] = musicBytes[i];
        }
        FileOutputStream fileOuputStream = new FileOutputStream(tmpFile);
        fileOuputStream.write(bytes);
        fileOuputStream.close();
        log.debug("buildTmpMusicFile DONE");
        return tmpFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<File> buildTmpMusicFilesForInstall(Catalog catalog) throws Exception {
        log.debug("buildTmpMusicFilesForInstall ...");
        if (new File(appFolder + ROOT_FOLDER_TMP).exists()) {
            deleteRecursive(appFolder + ROOT_FOLDER_TMP);
        }
        new File(appFolder + ROOT_FOLDER_TMP).mkdirs();

        List<File> files = new ArrayList<File>(catalog.size());
        for (Music music : catalog.getMusics()) {
            Byte[] musicBytes = music.getFileBytes();
            if (musicBytes != null && musicBytes.length != 0) {
                File file = new File(appFolder + ROOT_FOLDER_TMP + File.separator + music.getFileName());
                byte[] bytes = new byte[musicBytes.length];
                for (int i = 0; i < musicBytes.length; i++) {
                    bytes[i] = musicBytes[i];
                }
                FileOutputStream fileOuputStream = new FileOutputStream(file);
                fileOuputStream.write(bytes);
                fileOuputStream.close();
                file.deleteOnExit();
                files.add(file);
            }
        }
        log.debug("buildTmpMusicFilesForInstall DONE");
        return files;
    }

    private void deleteRecursive(String path) {
        log.trace("deleteRecursive : {} ...", path);
        File fileOrDir = new File(path);
        if (fileOrDir.isFile()) {
            fileOrDir.delete();
        } else if (fileOrDir.isDirectory()) {
            for (File file : fileOrDir.listFiles()) {
                try {
                    deleteRecursive(file.getCanonicalPath());
                } catch (IOException ex) {
                    log.error("deleteRecursive failed");
                }
            }
        }
        log.trace("deleteRecursive : {} DONE", path);
    }

    @Override
    public File getFileOfLocalMusic(Music localMusic) {
        log.debug("getFileOfLocalMusic ...");
        File file = null;
        if (localMusic != null) {
            String ownerLogin = appModel.getProfile().getUserInfo().getLogin();
            if (ownerLogin != null && !ownerLogin.isEmpty() && appModel.getProfile().getUserInfo().getPeerId().equals(localMusic.getOwnerPeerId())) {
                file = new File(appFolder + File.separator + ROOT_FOLDER_USERS + File.separator + ownerLogin + File.separator + localMusic.getFileName());
            }
        }
        log.debug("getFileOfLocalMusic DONE");
        return file;
    }

    @Override
    public String computeRealName(String name) {
        log.trace("computeRealName ...");
        if (name != null && !name.endsWith(DOT_MP3)) {
            name = name + DOT_MP3;
        }
        log.trace("computeRealName DONE ({})", name);
        return name;
    }

    @Override
    public String computeFileName(String musicActualRealName, String realname) {
        log.trace("computeFileName ...");
        String filename = null;
        if (realname != null && musicActualRealName != null) {
            if (!realname.endsWith(DOT_MP3)) {
                realname = realname + DOT_MP3;
            }
            filename = realname;
            if (!musicActualRealName.equals(realname)) {
                //  check if realName is already in use, change value to set if needed
                List<String> sameRealnameFilenames = new ArrayList<String>();
                for (Music m : appModel.getLocalCatalog().getMusics()) {
                    if (m.getRealName().equals(realname)) {
                        sameRealnameFilenames.add(m.getFileName());
                    }
                }
                if (!sameRealnameFilenames.isEmpty()) {
                    int newSameRealNameIndex = computeNextUnusedIndex(sameRealnameFilenames);
                    filename = realname.substring(0, realname.lastIndexOf(DOT_MP3));
                    filename = filename.trim();
                    filename += " (" + newSameRealNameIndex + ")" + DOT_MP3;
                }
            }
        }
        log.trace("computeFileName DONE ({})", filename);
        return filename;
    }

    private int computeNextUnusedIndex(List<String> sameRealnameFilenames) {
        int newSameRealNameIndex = 1;
        for (String name : sameRealnameFilenames) {
            int ob = name.lastIndexOf('(');
            int cb = name.lastIndexOf(')');
            if (ob > 0
                    && cb > 0
                    && ob < cb
                    && cb - ob >= 1
                    && cb - ob < 3) {
                String numStr = name.substring(ob + 1, cb);
                if (numStr != null && !numStr.isEmpty()) {
                    Integer num = null;
                    try {
                        num = Integer.parseInt(numStr);
                    } catch (NumberFormatException nfe) {
                        log.warn("Failed at extracting same real name index");
                    }
                    if (num != null && num > newSameRealNameIndex) {
                        newSameRealNameIndex = num;
                    }
                }
            }
        }
        return newSameRealNameIndex;
    }

    @Override
    public void saveToFile(SharUTCFile sharUTCFile, Object localCatalog) {
        log.debug("saveToFile ({}) ...", sharUTCFile.getFilename());
        StringBuilder builder = new StringBuilder(appFolder).append(ROOT_FOLDER_USERS).append(File.separator).append(getCurrentUserLogin()).append(File.separator).append(sharUTCFile.getFilename());
        try {
            mapper.writeValue(new File(builder.toString()), localCatalog);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        log.debug("saveToFile ({}) DONE", sharUTCFile.getFilename());
    }

    protected final String getCurrentUserLogin() {
        log.trace("getCurrentUserLogin ({})", appModel.getProfile().getUserInfo().getLogin());
        return appModel.getProfile().getUserInfo().getLogin();
    }

    @Override
    public <T> T readFile(SharUTCFile sharUTCFile, Class<T> clazz) {
        log.debug("readFile ({}) ...", sharUTCFile.getFilename());
        StringBuilder builder = new StringBuilder(appFolder).append(ROOT_FOLDER_USERS).append(File.separator).append(getCurrentUserLogin()).append(File.separator).append(sharUTCFile.getFilename());
        T object = null;
        try {
            object = mapper.readValue(new File(builder.toString()), clazz);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        log.debug("readFile ({}) DONE", sharUTCFile.getFilename());
        return object;
    }

    @Override
    public Profile readProfileFile(String login) {
        log.debug("readProfileFile ({}) ...", login);
        StringBuilder builder = new StringBuilder(appFolder).append(ROOT_FOLDER_USERS).append(File.separator).append(login).append(File.separator).append(SharUTCFile.PROFILE.getFilename());
        Profile profile = null;
        try {
            profile = mapper.readValue(new File(builder.toString()), Profile.class);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        log.debug("readProfileFile ({}) DONE", login);
        return profile;
    }

    @Override
    public void createFile(byte[] bytes, String fileName) {
        log.debug("createFile ({}) ...", fileName);
        try {
            //convert array of bytes into file
            FileOutputStream fileOuputStream =
                    new FileOutputStream(fileName);
            fileOuputStream.write(bytes);
            fileOuputStream.close();
        } catch (Exception ex) {
            log.error("Error while creating file '{}' from bytes", fileName, ex.toString());
            throw new RuntimeException("Error while creating file '" + fileName + "' from bytes", ex);
        }
        log.debug("createFile ({}) DONE", fileName);
    }

    @Override
    public void createAccountFolder(String login) {
        log.debug("createAccountFolder ({}) ...", login);
        if (!new File(appFolder + ROOT_FOLDER_USERS + File.separator + login).exists()) {
            log.debug("createAccountFolder ({}) : directory doesn't exist, making it", login);
            new File(appFolder + ROOT_FOLDER_USERS + File.separator + login).mkdir();
        }
    }
}

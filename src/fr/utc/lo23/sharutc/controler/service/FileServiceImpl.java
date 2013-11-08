package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Singleton
public class FileServiceImpl implements FileService {

    private static String appFolder;
    private static final int BUFFER_SIZE = 2048;
    private static final int COMPRESSION_LEVEL = 9;
    private static final Logger log = LoggerFactory
            .getLogger(FileServiceImpl.class);
    private final AppModel appModel;
    private static final String[] AUTHORIZED_MUSIC_FILE_TYPE = {"mp3"};
    private File tmpFile;

    @Inject
    public FileServiceImpl(AppModel appModel) {
        this.appModel = appModel;
        try {
            appFolder = new File(".").getCanonicalPath();
        } catch (IOException ex) {
            log.error("Can't write in AppFolder");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void importFile(String path, String password) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportFile(String srcPath, String destPath) throws IOException {
        List<String> fileList = new ArrayList<String>();
        byte data[] = new byte[BUFFER_SIZE];

        getFilesRec(fileList, new File(srcPath), srcPath);

        //Traitement de la sortie
        FileOutputStream dest = new FileOutputStream(destPath);
        BufferedOutputStream outBuffer = new BufferedOutputStream(dest);
        ZipOutputStream outStream = new ZipOutputStream(outBuffer);
        outStream.setMethod(ZipOutputStream.DEFLATED);
        outStream.setLevel(COMPRESSION_LEVEL);

        for (String path : fileList) {
            //traitement de l'entree
            FileInputStream srcFile = new FileInputStream(srcPath + File.separator + path);
            BufferedInputStream inBuffer = new BufferedInputStream(srcFile, BUFFER_SIZE);
            ZipEntry entry = new ZipEntry(path);
            outStream.putNextEntry(entry);

            int count = 0;
            while ((count = inBuffer.read(data, 0, BUFFER_SIZE)) != -1) {
                outStream.write(data, 0, count);
            }

            inBuffer.close();
            outStream.closeEntry();
        }
        outStream.close();
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
     * {@inheritDoc}
     */
    @Override
    public Music readFile(File file) throws Exception {
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
            track = tag.getFirst(FieldKey.TRACK);
            trackLength = ah.getTrackLength();
            frames = mp3AudioHeader.getNumberOfFrames();
        } catch (Exception ex) {
            log.error("Unable to read music file informations : {}", ex.toString());
            title = null;
            artist = null;
            album = null;
            track = null;
            trackLength = null;
            frames = 0L;
        }
        return new Music(appModel.getProfile().getNewMusicId(),
                appModel.getProfile().getUserInfo().getPeerId(),
                getFileAsByteArray(file),
                file.getName(), file.getName(), file.hashCode(), title, artist, album, track,
                trackLength, frames);
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
    public Byte[] getFileAsByteArray(File file) throws IOException {
        ByteArrayOutputStream baos = null;
        InputStream inputStream = null;
        try {
            byte[] buffer = new byte[4096];
            baos = new ByteArrayOutputStream();
            inputStream = new FileInputStream(file);
            int read = 0;
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
        Byte[] array;
        if (baos != null) {
            byte[] tmpArray = baos.toByteArray();
            array = new Byte[tmpArray.length];
            for (int i = 0; i < tmpArray.length; i++) {
                array[i] = new Byte(tmpArray[i]);
            }
            log.debug("Music.Byte[].length = {}", array.length);
        } else {
            throw new IOException();
        }
        return array;
    }

    private void resetTmpFile() {
        if (tmpFile == null) {
            // .mp3 extendsion required by other libs
            tmpFile = new File(appFolder + "\\tmp.mp3");
            tmpFile.deleteOnExit();
        }
        tmpFile.delete();
        try {
            tmpFile.createNewFile();
        } catch (IOException ex) {
            log.error(ex.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File buildTmpMusicFile(Byte[] musicBytes) throws Exception {
        resetTmpFile();

        // then we copy mp3 bytes into it
        byte[] bytes = new byte[musicBytes.length];
        for (int i = 0; i < musicBytes.length; i++) {
            bytes[i] = musicBytes[i];
        }
        FileOutputStream fileOuputStream = new FileOutputStream(tmpFile);
        fileOuputStream.write(bytes);
        fileOuputStream.close();
        return tmpFile;
    }
}

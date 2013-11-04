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
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Singleton
public class FileServiceImpl implements FileService {
    private static final int BUFFER_SIZE        = 2048;
    private static final int COMPRESSION_LEVEL  = 9;
    private String mSourceFolder;
    
    private static final Logger log = LoggerFactory
            .getLogger(FileServiceImpl.class);
    private final AppModel appModel;
    private static final String[] AUTHORIZED_MUSIC_FILES = {"mp3"};

    @Inject
    public FileServiceImpl(AppModel appModel) {
        this.appModel = appModel;
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
        mSourceFolder = srcPath;
        
        getFilesRec(fileList, new File(srcPath));
        
        //Traitement de la sortie
        FileOutputStream dest = new FileOutputStream(destPath);
        BufferedOutputStream outBuffer = new BufferedOutputStream(dest);
        ZipOutputStream outStream = new ZipOutputStream(outBuffer);
        outStream.setMethod(ZipOutputStream.DEFLATED);
        outStream.setLevel(COMPRESSION_LEVEL);
        
        for(String path: fileList){
            //traitement de l'entree
            FileInputStream srcFile = new FileInputStream(srcPath + File.separator + path);
            BufferedInputStream inBuffer = new BufferedInputStream(srcFile, BUFFER_SIZE);
            ZipEntry entry = new ZipEntry(path);
            outStream.putNextEntry(entry);
            
            int count = 0;
            while( (count = inBuffer.read(data, 0, BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);
            
            inBuffer.close();
            outStream.closeEntry();
        }
        outStream.close();
    }
    
    /**
     * Traverse a directory and get all files,
     * and add the file into fileList 
     * @param fileList list of file names
     * @param node file or directory
     */
    private void getFilesRec(List<String> fileList, File node){
        if(node.isFile()){
            String filePath = node.getAbsoluteFile().toString();
            //Format the file path for zip
            filePath = filePath.substring(mSourceFolder.length()+1, filePath.length());
            fileList.add(filePath);
        }

        if(node.isDirectory()){
            String[] subNote = node.list();
            for(String filename : subNote)
                getFilesRec(fileList, new File(node, filename));
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
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            AudioHeader audioHeader = audioFile.getAudioHeader();
            return new Music(appModel.getProfile().getNewMusicId(),
                    appModel.getProfile().getUserInfo().getPeerId(),
                    getFileAsByteArray(file),
                    file.getName(), file.getName(), file.hashCode(),
                    tag.getFirst(FieldKey.TITLE),
                    tag.getFirst(FieldKey.ARTIST),
                    tag.getFirst(FieldKey.ALBUM),
                    tag.getFirst(FieldKey.TRACK),
                    audioHeader.getTrackLength());
        } catch (Exception ex) {
            log.error("Unable to read music file informations : {}", ex.toString());
            return new Music(appModel.getProfile().getNewMusicId(),
                    appModel.getProfile().getUserInfo().getPeerId(),
                    getFileAsByteArray(file),
                    file.getName(), file.getName(), file.hashCode(),
                    null, null, null, null, null);
        }
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
        for (String extension : AUTHORIZED_MUSIC_FILES) {
            if (file.getName().endsWith("." + extension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Byte[] getFileAsByteArray(File file) throws IOException {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null) {
                    ous.close();
                }
            } catch (IOException e) {
                // swallow, since not that important
            }
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                // swallow, since not that important
            }
        }
        byte[] tmpArray = ous.toByteArray();
        Byte[] array = new Byte[tmpArray.length];
        for (int i = 0; i < tmpArray.length; i++) {
            array[i] = new Byte(tmpArray[i]);
        }
        return array;
    }
}

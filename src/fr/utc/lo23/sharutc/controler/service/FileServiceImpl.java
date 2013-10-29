package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.io.File;
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
    public void writeExportFile(String path) {
        log.warn("Not supported yet.");
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
                    file,
                    null,
                    tag.getFirst(FieldKey.TITLE),
                    tag.getFirst(FieldKey.ARTIST),
                    tag.getFirst(FieldKey.ALBUM),
                    tag.getFirst(FieldKey.TRACK),
                    audioHeader.getTrackLength());
        } catch (Exception ex) {
            log.error("Unable to read music file informations : {}", ex.toString());
            return new Music(appModel.getProfile().getNewMusicId(),
                    appModel.getProfile().getUserInfo().getPeerId(),
                    file, null, null, null, null, null, null);
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
}

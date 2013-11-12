package fr.utc.lo23.sharutc.controler.service;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import static fr.utc.lo23.sharutc.controler.service.FileService.ROOT_FOLDER_USERS;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class FileServiceMock extends FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory
            .getLogger(FileServiceMock.class);
    private final ObjectWriter objectWriter;

    @Inject
    public FileServiceMock(AppModel appModel) {
        super(appModel);
        objectWriter = super.mapper.writerWithDefaultPrettyPrinter();
    }

    /**
     *
     * @param path
     */
    @Override
    public void importWholeProfile(String path) throws Exception {
        super.importWholeProfile(path);
    }

    /**
     *
     * @param srcPath
     * @param destPath
     * @throws IOException
     */
    @Override
    public void exportFile(String srcPath, String destPath) throws IOException {
        super.exportFile(srcPath, destPath);
    }

    /**
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public Music createMusicFromFile(File file) throws Exception {
        return super.createMusicFromFile(file);
    }

    /**
     *
     * @param realName
     * @return
     */
    @Override
    public byte[] getFileAsByteArray(File file) throws IOException {
        return super.getFileAsByteArray(file);
    }

    @Override
    public File buildTmpMusicFile(Byte[] musicBytes) throws Exception {
        return super.buildTmpMusicFile(musicBytes);
    }

    @Override
    public void saveToFile(SharUTCFile sharUTCFile, Object localCatalog) {
        super.saveToFile(sharUTCFile, localCatalog);
        try {
            StringWriter stringWriter = new StringWriter();
            objectWriter.writeValue(stringWriter, localCatalog);
            log.info("Writen in {} :\n{}", sharUTCFile.getFilename(), stringWriter.toString());
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }
}

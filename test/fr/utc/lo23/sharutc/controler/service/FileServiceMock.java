package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class FileServiceMock extends FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory
            .getLogger(FileServiceMock.class);

    @Inject
    public FileServiceMock(AppModel appModel) {
        super(appModel);
    }

    /**
     *
     * @param path
     * @param password
     */
    @Override
    public void importFile(String path, String password) {
        super.importFile(path, password);
    }

    /**
     *
     * @param path
     */
    @Override
    public void writeExportFile(String path) {
        super.writeExportFile(path);
    }

    /**
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public Music readFile(File file) throws Exception {
        return super.readFile(file);
    }

    /**
     *
     * @param realName
     * @return
     */
    @Override
    public Byte[] getFileAsByteArray(File file) throws IOException {
        return super.getFileAsByteArray(file);
    }
}

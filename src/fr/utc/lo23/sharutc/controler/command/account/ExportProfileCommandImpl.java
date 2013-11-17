package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.ErrorMessage;
import java.io.IOException;

/**
 * implementation of export profile command
 * {@inheritDoc}
 */
public class ExportProfileCommandImpl implements ExportProfileCommand {
    private final FileService fs;
    private final AppModel appModel;
    private String mSrcFile;
    private String mDestFolder;
    
    /**
     * Constructor
     * @param fs
     * @param appModel
     */
    @Inject
    public ExportProfileCommandImpl(FileService fs, AppModel appModel) {
        this.fs = fs;
        this.appModel = appModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSrcFile() {
        return mSrcFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSrcFile(String srcFile) {
        mSrcFile = srcFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDestFolder() {
        return mDestFolder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDestFolder(String destFolder) {
        mDestFolder = destFolder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        try {
            fs.exportFile(mSrcFile, mDestFolder);
        } catch (IOException ex) {
            appModel.getErrorBus().pushErrorMessage(new ErrorMessage(ex.getMessage()));
        }
    }
}

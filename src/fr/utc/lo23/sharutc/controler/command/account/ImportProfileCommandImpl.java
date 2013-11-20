
package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.ErrorMessage;

/**
 *
 *
 */
public class ImportProfileCommandImpl implements ImportProfileCommand{
    private final FileService fs;
    private final AppModel appModel;
    private String mPath;
    private boolean mForce;
    
    /**
     * 
     * @param fs
     * @param appModel 
     */
    @Inject
    public ImportProfileCommandImpl(FileService fs, AppModel appModel) {
        this.fs = fs;
        this.appModel = appModel;
        mForce = false;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getPath() {
        return mPath;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setPath(String path) {
        mPath = path;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isForce() {
        return mForce;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setForce(boolean force) {
        this.mForce = force;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void execute() {
        try {
            fs.importWholeProfile(mPath, mForce);
        } catch (Exception ex) {
            appModel.getErrorBus().pushErrorMessage(new ErrorMessage(ex.getMessage()));
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.ErrorMessage;

/**
 *
 * @author Olivier
 */
public class ImportProfileCommandImpl implements ImportProfileCommand{
    private final FileService fs;
    private final AppModel appModel;
    private String mPath;
    private boolean mForce;
    
    @Inject
    public ImportProfileCommandImpl(FileService fs, AppModel appModel) {
        this.fs = fs;
        this.appModel = appModel;
        mForce = false;
    }

    @Override
    public String getPath() {
        return mPath;
    }

    @Override
    public void setPath(String path) {
        mPath = path;
    }

    public boolean isForce() {
        return mForce;
    }

    public void setForce(boolean Force) {
        this.mForce = Force;
    }

    @Override
    public void execute() {
        try {
            fs.importWholeProfile(mPath, mForce);
        } catch (Exception ex) {
            appModel.getErrorBus().pushErrorMessage(new ErrorMessage(ex.getMessage()));
        }
    }
}

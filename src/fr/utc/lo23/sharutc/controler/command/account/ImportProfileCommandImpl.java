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
    
    @Inject
    public ImportProfileCommandImpl(FileService fs, AppModel appModel, String path) {
        this.fs = fs;
        this.appModel = appModel;
        mPath = path;
    }

    @Override
    public String getPath() {
        return mPath;
    }

    @Override
    public void setPath(String path) {
        mPath = path;
    }

    @Override
    public void execute() {
        try {
            fs.importWholeProfile(mPath);
        } catch (Exception ex) {
            appModel.getErrorBus().pushErrorMessage(new ErrorMessage(ex.getMessage()));
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class EditUserInfoCommandImpl implements EditUserInfoCommand {
    private static final Logger log = LoggerFactory
        .getLogger(EditUserInfoCommandImpl.class);
    private final UserService userService;
    private final AppModel appModel;
    private UserInfo mUserInfo;
    
    /**
     * Constructor
     * @param fs - File Service
     * @param appModel - Application Model
     */
    @Inject
    public EditUserInfoCommandImpl(AppModel appModel, UserService userService) {
        this.appModel = appModel;
        this.userService = userService;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }
    
    /**
     * Get the current online profile, then update its user info.
     * Also persist new data
     */
    @Override
    public void execute() {
        log.info("EditUserInfoCommandImpl ...");

        // update user info.
        appModel.getProfile().setUserInfo(mUserInfo);
        userService.saveProfileFiles();

        log.info("EditUserInfoCommandImpl DONE");
    }
    
}

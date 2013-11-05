/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.userdata.Profile;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;

/**
 *
 * @author DeSweetChebs
 */
public class ProfileCreationCommandImpl implements ProfileCreationCommand {
    
    private Profile mProfile = new Profile();
     
    @Override
    public UserInfo getUserInfo(){
        return mProfile.getUserInfo();
    }

    /**
     *
     * @param userInfo
     */
    @Override
    public void setUserInfo(UserInfo userInfo){
        mProfile.setUserInfo(userInfo);
    }

    @Override
    public void execute() {
        UserService userservice = null;
        userservice.createProfile(mProfile.getUserInfo());
    }
 
}

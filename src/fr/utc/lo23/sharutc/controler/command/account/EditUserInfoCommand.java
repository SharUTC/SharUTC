package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;

/**
 * Edit the user info. of the current online user
 */
public interface EditUserInfoCommand extends Command {
    
    /**
     * Edit the user info. of the current online user
     *
     * @param userInfo new information about the online user
     */
    public void setUserInfo(UserInfo userInfo);
}

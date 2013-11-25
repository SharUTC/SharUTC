package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;

/**
 * Command to create an account
 */
public interface AccountCreationCommand extends Command {

    /**
     * Return user's information
     * 
     * @return user's information
     */
    public UserInfo getUserInfo();

    /**
     * Set user's information
     * 
     * @param userInfo
     */
    public void setUserInfo(UserInfo userInfo);
}

package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;

/**
 *
 */
public interface AddUserCommand extends Command{
    /**
     * Return the contact
     *
     * @return the contact
     */
    public UserInfo getUserInfo();

    /**
     * Set the contact
     *
     * @param userInfo
     */
    public void setContact(UserInfo userInfo);
}

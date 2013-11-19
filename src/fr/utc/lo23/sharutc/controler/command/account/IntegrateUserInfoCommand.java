package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;

/**
 * Add or update local data about a peer
 */
public interface IntegrateUserInfoCommand extends Command {

    /**
     * Return information about remote peer
     *
     * @return information about remote peer
     */
    public UserInfo getUserInfo();

    /**
     * Set information about remote peer
     *
     * @param userInfo information about remote peer
     */
    public void setUserInfo(UserInfo userInfo);
}

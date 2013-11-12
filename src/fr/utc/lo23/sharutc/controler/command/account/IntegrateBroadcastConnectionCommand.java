package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;

/**
 * 
 * @author Tudor Luchiancenco <tudorluchy@gmail.com>
 */
public interface IntegrateBroadcastConnectionCommand extends Command {

    /**
     * Return information about remote peer
     *
     * @return information about remote peer
     */
    public UserInfo getUserInfo();

    /**
     * Set informations about remote peer
     *
     * @param userInfo information about remote peer
     */
    public void setUserInfo(UserInfo userInfo);
}

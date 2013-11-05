package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;

/**
 * TODO: add comments
 */
public interface IntegrateConnectionCommand extends Command {

    /**
     * Return informations about remote peer
     *
     * @return informations about remote peer
     */
    public UserInfo getUserInfo();

    /**
     * Set informations about remote peer
     *
     * @param userInfo informations about remote peer
     */
    public void setUserInfo(UserInfo userInfo);
}

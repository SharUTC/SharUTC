/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.music.SendTagMapCommandImpl;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tudor Luchiancenco <tudorluchy@gmail.com>
 */
public class IntegrateConnectionCommandImpl implements IntegrateConnectionCommand {

    private static final Logger log = LoggerFactory
        .getLogger(SendTagMapCommandImpl.class);
    private final AppModel mAppModel;
    private final UserService mUserService;
    private UserInfo mUserInfo;

    /**
     *
     * @param appModel
     * @param us
     */
    @Inject
    public IntegrateConnectionCommandImpl(AppModel appModel, UserService us) {
        this.mAppModel = appModel;
        this.mUserService = us;
    }

    /**
     * Get user info
     * 
     * @return 
     */
    @Override
    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    /**
     * Set user info
     * 
     * @param userInfo 
     */
    @Override
    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    /**
     * Add user info to model
     * Add user info to active peer list
     */
    @Override
    public void execute() {
        mUserService.integrateConnection(mUserInfo);
        // TODO ip adresse ?
        Peer peer = new Peer(mUserInfo.getPeerId(), mUserInfo.getLogin());
        mAppModel.getActivePeerList().update(peer);
    }
}

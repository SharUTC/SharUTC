package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class IntegrateConnectionCommandImpl implements IntegrateConnectionCommand {

    private static final Logger log = LoggerFactory
        .getLogger(IntegrateConnectionCommandImpl.class);
    private final AppModel appModel;
    private final UserService userService;
    private UserInfo mUserInfo;

    /**
     * Construct IntegrateConnectionCommand
     *
     * @param appModel
     * @param us
     */
    @Inject
    public IntegrateConnectionCommandImpl(AppModel appModel, UserService us) {
        this.appModel = appModel;
        this.userService = us;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    /**
     * Add user info to model and update active peer list
     */
    @Override
    public void execute() {
        log.info("IntegrateConnectionCommandImpl ...");
        // add user info to model
        userService.integrateConnection(mUserInfo);
        // update active peer list
        Peer peer = mUserInfo.toPeer();
        appModel.getActivePeerList().update(peer);
        log.info("IntegrateConnectionCommandImpl DONE");
    }
}

package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class IntegrateBroadcastConnectionCommandImpl implements IntegrateConnectionCommand {

    private static final Logger log = LoggerFactory
            .getLogger(IntegrateBroadcastConnectionCommandImpl.class);
    private final AppModel appModel;
    private final UserService userService;
    private final NetworkService networkService;
    private UserInfo mUserInfo;

    /**
     *
     * @param appModel
     * @param us
     */
    @Inject
    public IntegrateBroadcastConnectionCommandImpl(AppModel appModel, UserService us, NetworkService ns) {
        this.appModel = appModel;
        this.userService = us;
        this.networkService = ns;
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
     * Add user info to model Add user info to active peer list
     */
    @Override
    public void execute() {
        log.info("IntegrateBroadscastConnectionCommandImpl ...");
        userService.integrateConnection(mUserInfo);
        // TODO ip adresse ?
        Peer peer = new Peer(mUserInfo.getPeerId(), mUserInfo.getLogin());
        appModel.getActivePeerList().update(peer);
        networkService.sendConnectionResponse(peer, appModel.getProfile().getUserInfo());
        log.info("IntegrateBroadscastConnectionCommandImpl DONE");
    }
}

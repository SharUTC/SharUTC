package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class IntegrateUserInfoCommandImpl implements IntegrateUserInfoCommand {

    private static final Logger log = LoggerFactory
        .getLogger(IntegrateUserInfoCommandImpl.class);
    private final UserService userService;
    private UserInfo mUserInfo;

    
    @Inject
    public IntegrateUserInfoCommandImpl(UserService userService) {
        this.userService = userService;
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
     * Add user info to model and update active peer list & contact
     */
    @Override
    public void execute() {
        log.info("IntegrateUserInfoCommandImpl ...");
        
        // update contacts & active peers
        userService.updateConnectedPeers(mUserInfo);
        
        log.info("IntegrateUserInfoCommandImpl DONE");
    }
}

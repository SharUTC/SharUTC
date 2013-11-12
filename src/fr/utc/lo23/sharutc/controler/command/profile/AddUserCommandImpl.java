package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.account.IntegrateDisconnectionCommandImpl;
import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexandre
 */
public class AddUserCommandImpl implements AddUserCommand {
    private static final Logger log = LoggerFactory
            .getLogger(IntegrateDisconnectionCommandImpl.class);
    final private UserService mUserService;
    private UserInfo mUserInfo;

    @Inject
    public AddUserCommandImpl(UserService mUserService, UserInfo mUserInfo) {
        this.mUserService = mUserService;
        this.mUserInfo = mUserInfo;
    }
    
    @Override
    public void execute() {
        log.info("Add user to connected peer list...");
        this.mUserService.updateConnectedPeers(this.mUserInfo);
        log.info("User added.");
    }
}

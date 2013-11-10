package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.account.IntegrateDisconnectionCommandImpl;
import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
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

    @Inject
    public AddUserCommandImpl(UserService mUserService) {
        this.mUserService = mUserService;
    }
    
    @Override
    public void execute() {
        log.info("Add user to connected peer list...");
        //
        log.info("User added.");
    }
}

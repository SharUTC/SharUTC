package fr.utc.lo23.sharutc.controler.command.profile;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexandre
 */
public class IntegrateDisconnectionCommandImpl implements IntegrateDisconnectionCommand {
    private static final Logger log = LoggerFactory
            .getLogger(IntegrateDisconnectionCommandImpl.class);
    final private UserService mUserService;
    
    /**
     * {@inheritDoc}
     */
    @Inject
    public IntegrateDisconnectionCommandImpl(UserService mUserService) {
        this.mUserService = mUserService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("Disconnection...");
        this.mUserService.disconnectionRequest();
        log.info("Disconnected.");  
    }
}
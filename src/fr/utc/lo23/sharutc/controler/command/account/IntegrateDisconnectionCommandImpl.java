package fr.utc.lo23.sharutc.controler.command.account;

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
    private long mPeerId;
    
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
    public long getPeerId() {
        return mPeerId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPeerId(long peerId) {
        this.mPeerId = peerId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("Remove Peer...");
        this.mUserService.removeFromConnectedPeers(mPeerId);
        log.info("Peer removed..");  
    }

    
}
package fr.utc.lo23.sharutc.controler.command.profile;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class AddContactCommandImpl implements AddContactCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddContactCommandImpl.class);
    private Peer mPeer;
    final private UserService mUserService;

    /**
     * {@inheritDoc}
     */
    @Inject
    public AddContactCommandImpl(UserService userService) {
        this.mUserService = userService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Peer getPeer() {
        return mPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPeer(Peer peer) {
        this.mPeer = peer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("AddContactCommand ...");
        mUserService.addContact(mPeer);
        log.info("AddContactCommand DONE");
    }
}

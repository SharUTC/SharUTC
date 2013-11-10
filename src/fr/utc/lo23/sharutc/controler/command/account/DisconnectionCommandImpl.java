/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO : add comments
 *
 * @author Alexandre
 */
public class DisconnectionCommandImpl implements DisconnectionCommand {

    private static final Logger log = LoggerFactory
            .getLogger(IntegrateDisconnectionCommandImpl.class);
    final private UserService mUserService;

    /**
     * {@inheritDoc}
     */
    @Inject
    public DisconnectionCommandImpl(UserService mUserService) {
        this.mUserService = mUserService;
    }

    @Override
    public void execute() {
        log.info("DisconnectionCommand ...");

        mUserService.disconnectionRequest();

        log.info("DisconnectionCommand DONE");
    }
}

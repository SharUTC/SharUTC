/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexandre
 */
public class DisconnectionCommandImpl implements DisconnectionCommand{
    private static final Logger log = LoggerFactory
            .getLogger(IntegrateDisconnectionCommandImpl.class);
    final private UserService mUserService;
    final private NetworkService mNetworkService;
    
    /**
     * {@inheritDoc}
     */
    @Inject
    public DisconnectionCommandImpl(UserService mUserService, NetworkService mNetworkService) {
        this.mUserService = mUserService;
        this.mNetworkService = mNetworkService;
    }

    @Override
    public void execute() {
        log.info("Disconnection...");
        this.mUserService.disconnectionRequest(this.mNetworkService);
        log.info("Disconnected.");
    }
    
}

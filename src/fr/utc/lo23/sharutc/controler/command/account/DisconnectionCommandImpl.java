/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
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
    final private UserService userService;
    final private NetworkService networkService;
    private final MusicService musicService;

    /**
     * @param userService
     * @param musicService
     * @param networkService
     */
    @Inject
    public DisconnectionCommandImpl(UserService userService, MusicService musicService, NetworkService networkService) {
        this.userService = userService;
        this.musicService = musicService;
        this.networkService = networkService;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void execute() {
        log.info("DisconnectionCommand ...");
        musicService.saveUserMusicFile();
        musicService.saveUserRightsListFile();
        userService.disconnectionRequest();
        // Notify network
        networkService.disconnectionBroadcast();

        log.info("DisconnectionCommand DONE");
    }
}

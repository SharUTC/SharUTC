package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.PlayerService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class DisconnectionCommandImpl implements DisconnectionCommand {

    private final static Logger log = LoggerFactory
            .getLogger(DisconnectionCommandImpl.class);
    private final UserService userService;
    private final NetworkService networkService;
    private final MusicService musicService;
    private final AppModel appModel;
    private final PlayerService playerService;

    /**
     * Construct DisconnectionCommandImpl.
     *
     * @param appModel
     * @param userService
     * @param musicService
     * @param networkService
     */
    @Inject
    public DisconnectionCommandImpl(AppModel appModel, UserService userService, MusicService musicService, NetworkService networkService, PlayerService playerService) {
        this.appModel = appModel;
        this.userService = userService;
        this.musicService = musicService;
        this.networkService = networkService;
        this.playerService = playerService;
    }

    /**
     * Save the user music files, the list of user's rights, his profile
     * information and notify the network of its disconnection. Then stop all
     * network threads.
     */
    @Override
    public void execute() {
        log.info("DisconnectionCommand ...");
        musicService.saveUserMusicFile();
        musicService.saveUserRightsListFile();
        // Notify network
        networkService.disconnectionBroadcast();

        userService.disconnectionRequest(); //Save and clear profile

        appModel.getActivePeerList().clear();
        appModel.getRemoteUserCatalog().clear();
        appModel.getSearchResults().clear();
        appModel.getTmpCatalog().clear();

        //Turn down network threads
        networkService.stop();

        //Turn down music player by clearing its playlist
        playerService.getPlaylist().clear();
        
        log.info("DisconnectionCommand DONE");
    }
}

package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class ConnectionRequestCommandImpl implements ConnectionRequestCommand {

    private static final Logger log = LoggerFactory
            .getLogger(ConnectionRequestCommandImpl.class);
    private String mLogin;
    private String mPassword;
    private final UserService userService;
    private final MusicService musicService;
    private final AppModel appModel;

    @Inject
    public ConnectionRequestCommandImpl(UserService userservice, MusicService musicService, AppModel appModel) {
        this.userService = userservice;
        this.musicService = musicService;
        this.appModel = appModel;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public String getLogin() {
        return mLogin;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void setLogin(String login) {
        mLogin = login;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return mPassword;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void setPassword(String password) {
        mPassword = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("AccountCreationCommand ...");

        userService.connectionRequest(mLogin, mPassword);
        if (appModel.getProfile() != null) {
            musicService.loadUserMusicFile();
            musicService.loadUserRightsListFile();
        }

        log.info("AccountCreationCommand DONE");
    }
}

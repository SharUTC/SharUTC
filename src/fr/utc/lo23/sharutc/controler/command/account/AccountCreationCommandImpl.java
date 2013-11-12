package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class AccountCreationCommandImpl implements AccountCreationCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AccountCreationCommandImpl.class);
    private UserInfo mUserInfo;
    private final UserService userService;
    private final MusicService musicService;

    @Inject
    public AccountCreationCommandImpl(UserService userService, MusicService musicService) {
        this.userService = userService;
        this.musicService = musicService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("AccountCreationCommand ...");
        musicService.createAndSetCatalog();
        musicService.createAndSetRightsList();
        // let userService.createAndSetProfile(mUserInfo) call after
        // musicService calls, we only inform once the ui that the user has
        // changed by setting profile attribute in appModel
        userService.createAndSetProfile(mUserInfo);

        userService.saveProfileFiles();
        musicService.saveUserMusicFile();
        log.info("AccountCreationCommand DONE");
    }
}

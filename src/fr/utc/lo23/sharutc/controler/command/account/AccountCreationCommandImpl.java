package fr.utc.lo23.sharutc.controler.command.account;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.ErrorMessage;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class AccountCreationCommandImpl implements AccountCreationCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AccountCreationCommandImpl.class);
    private AppModel appModel;
    private UserInfo mUserInfo;
    private final UserService userService;
    private final MusicService musicService;
    private final FileService fileService;

    @Inject
    public AccountCreationCommandImpl(AppModel appModel, UserService userService, FileService fileService, MusicService musicService) {
        this.appModel = appModel;
        this.userService = userService;
        this.fileService = fileService;
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
        StringBuilder builder = new StringBuilder(fileService.getAppFolder()).append(FileService.ROOT_FOLDER_USERS).append(File.separator).append(mUserInfo.getLogin());
        File file = new File(builder.toString());
        
        if(file.exists() == false){
            // FIXME userProfile validation ??
            // it should be performed by the command or a service,
            // not in ui, use ErrorMessage and ErrorBus

            musicService.createAndSetCatalog();
            musicService.createAndSetRightsList();
            // let userService.createAndSetProfile(mUserInfo) call AFTER
            // musicService calls, we only inform once the ui that the user has
            // changed by setting profile attribute in appModel
            userService.createAndSetProfile(mUserInfo);

            fileService.createAccountFolder(mUserInfo.getLogin());
            userService.saveProfileFiles();
            musicService.saveUserMusicFile();
            musicService.saveUserRightsListFile();
            log.info("AccountCreationCommand DONE");
            }
        else{
            log.warn("Can't create the profile. A profile with the same login already exists.");
            ErrorMessage nErrorMessage = new ErrorMessage("Can't create the profile. A profile with the same login already exists.");
            appModel.getErrorBus().pushErrorMessage(nErrorMessage);
        }

    }
}

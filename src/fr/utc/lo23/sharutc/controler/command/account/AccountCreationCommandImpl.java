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
 * Implementation of AccountCreationCommand
 */
public class AccountCreationCommandImpl implements AccountCreationCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AccountCreationCommandImpl.class);
    private AppModel appModel;
    private UserInfo mUserInfo;
    private final UserService userService;
    private final MusicService musicService;
    private final FileService fileService;
    
     /**
     * Constructor
     * 
     * @param appModel
     * @param userService
     * @param fileService
     * @param musicService
     */
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
        mUserInfo.setPassword(UserInfo.sha1(mUserInfo.getPassword()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("AccountCreationCommand ...");
        //Build the path name with the login of the new user in order to check if there is already a directory with the same name  
        StringBuilder builder = new StringBuilder(fileService.getAppFolder()).append(FileService.ROOT_FOLDER_USERS).append(File.separator).append(mUserInfo.getLogin());
        File file = new File(builder.toString());
        
        //Check if there is already a directory with the same name as the new user login.
        if (file.exists() == false) {
            // FIXME userProfile validation ??
            // it should be performed by the command or a service,
            // not in ui, use ErrorMessage and ErrorBus
            mUserInfo.setPeerId(System.currentTimeMillis());
            musicService.createAndSetCatalog();
            musicService.createAndSetRightsList();
            // let userService.createAndSetProfile(mUserInfo) call AFTER
            // musicService calls, we only inform once the ui that the user has
            // changed by setting profile attribute in appModel
            //if There is not, the profile will be created and a directory will be create on the computer
            userService.createAndSetProfile(mUserInfo);
            fileService.createAccountFolder(mUserInfo.getLogin());
            //Then, the profile is saved on a JSON file
            userService.saveProfileFiles();
            //All the musics of the profile are also saved
            musicService.saveUserMusicFile();
            musicService.saveUserRightsListFile();
            log.info("AccountCreationCommand DONE");
            
          // If there is already a directory with the same name as the new user, the new profile can't be create, then an error is sent.  
        } else {
            log.warn("Can't create the profile. A profile with the same login already exists.");
            ErrorMessage nErrorMessage = new ErrorMessage("Can't create the profile. A profile with the same login already exists.");
            appModel.getErrorBus().pushErrorMessage(nErrorMessage);
        }

    }
}

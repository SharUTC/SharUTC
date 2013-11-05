package fr.utc.lo23.sharutc.controler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.Profile;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
@Singleton
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory
            .getLogger(UserServiceImpl.class);
    private final AppModel appModel;
    private final Profile profile;
    private static final String dataPath = "";

    @Inject
    public UserServiceImpl(AppModel appModel) {
        this.appModel = appModel;
        this.profile = appModel.getProfile();
    }

    /**
     * {@inheritDoc}
     */
    //TODO : factorize code with any file by using/creating FileService.save(String filename, Object object);
    //FIXME : ObjectMapper should be a static instance (singleton is we refer to doc), actually another instance is required in MessageParser, the solution could be to create a MapperService that only owns this instance of ObjectMapper
    @Override
    public void saveProfileFiles() {
        Profile profile = appModel.getProfile();
        if (profile != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.writeValue(new File(dataPath + "\\profile.json"), profile);
            } catch (IOException ex) {
                log.error(ex.toString());
            }
        } else {
            log.warn("Can't save current profile(null)");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addContact(Peer peer) {
        profile.getCategories().findCategoryByName("default").addContactId(peer.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteContact(Long contactId) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createCategory(String categoryName) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCategory(Category category) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContactToCategory(Peer peer, Category category) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeContactFromCategory(Peer peer, Category category) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAndSetProfile(UserInfo userInfo) {
        Profile profile = new Profile(userInfo);
        appModel.setProfile(profile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadUserProfileFiles(String path) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectionRequest(String login, String password) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateConnectedPeers(UserInfo userInfo) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromConnectedPeers(long peerId) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void integrateConnection(UserInfo userinfo) {
        log.warn("Not supported yet.");
    }

    @Override
    public Long findContactByPeerId(Long peerId) {
        // 2 modes : when peer is a contact and when peer isn't a contact
        Long contact = appModel.getProfile().getCategories().
                findCategoryByName("default").getContacts().findById(peerId);
        return contact;
    }
}

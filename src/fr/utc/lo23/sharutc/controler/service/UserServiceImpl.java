package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.ErrorMessage;
import fr.utc.lo23.sharutc.model.userdata.ActivePeerList;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.Profile;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.util.Set;
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
    private final FileService fileService;
    //FIXME: un service n'est pas un conteneur d'objet, l'instance de profile
    // est à supprimer, j'imagine qu'elle devrait se trouver dans une commande
    private Profile profile;

    @Inject
    public UserServiceImpl(AppModel appModel, FileService fileService) {
        this.appModel = appModel;
        this.profile = appModel.getProfile();
        this.fileService = fileService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProfileFiles() {
        if (profile != null) {
            fileService.saveToFile(SharUTCFile.PROFILE, profile);
        } else {
            log.warn("Can't save current profile(null)");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContact(Peer peer) {
        //Check that the contact does not exist in the category Public
        if (!profile.getCategories().findCategoryById(Category.PUBLIC_CATEGORY_ID).getContacts().contains(peer)) {
            profile.getCategories().findCategoryById(Category.PUBLIC_CATEGORY_ID).addContact(peer);
        } else {
            log.warn("This contact already exists");
            ErrorMessage nErrorMessage = new ErrorMessage("This contact already exists");
            appModel.getErrorBus().pushErrorMessage(nErrorMessage);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteContact(Peer peer) {
        for (Category cat : profile.getCategories().getCategories()) {
            cat.getContacts().remove(peer);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createCategory(String categoryName) {
        Category c = new Category(categoryName);
        profile.getCategories().getCategories().add(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCategory(Category category) {
        if (category.getId() != 0) {
            profile.getCategories().remove(category);
        } else {
            log.warn("Can't delete Category Public");
            ErrorMessage nErrorMessage = new ErrorMessage("Can't delete Category Public");
            appModel.getErrorBus().pushErrorMessage(nErrorMessage);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContactToCategory(Peer peer, Category category) {
        Long peerId = peer.getId();
        Set<Integer> CategoriesIdsList = profile.getCategories().getCategoriesIdsByContactId(peerId);
        /*
         * Check that the contact does not exist in the category Public.
         * If it does not exist, the contact is added to the category Public.
         */
        if (!CategoriesIdsList.contains(Category.PUBLIC_CATEGORY_ID)) {
            profile.getCategories().findCategoryById(Category.PUBLIC_CATEGORY_ID).addContact(peer);
        }
        //Check that the contact does not exist in this category
        if (!CategoriesIdsList.contains(category.getId())) {
            profile.getCategories().findCategoryById(category.getId()).addContact(peer);
        } else {
            log.warn("This contact already exists in this category");
            ErrorMessage nErrorMessage = new ErrorMessage("This contact already exists in this category");
            appModel.getErrorBus().pushErrorMessage(nErrorMessage);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeContactFromCategory(Peer peer, Category category) {
        profile.getCategories().findCategoryById(category.getId()).removeContact(peer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAndSetProfile(UserInfo userInfo) {
        Profile nProfile = new Profile(userInfo);
        appModel.setProfile(nProfile);
        this.profile = nProfile;
        this.saveProfileFiles();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectionRequest(String login, String password) {
        Profile profileToConnect = fileService.readFile(SharUTCFile.PROFILE, Profile.class);
        if (profileToConnect != null && profileToConnect.getUserInfo() != null) {
            UserInfo userInfo = profileToConnect.getUserInfo();
            boolean success = userInfo.getLogin().equals(login)
                    && userInfo.getPassword().equals(password);
            if (success) {
                appModel.setProfile(profileToConnect);
            } else {
                // TODO: add a new error message instead of null
                appModel.getErrorBus().pushErrorMessage(null);
            }
        } else {
            // TODO: add a new error message instead of null
            appModel.getErrorBus().pushErrorMessage(null);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateConnectedPeers(UserInfo userInfo) {
        ActivePeerList activePeerList = appModel.getActivePeerList();
        Peer newPeer = new Peer(userInfo.getPeerId(), userInfo.getLogin());
        activePeerList.update(newPeer);
        //TODO: also update known peer list
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromConnectedPeers(long peerId) {
        ActivePeerList activePeerList = appModel.getActivePeerList();
        Peer removePeer = activePeerList.getByPeerId(peerId);
        activePeerList.remove(removePeer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void integrateConnection(UserInfo userinfo) {
        log.warn("Not supported yet.");
    }

    @Override
    public Long findContactIdByPeerId(Long peerId) {
        // 2 modes : when peer is a contact and when peer isn't a contact
        Peer contact = appModel.getProfile().getCategories().
                findCategoryById(Category.PUBLIC_CATEGORY_ID).getContacts().findById(peerId);
        return contact.getId();
    }

    @Override
    public void disconnectionRequest() {
        this.saveProfileFiles();
    }
}

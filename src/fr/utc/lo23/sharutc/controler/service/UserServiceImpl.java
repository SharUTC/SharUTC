package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.ErrorMessage;
import fr.utc.lo23.sharutc.model.userdata.ActivePeerList;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.Profile;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateConnectionCommandImpl;
import fr.utc.lo23.sharutc.model.userdata.KnownPeerList;

/**
 * {@inheritDoc}
 */
@Singleton
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory
            .getLogger(UserServiceImpl.class);
    private final AppModel appModel;
    private final FileService fileService;

    @Inject
    public UserServiceImpl(AppModel appModel, FileService fileService) {
        this.appModel = appModel;
        this.fileService = fileService;
    }

    private Profile getProfile() {
        return appModel.getProfile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProfileFiles() {
        if (getProfile() != null) {
            fileService.saveToFile(SharUTCFile.PROFILE, getProfile());
        } else {
            log.warn("Can't save current profile(null)");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContact(Contact contact) {
        Long contactId = contact.getUserInfo().getPeerId();
        //check that the contact is not in the Public category
        if (!contact.isInPublic()) {
            getProfile().getContacts().findById(contactId).addCategoryId(Category.PUBLIC_CATEGORY_ID);
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
    public void deleteContact(Contact contact) {
        getProfile().getContacts().remove(contact);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createCategory(String categoryName) {
        Category c = new Category(categoryName);
        getProfile().getCategories().add(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCategory(Category category) {
        //Check that the category is not the public one
        if (category.getId() != Category.PUBLIC_CATEGORY_ID) {
            getProfile().getCategories().remove(category);
            for (Contact c : getProfile().getContacts().getContacts()) {
                /*
                 * for each contact try to delete the categoryId
                 * and if the contact was only in this category, we put the contact in the public one
                 */
                this.removeContactFromCategory(c, category);             
            }
        } 
        else {
            log.warn("Can't delete Category Public");
            ErrorMessage nErrorMessage = new ErrorMessage("Can't delete Category Public");
            appModel.getErrorBus().pushErrorMessage(nErrorMessage);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContactToCategory(Contact contact, Category category) {
        Long contactId = contact.getUserInfo().getPeerId();
        Set<Integer> categoriesIdsList = contact.getCategoryIds();
        
        //Check that the contact does not exist in this category
        if (!categoriesIdsList.contains(category.getId())) {
            /* Check if the contact is in the public category
             * If it is the case, we remove the PUBLIC_CATEGORY_ID
             */
            if (categoriesIdsList.contains(Category.PUBLIC_CATEGORY_ID)) {
                getProfile().getContacts().findById(contactId).removeCategoryId(Category.PUBLIC_CATEGORY_ID);
            }
            getProfile().getContacts().findById(contactId).addCategoryId(category.getId());
        } 
        else {
            log.warn("This contact already exists in this category");
            ErrorMessage nErrorMessage = new ErrorMessage("This contact already exists in this category");
            appModel.getErrorBus().pushErrorMessage(nErrorMessage);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeContactFromCategory(Contact contact, Category category) {
        //Check that the category is not the public one because it is the default category
        if (category.getId() != Category.PUBLIC_CATEGORY_ID) {
            Contact c = getProfile().getContacts().findById(contact.getUserInfo().getPeerId());
            // Delete the categoryId in the contact
            c.removeCategoryId(category.getId());
            // if this category was the only one, we put the contact in the public one
            if (c.getCategoryIds().isEmpty())
                c.addCategoryId(Category.PUBLIC_CATEGORY_ID);
        } 
        else {
            log.warn("Can't remove contact from Public category");
            ErrorMessage nErrorMessage = new ErrorMessage("Can't remove contact from Public category");
            appModel.getErrorBus().pushErrorMessage(nErrorMessage);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAndSetProfile(UserInfo userInfo) {
        Profile nProfile = new Profile(userInfo);
        appModel.setProfile(nProfile);
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
                this.integrateConnection(appModel.getProfile().getUserInfo());
            } else {
                String message = "Incorrect login or password";
                log.warn(message);
                appModel.getErrorBus().pushErrorMessage(new ErrorMessage(message));
            }
        } else {
            String message = "Unknown user";
            log.warn(message);
            appModel.getErrorBus().pushErrorMessage(new ErrorMessage(message));
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateConnectedPeers(UserInfo userInfo) {
        ActivePeerList activePeerList = appModel.getActivePeerList();
        KnownPeerList knownPeerList = appModel.getProfile().getKnownPeerList();
        Peer newPeer = new Peer(userInfo.getPeerId(), userInfo.getLogin());
        activePeerList.update(newPeer);
        knownPeerList.update(newPeer);
        //TODO: also update contact.userInfo if peer is a contact for offline access

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
        // FIXME : pas de commande dans ce service, encore moins l'utilisation de xxxImpl à la main...
        //IntegrateConnectionCommandImpl command = new IntegrateConnectionCommandImpl(appModel, this);
        //command.setUserInfo(userinfo);
        //command.execute();
    }

    @Override
    public Contact findContactByPeerId(Long peerId) {
        return getProfile().getContacts().findById(peerId);
    }

    @Override
    public void disconnectionRequest() {
        this.saveProfileFiles();
    }
}

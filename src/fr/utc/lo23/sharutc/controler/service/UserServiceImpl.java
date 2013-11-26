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
import fr.utc.lo23.sharutc.model.userdata.KnownPeerList;

/**
 * Implementation of UserService
 */
@Singleton
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory
            .getLogger(UserServiceImpl.class);
    private final AppModel appModel;
    private final FileService fileService;

     /**
     * Constructor
     * 
     * @param appModel
     * @param fileService
     */
    @Inject
    public UserServiceImpl(AppModel appModel, FileService fileService) {
        this.appModel = appModel;
        this.fileService = fileService;
    }

     /**
     * Return user's profile
     * 
     * @return user's profile
     */
    private Profile getProfile() {
        return appModel.getProfile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProfileFiles() {
        log.debug("saveProfileFiles ...");
        if (getProfile() != null) {
            fileService.saveToFile(SharUTCFile.PROFILE, getProfile());
        } else {
            log.warn("Can't save current profile(null)");
        }
        log.debug("saveProfileFiles DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContact(Contact contact) {
        log.debug("addContact ...");
        //check that the contact does not exist
        boolean present = false;
        for (Contact c : getProfile().getContacts().getContacts()) {
            if (c.getUserInfo().getPeerId().equals(contact.getUserInfo().getPeerId())) {
                present = true;
                break;
            }
        }
        if (!present) {
            contact.addCategoryId(Category.PUBLIC_CATEGORY_ID);
            getProfile().getContacts().add(contact);
        } else {
            log.warn("This contact already exists");
            ErrorMessage nErrorMessage = new ErrorMessage("This contact already exists");
            appModel.getErrorBus().pushErrorMessage(nErrorMessage);
        }
        log.debug("addContact DONE");
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
        /*
         * check that the name of the category does not exist
         * Indeed, even if it will be possible to have two categories with the same name
         * (since there are IDs), we consider that a user can't create two categories
         *  with the same name.
         */
        boolean present = false;
        for (Category c : getProfile().getCategories().getCategories()) {
            if (c.getName().equals(categoryName)) {
                present = true;
                break;
            }
        }
        if (!present) {
            Category c = new Category(getProfile().getNewCategoryId(), categoryName);
            getProfile().getCategories().add(c);
        } else {
            log.warn("This category name already exists");
            ErrorMessage nErrorMessage = new ErrorMessage("This category name already exists");
            appModel.getErrorBus().pushErrorMessage(nErrorMessage);
        }
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
        } else {
            log.warn("Can't delete Public category ");
            ErrorMessage nErrorMessage = new ErrorMessage("Can't delete Public category");
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
            } else {
                // if the contact does not already exists, we add the contact
                boolean present = false;
                for (Contact c : getProfile().getContacts().getContacts()) {
                    if (c.getUserInfo().getPeerId().equals(contact.getUserInfo().getPeerId())) {
                        present = true;
                        break;
                    }
                }
                if (!present) {
                    getProfile().getContacts().add(contact);
                }
            }
            getProfile().getContacts().findById(contactId).addCategoryId(category.getId());
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
    public void removeContactFromCategory(Contact contact, Category category) {
        //Check that the category is not the public one because it is the default category
        if (!category.getId().equals(Category.PUBLIC_CATEGORY_ID)) {
            Contact c = getProfile().getContacts().findById(contact.getUserInfo().getPeerId());
            // Delete the categoryId in the contact
            c.removeCategoryId(category.getId());
            // if this category was the only one, we put the contact in the public one
            if (c.getCategoryIds().isEmpty()) {
                c.addCategoryId(Category.PUBLIC_CATEGORY_ID);
            }
        } else {
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
        log.debug("createAndSetProfile ...");
        //Create the new profile with the userInfo entered by a user
        Profile nProfile = new Profile(userInfo);
        //The new profile is automatically online
        appModel.setProfile(nProfile);
        log.debug("createAndSetProfile DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectionRequest(String login, String password) {
        Profile profileToConnect = fileService.readProfileFile(login);
        if (profileToConnect != null && profileToConnect.getUserInfo() != null) {
            UserInfo userInfo = profileToConnect.getUserInfo();
            boolean success = userInfo.getLogin().equals(login)
                    && userInfo.getPassword().equals(password);
            if (success) {
                appModel.setProfile(profileToConnect);
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
        Peer newPeer = userInfo.toPeer();
        activePeerList.update(userInfo);
        //peer is in knownPeerList only if he is a contact, commented or scored a music, otherwise we don't need to save it's name
        if (knownPeerList.contains(newPeer)) {
            knownPeerList.update(newPeer);
        }

        Contact contact = appModel.getProfile().getContacts().findById(userInfo.getPeerId());
        if (contact != null) {
            contact.setUserInfo(userInfo);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromConnectedPeers(long peerId) {
        ActivePeerList activePeerList = appModel.getActivePeerList();
        UserInfo removeUserInfo = activePeerList.getUserInfoByPeerId(peerId);
        activePeerList.remove(removeUserInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact findContactByPeerId(Long peerId) {
        return getProfile().getContacts().findById(peerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanProfile() {
        appModel.setProfile(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnectionRequest() {
        this.saveProfileFiles();
        cleanProfile();
    }
}

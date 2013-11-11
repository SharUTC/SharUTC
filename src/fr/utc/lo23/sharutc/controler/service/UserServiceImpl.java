package fr.utc.lo23.sharutc.controler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.ErrorMessage;
import fr.utc.lo23.sharutc.model.userdata.ActivePeerList;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.Profile;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.io.File;
import java.io.IOException;
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
    //FIXME: un service n'est pas un conteneur d'objet, l'instance de profile
    // est à supprimer, j'imagine qu'elle devrait se trouver dans une commande
    private Profile profile;
    //TODO: externalize datapath, see or use FileService constants
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
        //Check that the contact does not exist in the category Public
        if (!profile.getCategories().findCategoryById(Category.PUBLIC_CATEGORY_ID).getContacts().contains(peer)) {
               profile.getCategories().findCategoryById(Category.PUBLIC_CATEGORY_ID).addContact(peer);
        }
        else {
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
        for(Category cat : profile.getCategories().getCategories()) {
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
         //Check that the category is not the Public one
        if (category.getId() != Category.PUBLIC_CATEGORY_ID) {
            profile.getCategories().remove(category);
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
    public void removeContactFromCategory(Peer peer, Category category) {
        //Check that the category is not the Public one
        if (category.getId() != Category.PUBLIC_CATEGORY_ID) {
           profile.getCategories().findCategoryById(category.getId()).removeContact(peer);
        }
        else {
            log.warn("Can't remove the contact from category Public");
            ErrorMessage nErrorMessage = new ErrorMessage("Can't remove the contact from category Public");
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
        this.profile = nProfile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectionRequest(String login, String password) {
        Profile profile;
        ObjectMapper mapper = new ObjectMapper();
        try {
            //FIXME: use fileService constantes instead of local folder definitions
            profile = mapper.readValue(new File(dataPath + login + "\\profile.json"), Profile.class);
            boolean success = profile.getUserInfo().getLogin().equals(login)
                    && profile.getUserInfo().getPassword().equals(password);
            if (success) {
                appModel.setProfile(profile);
            } else {
                // TODO: add a new error message instead of null
                appModel.getErrorBus().pushErrorMessage(null);
            }
            profile = null;
        } catch (IOException ex) {
            log.warn("Exception raised during user login");
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
    public void disconnectionRequest(){
        this.saveProfileFiles();
    }
}

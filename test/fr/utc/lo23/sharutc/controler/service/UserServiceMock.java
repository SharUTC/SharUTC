package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Singleton
public class UserServiceMock extends UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory
            .getLogger(UserServiceMock.class);

    @Inject
    public UserServiceMock(AppModel appModel) {
        super(appModel);
    }

    @Override
    public void addContact(Peer peer) {
        super.addContact(peer);
    }

    @Override
    public void deleteContact(Contact contact) {
        super.deleteContact(contact);
    }

    @Override
    public void createCategory(String categoryName) {
        super.createCategory(categoryName);
    }

    @Override
    public void deleteCategory(Category category) {
        super.deleteCategory(category);
    }

    @Override
    public void addContactToCategory(Peer peer, Category category) {
        super.addContactToCategory(peer, category);
    }

    @Override
    public void removeContactFromCategory(Peer peer, Category category) {
        super.removeContactFromCategory(peer, category);
    }

    @Override
    public void createProfile(UserInfo userInfo) {
        super.createProfile(userInfo);
    }

    @Override
    public void loadUserProfileFiles(String path) {
        super.loadUserProfileFiles(path);
    }

    @Override
    public void connectionRequest(String login, String password) {
        super.connectionRequest(login, password);
    }

    @Override
    public void updateConnectedPeers(UserInfo userInfo) {
        super.updateConnectedPeers(userInfo);
    }

    @Override
    public void removeFromConnectedPeers(long peerId) {
        super.removeFromConnectedPeers(peerId);
    }

    @Override
    public void saveUserProfileFiles() {
        super.saveUserProfileFiles();
    }

    @Override
    public void integrateConnection(UserInfo userinfo) {
        super.integrateConnection(userinfo);
    }
}
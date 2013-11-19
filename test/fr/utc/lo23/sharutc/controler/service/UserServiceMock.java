package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;
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
    public UserServiceMock(AppModel appModel, FileService fileService) {
        super(appModel, fileService);
    }

    @Override
    public void addContact(Contact peer) {
        super.addContact(peer);
    }

    @Override
    public void deleteContact(Contact peer) {
        super.deleteContact(peer);
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
    public void addContactToCategory(Contact contact, Category category) {
        super.addContactToCategory(contact, category);
    }

    @Override
    public void removeContactFromCategory(Contact contact, Category category) {
        super.removeContactFromCategory(contact, category);
    }

    @Override
    public void createAndSetProfile(UserInfo userInfo) {
        super.createAndSetProfile(userInfo);
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
    public void saveProfileFiles() {
        super.saveProfileFiles();
    }

}
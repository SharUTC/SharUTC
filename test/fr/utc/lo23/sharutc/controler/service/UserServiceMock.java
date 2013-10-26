package fr.utc.lo23.sharutc.controler.service;

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
public class UserServiceMock implements UserService {

    private static final Logger log = LoggerFactory
            .getLogger(UserServiceMock.class);

    @Override
    public void addContact(Peer peer) {
        log.warn("Not supported yet.");
    }

    @Override
    public void deleteContact(Contact contact) {
        log.warn("Not supported yet.");
    }

    @Override
    public void createCategory(String categoryName) {
        log.warn("Not supported yet.");
    }

    @Override
    public void deleteCategory(Category category) {
        log.warn("Not supported yet.");
    }

    @Override
    public void addContactToCategory(Peer peer, Category category) {
        log.warn("Not supported yet.");
    }

    @Override
    public void removeContactFromCategory(Peer peer, Category category) {
        log.warn("Not supported yet.");
    }

    @Override
    public void manageGroupRights(Category category, Music music, boolean readInfo, boolean listen, boolean noteAndComment) {
        log.warn("Not supported yet.");
    }

    @Override
    public void createProfile(UserInfo userInfo) {
        log.warn("Not supported yet.");
    }

    @Override
    public void loadUserProfileFiles(String path) {
        log.warn("Not supported yet.");
    }

    @Override
    public void connectionRequest(String login, String password) {
        log.warn("Not supported yet.");
    }

    @Override
    public void updateConnectedPeers(UserInfo userInfo) {
        log.warn("Not supported yet.");
    }

    @Override
    public void removeFromConnectedPeers(long peerId) {
        log.warn("Not supported yet.");
    }

    @Override
    public void saveUserProfileFiles() {
        log.warn("Not supported yet.");
    }

    @Override
    public void integrateHeartbeat(UserInfo userinfo) {
        log.warn("Not supported yet.");
    }

}

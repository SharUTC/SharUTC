package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;

/**
 *
 */
public interface UserService {

    /**
     * Save the currently connected user profile, by writing the java data into
     * a JSON file.
     */
    public void saveProfileFiles();

    /**
     *
     * @param peer
     */
    public void addContact(Peer peer);

    /**
     *
     * @param contact
     */
    public void deleteContact(Contact contact);

    /**
     *
     * @param categoryName
     */
    public void createCategory(String categoryName);

    /**
     *
     * @param category
     */
    public void deleteCategory(Category category);

    /**
     *
     * @param peer
     * @param category
     */
    public void addContactToCategory(Peer peer, Category category);

    /**
     *
     * @param peer
     * @param category
     */
    public void removeContactFromCategory(Peer peer, Category category);

    /**
     *
     * @param userInfo
     */
    public void createAndSetProfile(UserInfo userInfo);

    /**
     *
     * @param path
     */
    public void loadUserProfileFiles(String path);

    /**
     *
     * @param login
     * @param password
     */
    public void connectionRequest(String login, String password);

    /**
     *
     * @param userInfo
     */
    public void updateConnectedPeers(UserInfo userInfo);

    /**
     *
     * @param peerId
     */
    public void removeFromConnectedPeers(long peerId);

    /**
     *
     * @param userinfo
     */
    public void integrateConnection(UserInfo userinfo);

    /**
     *
     * @param peerId
     * @return
     */
    public Contact findContactByPeerId(Long peerId);
}

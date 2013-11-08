package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.userdata.Category;
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
     * Add a new contact represented by one's id in the category
     * @param peer
     */
    public void addContact(Peer peer);

    /**
     * Remove the contact represented by one's id from every category
     * @param contactId
     */
    public void deleteContact(Long contactId);
    
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
    public Long findContactIdByPeerId(Long peerId);
}

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
     * Remove the contact represented by one's peer from every category
     * @param peer
     */
    public void deleteContact(Peer peer);
    
    /**
     * Create the category categoryName
     * @param categoryName
     */
    public void createCategory(String categoryName);

    /**
     * Delete the category category
     * @param category
     */
    public void deleteCategory(Category category);

    /**
     * Add a contact represented by one's peer to a specified category
     * @param peer
     * @param category
     */
    public void addContactToCategory(Peer peer, Category category);

    /**
     * Remove a contact represented by one's peer from a specified category
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
    
    /**
     * Send a disconnection request to the network and save profile 
     * 
     */
    public void disconnectionRequest();
}

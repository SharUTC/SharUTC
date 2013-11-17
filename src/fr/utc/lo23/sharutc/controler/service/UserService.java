package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;
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
     * Add a new contact in the category
     *
     * @param contact
     */
    public void addContact(Contact contact);

    /**
     * Remove a contact from every category
     *
     * @param contact
     */
    public void deleteContact(Contact contact);

    /**
     * Create the category categoryName
     *
     * @param categoryName
     */
    public void createCategory(String categoryName);

    /**
     * Delete the category category
     *
     * @param category
     */
    public void deleteCategory(Category category);

    /**
     * Add a contact to a specified category
     *
     * @param contact
     * @param category
     */
    public void addContactToCategory(Contact contact, Category category);

    /**
     * Remove a contact from a specified category
     *
     * @param contact
     * @param category
     */
    public void removeContactFromCategory(Contact contact, Category category);

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
     * Update the connected peers list with userInfo.
     * If the peerId didn't exist in the list, it is added.
     * Else, the peer info is updated
     * @param userInfo
     */
    public void updateConnectedPeers(UserInfo userInfo);

    /**
     * Remove a peer from the connected peers list with id.
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

    /**
     * Send a disconnection request to the network and save profile
     *
     */
    public void disconnectionRequest();
    
}

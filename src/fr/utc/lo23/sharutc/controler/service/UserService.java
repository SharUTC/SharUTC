package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;

/**
 * Service that provides all functions for user management
 */
public interface UserService {

    /**
     * Save the currently connected user profile, by writing the java data into
     * a JSON file.
     */
    public void saveProfileFiles();

    /**
     * Add a new contact (to the category Public)
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
     * Delete the category category (we can't delete the category Public)
     * It removes the contacts from this category
     * and manages the changes of categories. 
     * For instance, if a contact was only in this category, we add it in the category Public.
     * 
     * @param category
     */
    public void deleteCategory(Category category);
    
    /**
     * It allows users to set a category name
     * 
     * @param categoryId
     * @param newCategoryName
     */
    public void setCategoryName(Integer categoryId, String newCategoryName);

    /**
     * Add a contact to a specified category
     * It manages the changes of categories for a contact, if it is needed
     * (particularly with the category Public : if the contact was previously
     * present in the category Public, we remove the contact from it)
     *
     * @param contact
     * @param category
     */
    public void addContactToCategory(Contact contact, Category category);

    /**
     * Remove a contact from a specified category (we can't remove a contact from the category Public)
     * It manages the changes of categories. 
     * For instance, if the contact was only in this category, we add it in the category Public
     *
     * @param contact
     * @param category
     */
    public void removeContactFromCategory(Contact contact, Category category);

    /**
     * Create and set the profile
     * 
     * @param userInfo
     */
    public void createAndSetProfile(UserInfo userInfo);

    /**
     * loginRequest
     * 
     * @param login
     * @param password
     */
    public void connectionRequest(String login, String password);

    /**
     * Update the connected peers list with userInfo.
     * If the peerId didn't exist in the list, it is added.
     * Else, the peer info is updated
     * 
     * @param userInfo
     */
    public void updateConnectedPeers(UserInfo userInfo);

    /**
     * Remove a peer from the connected peers list with id.
     * @param peerId
     */
    public void removeFromConnectedPeers(long peerId);

    /**
     * Find a contact thanks to its peerId
     * 
     * @param peerId
     * @return the contact
     */
    public Contact findContactByPeerId(Long peerId);
    
    /**
     * Clean the profile object by putting a null value in the object.
     *
     */
    public void cleanProfile();

    /**
     * Save profile in JSON then clean the profile object.
     *
     */
    public void disconnectionRequest();
    
}

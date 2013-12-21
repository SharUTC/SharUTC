package fr.utc.lo23.sharutc.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * Represents a user profile
 */
public class Profile implements Serializable {

    private static final long serialVersionUID = 4883249865688142273L;
    private KnownPeerList mKnownPeerList;
    private Categories mCategories;
    private Contacts mContacts;
    private UserInfo mUserInfo;
    private Long musicCounter;
    private Integer categoryIdSequence;

    /**
     * Constructor keep no args constructor for parsing purpose
     */
    public Profile() {
        this.musicCounter = 0L;
        this.categoryIdSequence = 0;
    }

    /**
     * Constructor For ProfileCreationCommand at least
     *
     * @param userInfo
     */
    public Profile(UserInfo userInfo) {
        this.musicCounter = 0L;
        this.categoryIdSequence = 0;
        this.mKnownPeerList = new KnownPeerList();
        this.mCategories = new Categories();
        mCategories.add(new Category(Category.PUBLIC_CATEGORY_ID, Category.PUBLIC_CATEGORY_NAME));
        this.mContacts = new Contacts();
        this.mUserInfo = userInfo;
    }

    /**
     * Return a new unique music id and increment the music id sequence
     *
     * @return a new unique music id
     */
    @JsonIgnore
    public Long getNewMusicId() {
        return ++musicCounter;
    }

    /**
     * Decrement the music id sequence
     *
     */
    public void decrementMusicId() {
        musicCounter--;
    }

    /**
     * Return a new unique category id and increment the category id sequence
     *
     * @return a new unique category id
     */
    @JsonIgnore
    public Integer getNewCategoryId() {
        return ++categoryIdSequence;
    }

    /**
     * Return the object containing the list of known peers
     *
     * @return the object containing the list of known peers
     */
    public KnownPeerList getKnownPeerList() {
        return mKnownPeerList;
    }

    /**
     * Set the object containing the list of known peers
     *
     * @param knownPeerList the object containing the list of known peers
     */
    public void setKnownPeerList(KnownPeerList knownPeerList) {
        this.mKnownPeerList = knownPeerList;
    }

    /**
     * Return the category list
     *
     * @return the category list
     */
    public Categories getCategories() {
        return mCategories;
    }

    /**
     * Set the category list
     *
     * @param categories
     */
    public void setCategories(Categories categories) {
        this.mCategories = categories;
    }

    /**
     * Return the contact list
     *
     * @return the contact list
     */
    public Contacts getContacts() {
        return mContacts;
    }

    /**
     * Set the contact list
     *
     * @param mContacts
     */
    public void setContacts(Contacts mContacts) {
        this.mContacts = mContacts;
    }

    /**
     * Return user's information
     *
     * @return user's information
     */
    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    /**
     * Set user's information
     *
     * @param userInfo
     */
    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    /**
     * Return the number of musics
     *
     * @return the number of musics
     */
    public Long getMusicCounter() {
        return musicCounter;
    }

    /**
     * Set the number of musics
     *
     * @param musicCounter
     */
    public void setMusicCounter(Long musicCounter) {
        this.musicCounter = musicCounter;
    }

    /**
     * Return the number of categories
     *
     * @return the number of categories
     */
    public Integer getCategoryIdSequence() {
        return categoryIdSequence;
    }

    /**
     * Set the number of categories
     *
     * @param categoryIdSequence
     */
    public void setCategoryIdSequence(Integer categoryIdSequence) {
        this.categoryIdSequence = categoryIdSequence;
    }

    /**
     * give the number of contact in <i>category</i>
     *
     * @param category
     * @return number of contacts
     */
    public int getNumberOfContact(Category category) {
        Integer id = category.getId();
        int count = 0;
        ArrayList<Contact> contacts = mContacts.getContacts();
        for (Contact contact : contacts) {
            Set<Integer> categoriesIds = contact.getCategoryIds();
            for (Integer categoryId : categoriesIds) {
                if (id == categoryId) {
                    ++count;
                    break;
                }
            }
        }
        return count;
    }
}

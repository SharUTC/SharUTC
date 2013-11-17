package fr.utc.lo23.sharutc.model.userdata;

import java.io.Serializable;

/**
 *
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
     * keep no args constructor for parsing purpose
     */
    public Profile() {
        this.musicCounter = 0L;
        this.categoryIdSequence = 0;
    }

    /**
     * For ProfileCreationCommand at least
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
     *
     * @return
     */
    public Long getNewMusicId() {
        return ++musicCounter;
    }

    public void decrementMusicId() {
        musicCounter--;
    }
    
     /**
     *
     * @return
     */
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
     *
     * @return
     */
    public Categories getCategories() {
        return mCategories;
    }

    /**
     *
     * @param categories
     */
    public void setCategories(Categories categories) {
        this.mCategories = categories;
    }

    /**
     *
     * @return
     */    
    public Contacts getContacts() {
        return mContacts;
    }

    /**
     *
     * @param mContacts
     */    
    public void setContacts(Contacts mContacts) {
        this.mContacts = mContacts;
    }

    /**
     *
     * @return
     */
    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    /**
     *
     * @param userInfo
     */
    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }
}

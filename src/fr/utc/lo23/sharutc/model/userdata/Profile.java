package fr.utc.lo23.sharutc.model.userdata;

import java.io.Serializable;

/**
 *
 */
public class Profile implements Serializable {

    private static final long sSerialVersionUID = 4883249865688142273L;
    private Long mMtimeStampProfile;
    private KnownPeerList mKnownPeerList;
    private Contacts mContacts;
    private Categories mCategories;
    private UserInfo mUserInfo;

    /**
     *
     */
    public Profile() {
        counter = 0l;
    }

    /**
     *
     * @return
     */
    public Long getTimeStampProfile() {
        return mMtimeStampProfile;
    }

    /**
     *
     * @param timeStampProfile
     */
    public void setTimeStampProfile(Long timeStampProfile) {
        this.mMtimeStampProfile = timeStampProfile;
    }

    /**
     *
     * @return
     */
    public Long getNewMusicId() {
        return ++counter;
    }

    /**
     *
     * @return
     */
    public KnownPeerList getKnownPeerList() {
        return mKnownPeerList;
    }

    /**
     *
     * @param knownPeerList
     */
    public void setKnownPeerList(KnownPeerList knownPeerList) {
        this.mKnownPeerList = knownPeerList;
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
     * @param contacts
     */
    public void setContacts(Contacts contacts) {
        this.mContacts = contacts;
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

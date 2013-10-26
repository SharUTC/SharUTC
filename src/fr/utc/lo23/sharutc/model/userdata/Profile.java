package fr.utc.lo23.sharutc.model.userdata;

import java.io.Serializable;

/**
 *
 */
public class Profile implements Serializable {

    private static final long serialVersionUID = 4883249865688142273L;
    private Long counter;
    private KnownPeerList knownPeerList;
    private Contacts contacts;
    private Categories categories;
    private UserInfo userInfo;

    /**
     *
     */
    public Profile() {
    }

    /**
     *
     * @return
     */
    public Long getCounter() {
        return counter;
    }

    /**
     *
     * @param counter
     */
    public void setCounter(Long counter) {
        this.counter = counter;
    }

    /**
     *
     * @return
     */
    public KnownPeerList getKnownPeerList() {
        return knownPeerList;
    }

    /**
     *
     * @param knownPeerList
     */
    public void setKnownPeerList(KnownPeerList knownPeerList) {
        this.knownPeerList = knownPeerList;
    }

    /**
     *
     * @return
     */
    public Contacts getContacts() {
        return contacts;
    }

    /**
     *
     * @param contacts
     */
    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    /**
     *
     * @return
     */
    public Categories getCategories() {
        return categories;
    }

    /**
     *
     * @param categories
     */
    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    /**
     *
     * @return
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     *
     * @param userInfo
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}

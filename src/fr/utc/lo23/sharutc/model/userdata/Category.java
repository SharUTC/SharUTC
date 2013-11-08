package fr.utc.lo23.sharutc.model.userdata;

import java.io.Serializable;

/**
 *
 */
public class Category implements Serializable {

    private static final long serialVersionUID = 3790688676428360970L;
    /**
     * Undeletable category ID
     */
    public static final Integer PUBLIC_CATEGORY_ID = new Integer(0);
    public static final String PUBLIC_CATEGORY_NAME = "Public";
    private Integer mId;
    private String mName;
    private Contacts mContacts;

    //keep no args constructor for parsing purpose
    public Category() {
    }

    /**
     *
     * @param name
     */
    public Category(String name) {
        mName = name;
    }

    public Category(Integer id, String name) {
        this.mId = id;
        this.mName = name;
        this.mContacts = new Contacts();
    }

    /**
     *
     * @return
     */
    public Integer getId() {
        return mId;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        this.mId = id;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return mName;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.mName = name;
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
     * add an id to the contacts list
     *
     * @param id
     */
    public void addContactId(Long id) {
        mContacts.add(id);
    }

    /**
     *
     * @param id
     * @return
     */
    public Long findContactId(Long id) {
        return mContacts.findById(id);
    }
}

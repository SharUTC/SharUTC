package fr.utc.lo23.sharutc.model.userdata;

import java.io.Serializable;

/**
 * Represents a category
 */
public class Category implements Serializable {

    private static final long serialVersionUID = 3790688676428360970L;
    /**
     * Id of the category Public
     */
    public static final Integer PUBLIC_CATEGORY_ID = new Integer(0);
    /**
     * Name of the category Public
     */
    public static final String PUBLIC_CATEGORY_NAME = "My Contacts";
    private Integer mId;
    private String mName;

    /**
     * Default constructor
     */
    public Category() {
    }

    /**
     * Constructor
     *
     * @param mId
     * @param mName
     */
    public Category(Integer mId, String mName) {
        this.mId = mId;
        this.mName = mName;
    }

    /**
     * Return the category id
     *
     * @return the category id
     */
    public Integer getId() {
        return mId;
    }

    /**
     * Set the category id
     *
     * @param id - the category id
     */
    public void setId(Integer id) {
        this.mId = id;
    }

    /**
     * Return the name of the category
     *
     * @return the name of the category
     */
    public String getName() {
        return mName;
    }

    /**
     * Set the name of the category
     *
     * @param name
     */
    public void setName(String name) {
        this.mName = name;
    }

    /**
     * Override the method equals
     *
     * @param obj
     * @return a boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Category other = (Category) obj;
        if (this.mId != other.mId && (this.mId == null || !this.mId.equals(other.mId))) {
            return false;
        }
        if ((this.mName == null) ? (other.mName != null) : !this.mName.equals(other.mName)) {
            return false;
        }
        return true;
    }

    /**
     * Override the method hashCode
     *
     * @return the hash
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.mId != null ? this.mId.hashCode() : 0);
        hash = 59 * hash + (this.mName != null ? this.mName.hashCode() : 0);
        return hash;
    }

    /**
     * Override the method toString
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Category{" + "mId=" + mId + ", mName=" + mName + '}';
    }
}
package fr.utc.lo23.sharutc.model.userdata;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a contact
 */
public class Contact implements Serializable {

    private static final long serialVersionUID = 2934900084513371081L;
    private UserInfo mUserInfo;
    private Set<Integer> mCategoryIds;

    /**
     * Default constructor
     */
    public Contact() {
    }

    /**
     * Constructor
     *
     * @param userInfo
     */
    public Contact(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        this.mCategoryIds = new HashSet<Integer>();
    }

    /**
     * Return contact's UserInfo
     *
     * @return contact's UserInfo
     */
    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    /**
     * Set contact's UserInfo
     *
     * @param mUserInfo
     */
    public void setUserInfo(UserInfo mUserInfo) {
        this.mUserInfo = mUserInfo;
    }

    /**
     * Return the list of the ids of the categories in which the contact is
     *
     * @return the list of the ids of the categories in which the contact is
     */
    public Set<Integer> getCategoryIds() {
        return mCategoryIds;
    }

    /**
     * Set the list of the ids of the categories in which the contact is
     *
     * @param categoryIds - the list of the ids of the categories in which the
     * contact is
     */
    public void setCategoryId(Set<Integer> categoryIds) {
        this.mCategoryIds = categoryIds;
    }

    /**
     * Add a category id to the list of the ids of the categories in which the
     * contact is
     *
     * @param categoryId
     */
    public void addCategoryId(Integer categoryId) {
        mCategoryIds.add(categoryId);
    }

    /**
     * Remove a category id from the list of the ids of the categories in which
     * the contact is
     *
     * @param categoryId
     */
    public void removeCategoryId(Integer categoryId) {
        mCategoryIds.remove(categoryId);
    }

    /**
     * Check if a contact is in the Public category
     *
     * @return a boolean
     */
    public boolean isInPublic() {
        boolean isInPublic = false;
        for (Integer c : mCategoryIds) {
            if (c.equals(Category.PUBLIC_CATEGORY_ID)) {
                isInPublic = true;
                break;
            }
        }
        return isInPublic;
    }

    /**
     * Return the hashcode of this instance
     *
     * @return the hashcode of this instance
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.mUserInfo != null ? this.mUserInfo.hashCode() : 0);
        hash = 67 * hash + (this.mCategoryIds != null ? this.mCategoryIds.hashCode() : 0);
        return hash;
    }

    /**
     * Override the method equals
     *
     * @param obj the object test for equality
     * @return true if both objects equals
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Contact other = (Contact) obj;
        if (this.mUserInfo != other.mUserInfo && (this.mUserInfo == null || !this.mUserInfo.equals(other.mUserInfo))) {
            return false;
        }
        if (this.mCategoryIds != other.mCategoryIds && (this.mCategoryIds == null || !this.mCategoryIds.equals(other.mCategoryIds))) {
            return false;
        }
        return true;
    }

    /**
     * Override the method toString
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Contact{" + "mUserInfo=" + mUserInfo + ", mCategoryIds=" + mCategoryIds + '}';
    }
}
package fr.utc.lo23.sharutc.model.userdata;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Contact implements Serializable {

    private static final long serialVersionUID = 2934900084513371081L;
    private UserInfo mUserInfo;
    private Set<Integer> mCategoryIds;

    /**
     *
     */
    public Contact() {
    }

    /**
     *
     * @param userInfo
     */
    public Contact(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        this.mCategoryIds = new HashSet<Integer>();
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
     * @param mUserInfo
     */
    public void setUserInfo(UserInfo mUserInfo) {
        this.mUserInfo = mUserInfo;
    }

    /**
     *
     * @return
     */
    public Set<Integer> getCategoryIds() {
        return mCategoryIds;
    }

    /**
     *
     * @param categoryIds
     */
    public void setCategoryId(Set<Integer> categoryIds) {
        this.mCategoryIds = categoryIds;
    }

     /**
     *
     * @param categoryId
     */
    public void addCategoryId(Integer categoryId) {
        mCategoryIds.add(categoryId);
    }

    /**
     *
     * @param categoryId
     */
    public void removeCategoryId(Integer categoryId) {
        mCategoryIds.remove(categoryId);
    }

     /**
     * Check if a contact is in the Public category
     * 
     * @return
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
     * @param obj
     * 
     * @return
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
     * 
     * @return
     */
    @Override
    public String toString() {
        return "Contact{" + "mUserInfo=" + mUserInfo + ", mCategoryIds=" + mCategoryIds + '}';
    }
}
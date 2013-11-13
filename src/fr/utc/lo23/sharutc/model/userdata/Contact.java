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
     * @param peerId
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
     * @param categories
     */
    public void setCategoryId(Set<Integer> categoryIds) {
        this.mCategoryIds = categoryIds;
    }

    public void addCategoryId(Integer categoryId) {
        mCategoryIds.add(categoryId);
    }

    public void removeCategoryId(Integer categoryId) {
        mCategoryIds.remove(categoryId);
    }

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
}
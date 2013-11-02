package fr.utc.lo23.sharutc.model.userdata;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class Contact implements Serializable {

    private static final long serialVersionUID = 2934900084513371081L;
    private Long mPeerId;
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
    public Contact(Long peerId) {
        this.mPeerId = peerId;
        this.mCategoryIds = new HashSet<Integer>();
    }

    /**
     *
     * @return
     */
    public Long getPeerId() {
        return mPeerId;
    }

    /**
     *
     * @param peerId
     */
    public void setPeerId(Long peerId) {
        this.mPeerId = peerId;
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
}

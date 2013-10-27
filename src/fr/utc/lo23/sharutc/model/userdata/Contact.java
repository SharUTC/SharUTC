package fr.utc.lo23.sharutc.model.userdata;

import java.io.Serializable;

/**
 *
 */
public class Contact implements Serializable {

    private static final long sSerialVersionUID = 2934900084513371081L;
    private Long mPeerId;
    private Categories mCategories;

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
        this.mCategories = new Categories();
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
    public Categories getCategories() {
        return mCategories;
    }

    /**
     *
     * @param categories
     */
    public void setCategorieds(Categories categories) {
        this.mCategories = categories;
    }
}

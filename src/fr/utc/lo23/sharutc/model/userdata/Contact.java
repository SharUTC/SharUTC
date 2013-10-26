package fr.utc.lo23.sharutc.model.userdata;

import java.io.Serializable;

/**
 *
 */
public class Contact implements Serializable {

    private static final long serialVersionUID = 2934900084513371081L;
    private Long peerId;
    private Categories categories;

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
        this.peerId = peerId;
        this.categories = new Categories();
    }

    /**
     *
     * @return
     */
    public Long getPeerId() {
        return peerId;
    }

    /**
     *
     * @param peerId
     */
    public void setPeerId(Long peerId) {
        this.peerId = peerId;
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
    public void setCategorieds(Categories categories) {
        this.categories = categories;
    }
}

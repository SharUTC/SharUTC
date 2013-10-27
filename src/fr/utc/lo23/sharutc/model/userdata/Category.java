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
    private Integer mId;
    private String mMame;

    /**
     *
     */
    public Category() {
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
        return mMame;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.mMame = name;
    }
}

package fr.utc.lo23.sharutc.model.userdata;

import java.io.Serializable;

/**
 *
 */
public class Category implements Serializable {

    private static final long serialVersionUID = 3790688676428360970L;
    public static final Integer PUBLIC_CATEGORY_ID = new Integer(0);
    public static final String PUBLIC_CATEGORY_NAME = "Public";
    /**
     * Undeletable category ID
     */
    //FIXME ca ne fonctionnera pas comme ça, la valeur sera perdue.
    public static Integer IDS_SEQUENCE = new Integer(0);
    private Integer mId;
    private String mName;

    /**
     * Default constructor
     */
    public Category() {
    }

    /**
     *
     * @param name
     */
    public Category(String name) {
        this.mId = IDS_SEQUENCE++;
        this.mName = name;
    }

    /**
     *
     * @param mId
     * @param mName
     */
    public Category(Integer mId, String mName) {
        this.mId = mId;
        this.mName = mName;
        
        if (this.mId >= IDS_SEQUENCE)
        IDS_SEQUENCE = this.mId + 1;
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
}
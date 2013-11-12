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
    public static Integer IDS_SEQUENCE = new Integer(0);
    private Integer mId;
    private String mName;

    /**
     *
     */
    public Category() {
        
    }
    
     /**
     *
     */
    public Category(String name) {
        this.mId = IDS_SEQUENCE ++;
        this.mName = name;
    }

     /**
     *
     */
    public Category(Integer mId, String mName) {
        this.mId = mId;
        this.mName = mName;
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
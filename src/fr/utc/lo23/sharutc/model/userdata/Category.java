package fr.utc.lo23.sharutc.model.userdata;

import java.io.Serializable;

/**
 *
 */
public class Category implements Serializable {

    private static final long serialVersionUID = 3790688676428360970L;
    public static final Integer PUBLIC_CATEGORY_ID = new Integer(0);
    public static final String PUBLIC_CATEGORY_NAME = "Public";

    private Integer mId;
    private String mName;

    /**
     * Default constructor
     */
    public Category() {
    }

    /**
     *
     * @param mId
     * @param mName
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
     * 
     * @return
     */
    @Override
    public String toString() {
        return "Category{" + "mId=" + mId + ", mName=" + mName + '}';
    }
    
    
}
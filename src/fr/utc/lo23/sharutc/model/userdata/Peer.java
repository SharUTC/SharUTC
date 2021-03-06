package fr.utc.lo23.sharutc.model.userdata;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * Represent a peer
 */
public class Peer implements Serializable {

    private static final long serialVersionUID = 8410656879206380403L;
    private PropertyChangeSupport mPropertyChangeSupport = new PropertyChangeSupport(this);
    /**
     * Unique ID generated at profile creation (new Date())
     */
    private long mId;
    /**
     * lattest knwon peer name displayed in comments
     */
    private String mDisplayName;

    /**
     * Default constructor
     */
    public Peer() {
    }

    /**
     * Constructor
     *
     * @param id
     * @param displayName
     */
    public Peer(long id, String displayName) {
        this.mId = id;
        this.mDisplayName = displayName;
    }

    /**
     * Return the peer id
     *
     * @return the peer id
     */
    public long getId() {
        return mId;
    }

    /**
     * Set the peer id
     *
     * @param id - the peer id
     */
    public void setId(long id) {
        this.mId = id;
    }

    /**
     * Return the peer name
     *
     * @return the peer name
     */
    public String getDisplayName() {
        return mDisplayName;
    }

    /**
     * Set the peer name
     *
     * @param displayName
     */
    public void setDisplayName(String displayName) {
        String oldDisplayName = this.mDisplayName;
        this.mDisplayName = displayName;
        mPropertyChangeSupport.firePropertyChange(Property.DISPLAY_NAME.name(), oldDisplayName, displayName);
    }

    /**
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        mPropertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        mPropertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     *
     */
    public enum Property {

        /**
         *
         */
        DISPLAY_NAME
    }

    /**
     * Override the method hashCode
     *
     * @return the hash
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.mId ^ (this.mId >>> 32));
        hash = 97 * hash + (this.mDisplayName != null ? this.mDisplayName.hashCode() : 0);
        return hash;
    }

    /**
     * Override the method equals
     *
     * @param obj
     * @return a boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Peer other = (Peer) obj;
        if (!new Long(this.mId).equals(new Long(other.mId))) {
            return false;
        }
        if ((this.mDisplayName == null) ? (other.mDisplayName != null) : !this.mDisplayName.equals(other.mDisplayName)) {
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
        return "Peer{" + "id=" + mId + ", name=" + mDisplayName + '}';
    }
}

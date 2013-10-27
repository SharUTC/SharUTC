package fr.utc.lo23.sharutc.model.userdata;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 *
 */
public class Peer implements Serializable {

    private static final long sSerialVersionUID = 8410656879206380403L;
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
     * peer ip address currently in use, specific to network
     */
    private String mIpAddress;

    /**
     * Default constructor
     */
    public Peer() {
    }

    /**
     *
     * @param id
     * @param displayName
     */
    public Peer(long id, String displayName) {
        this.mId = id;
        this.mDisplayName = displayName;
    }

    /**
     *
     * @param id
     * @param displayName
     * @param ipAddress
     */
    public Peer(long id, String displayName, String ipAddress) {
        this.mId = id;
        this.mDisplayName = displayName;
        this.mIpAddress = ipAddress;
    }

    /**
     *
     * @return
     */
    public long getId() {
        return mId;
    }

    /**
     *
     * @param id
     */
    public void setId(long id) {
        this.mId = id;
    }

    /**
     *
     * @return
     */
    public String getDisplayName() {
        return mDisplayName;
    }

    /**
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
     * @return
     */
    public String getIpAddress() {
        return mIpAddress;
    }

    /**
     * no listener for this attribute since ip doesn't change and the view
     * doesn't display it anyway
     *
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.mIpAddress = ipAddress;
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
}

package fr.utc.lo23.sharutc.model.userdata;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 *
 */
public class Peer implements Serializable {

    private static final long serialVersionUID = 8410656879206380403L;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    /**
     * Unique ID generated at profile creation (new Date())
     */
    private long id;
    /**
     * lattest knwon peer name displayed in comments
     */
    private String displayName;
    /**
     * peer ip address currently in use, specific to network
     */
    private String ipAddress;

    // keep no arg. constructor in bean
    /**
     *
     */
    public Peer() {
    }

    /**
     *
     * @param id
     * @param displayName
     */
    public Peer(long id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    /**
     *
     * @param id
     * @param displayName
     * @param ipAddress
     */
    public Peer(long id, String displayName, String ipAddress) {
        this.id = id;
        this.displayName = displayName;
        this.ipAddress = ipAddress;
    }

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     *
     * @param displayName
     */
    public void setDisplayName(String displayName) {
        String oldDisplayName = this.displayName;
        this.displayName = displayName;
        propertyChangeSupport.firePropertyChange(Property.DISPLAY_NAME.name(), oldDisplayName, displayName);
    }

    /**
     *
     * @return
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * no listener for this attribute since ip doesn't change and the view
     * doesn't display it anyway
     *
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
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

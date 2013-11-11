package fr.utc.lo23.sharutc.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * Users may attach a score to a music. Only one instance per music and user is
 * possible. Score value goes between 1 and 5, a score of 0 leads to removing a
 * Score instance from the music (at command/service level)
 *
 */
public class Score implements Serializable {

    private static final long serialVersionUID = 8217480208364879598L;
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 5;
    @JsonIgnore
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    /**
     * the score value
     */
    private Integer mValue;
    /**
     * the id of the peer that added this score
     */
    private Long mPeerId;

    /**
     *
     */
    public Score() {
    }

    /**
     *
     * @param value the score value, between 1 and 5
     * @param peerId the id of the peer who added this score
     */
    public Score(Integer value, Long peerId) {
        this.mValue = value;
        this.mPeerId = peerId;
    }

    /**
     * Return the score value, between 1 and 5
     *
     * @return the score value, between 1 and 5
     */
    public Integer getValue() {
        return mValue;
    }

    /**
     * Set the score value, between 1 and 5
     *
     * @param value the score value, between 1 and 5
     */
    public void setValue(Integer value) {
        Integer oldValue = this.mValue;
        this.mValue = value;
        propertyChangeSupport.firePropertyChange(Property.VALUE.name(), oldValue, value);
    }

    /**
     * Return the id of the peer who added this score
     *
     * @return the id of the peer who added this score
     */
    public Long getPeerId() {
        return mPeerId;
    }

    /**
     * Set the id of the peer who added this score
     *
     * @param peerId the id of the peer who added this score
     */
    public void setPeerId(Long peerId) {
        this.mPeerId = peerId;
    }

    /**
     * Add the listener in parameter to the list of listeners that may be
     * notified
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes the listener in parameter to the list of listeners that may be
     * notified
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null && obj instanceof Score && ((Score) obj).mPeerId.equals(this.mPeerId) && ((Score) obj).mValue.equals(this.mValue));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.mValue != null ? this.mValue.hashCode() : 0);
        hash = 41 * hash + (this.mPeerId != null ? this.mPeerId.hashCode() : 0);
        return hash;
    }

    /**
     *
     */
    public enum Property {

        /**
         *
         */
        VALUE
    }
}

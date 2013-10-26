package fr.utc.lo23.sharutc.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 *

 */
public class Score implements Serializable {
    
    private static final long serialVersionUID = 8217480208364879598L;
    @JsonIgnore
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private Integer value;
    private Long peerId;
    
    /**
     *
     */
    public Score() {
    }
    
    /**
     *
     * @param value
     * @param peerId
     */
    public Score(Integer value, Long peerId) {
        this.value = value;
        this.peerId = peerId;
    }
    
    /**
     *
     * @return
     */
    public Integer getValue() {
        return value;
    }
    
    /**
     *
     * @param value
     */
    public void setValue(Integer value) {
        Integer oldValue = this.value;
        this.value = value;
        propertyChangeSupport.firePropertyChange(Property.VALUE.name(), oldValue, value);
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
        VALUE
    }
}

package fr.utc.lo23.sharutc.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 */
public class Comment implements Serializable {

    private static final long serialVersionUID = -4908693993402023011L;
    @JsonIgnore
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private Integer index;
    private String text;
    private Long peerId;
    private Date creationDate;
    private String peerDisplayName;

    /**
     *
     */
    public Comment() {
    }

    /**
     *
     * @param index
     * @param text
     * @param peerId
     * @param creationDate
     */
    public Comment(Integer index, String text, Long peerId, Date creationDate) {
        this.index = index;
        this.text = text;
        this.peerId = peerId;
        this.creationDate = creationDate;
    }

    /**
     *
     * @return
     */
    public Integer getIndex() {
        return index;
    }

    /**
     *
     * @param index
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     */
    public void setText(String text) {
        String oldText = this.text;
        this.text = text;
        propertyChangeSupport.firePropertyChange(Property.TEXT.name(), oldText, text);
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
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     *
     * @param creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     *
     * @return
     */
    public String getPeerDisplayName() {
        return peerDisplayName;
    }

    /**
     *
     * @param peerDisplayName
     */
    public void setPeerDisplayName(String peerDisplayName) {
        this.peerDisplayName = peerDisplayName;
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
        TEXT
    }
}

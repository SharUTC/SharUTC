package fr.utc.lo23.sharutc.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;

/**
 * Users may add comments to a musics, a comment is a text with its creation
 * date, its author, and an index. The index is relative to a Music, its purpose
 * is to be able to identify the comment to delete or update when the music
 * belongs to a peer
 *
 * the peerDisplayName is required to name unknown peers when others are reading
 * comments, it must be loaded with each comment when a search is being
 * performed
 *
 * There is a listener on the text only, if ever we update text edits in
 * comments in live
 */
public class Comment implements Serializable {

    private static final long serialVersionUID = -4908693993402023011L;
    @JsonIgnore
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private Integer mIndex;
    private String mText;
    private Long mPeerId;
    private Date mCreationDate;
    private String mPeerDisplayName;

    /**
     * Empty constructor, used for Json
     */
    public Comment() {
    }

    //TODO there are others constructors to write here, index might be setted another way or moment
    //
    /**
     *
     * @param index
     * @param text
     * @param peerId
     * @param creationDate
     */
    public Comment(Integer index, String text, Long peerId, Date creationDate) {
        this.mIndex = index;
        this.mText = text;
        this.mPeerId = peerId;
        this.mCreationDate = creationDate;
    }

    /**
     *
     * @return
     */
    public Integer getIndex() {
        return mIndex;
    }

    /**
     *
     * @param index
     */
    public void setIndex(Integer index) {
        this.mIndex = index;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return mText;
    }

    /**
     *
     * @param text
     */
    public void setText(String text) {
        String oldText = this.mText;
        this.mText = text;
        propertyChangeSupport.firePropertyChange(Property.TEXT.name(), oldText, text);
    }

    /**
     *
     * @return
     */
    public Long getPeerId() {
        return mPeerId;
    }

    /**
     *
     * @param peerId
     */
    public void setPeerId(Long peerId) {
        this.mPeerId = peerId;
    }

    /**
     *
     * @return
     */
    public Date getCreationDate() {
        return mCreationDate;
    }

    /**
     *
     * @param creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.mCreationDate = creationDate;
    }

    /**
     *
     * @return
     */
    public String getPeerDisplayName() {
        return mPeerDisplayName;
    }

    /**
     *
     * @param peerDisplayName
     */
    public void setPeerDisplayName(String peerDisplayName) {
        this.mPeerDisplayName = peerDisplayName;
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

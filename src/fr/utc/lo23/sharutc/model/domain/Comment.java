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

    private static Integer sCurrentIndex = 0;
    private static final long serialVersionUID = -4908693993402023011L;
    @JsonIgnore
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private Integer mIndex;
    private String mText;
    private Long mAuthorPeerId;
    private Date mCreationDate;
    private String mAuthorName;

    /**
     * Empty constructor, used for Json
     */
    public Comment() {
    }

    public Comment(Comment comment) {
        this.mIndex = new Integer(comment.mIndex);
        this.mText = comment.mText;
        this.mAuthorPeerId = new Long(comment.mAuthorPeerId);
        this.mCreationDate = new Date(comment.mCreationDate.getTime());
        this.mAuthorName = comment.mAuthorName;
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
        this.mAuthorPeerId = peerId;
        this.mCreationDate = creationDate;
    }
    
    public static Integer getCurrentIndex() {
        return sCurrentIndex;
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
        sCurrentIndex = Math.max(sCurrentIndex, mIndex);
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
    public Long getAuthorPeerId() {
        return mAuthorPeerId;
    }

    /**
     *
     * @param authorPeerId
     */
    public void setAuthorPeerId(Long authorPeerId) {
        this.mAuthorPeerId = authorPeerId;
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
    public String getAuthorName() {
        return mAuthorName;
    }

    /**
     *
     * @param authorName
     */
    public void setAuthorName(String authorName) {
        this.mAuthorName = authorName;
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

    @Override
    public Comment clone() {
        return new Comment(this);
    }
}

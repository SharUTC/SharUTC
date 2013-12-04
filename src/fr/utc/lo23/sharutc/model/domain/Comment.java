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
    private Long mAuthorPeerId;
    private Date mCreationDate;
    private String mAuthorName;

    /**
     * Empty constructor, used for Json
     */
    public Comment() {
    }

    /**
     * Constructor
     *
     * @param comment The comment to copie
     */
    public Comment(Comment comment) {
        this.mIndex = new Integer(comment.mIndex);
        this.mText = comment.mText;
        this.mAuthorPeerId = new Long(comment.mAuthorPeerId);
        this.mCreationDate = new Date(comment.mCreationDate.getTime());
        this.mAuthorName = comment.mAuthorName;
    }

    /**
     * Constructor
     *
     * @param index The index of the comment in the list of a music
     * @param text The content of the comment
     * @param peerId The id of the peer
     * @param creationDate The date of the comment
     */
    public Comment(Integer index, String text, Long peerId, Date creationDate) {
        this.mIndex = index;
        this.mText = text;
        this.mAuthorPeerId = peerId;
        this.mCreationDate = creationDate;
    }

    /**
     * Constructor
     *
     * @param text The content of the comment
     * @param peerId The id of the peer
     */
    public Comment(String text, Long peerId) {
        this(new Integer(0), text, peerId, new Date());
    }

    /**
     * Get the index
     *
     * @return The index
     */
    public Integer getIndex() {
        return mIndex;
    }

    /**
     * Set the index
     *
     * @param index The index
     */
    public void setIndex(Integer index) {
        this.mIndex = index;
    }

    /**
     * Get the text
     *
     * @return The text
     */
    public String getText() {
        return mText;
    }

    /**
     * Set the text
     *
     * @param text The text
     */
    public void setText(String text) {
        String oldText = this.mText;
        this.mText = text;
        propertyChangeSupport.firePropertyChange(Property.TEXT.name(), oldText, text);
    }

    /**
     * Get the author id
     *
     * @return The author id
     */
    public Long getAuthorPeerId() {
        return mAuthorPeerId;
    }

    /**
     * Set the author id
     *
     * @param authorPeerId The author id
     */
    public void setAuthorPeerId(Long authorPeerId) {
        this.mAuthorPeerId = authorPeerId;
    }

    /**
     * Get the date of creation
     *
     * @return The date of creation
     */
    public Date getCreationDate() {
        return mCreationDate;
    }

    /**
     * Set the date of creation
     *
     * @param creationDate The date of creation
     */
    public void setCreationDate(Date creationDate) {
        this.mCreationDate = creationDate;
    }

    /**
     * Get the author's name
     *
     * @return The author's name
     */
    public String getAuthorName() {
        return mAuthorName;
    }

    /**
     * Set the author's name
     *
     * @param authorName The author's name
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

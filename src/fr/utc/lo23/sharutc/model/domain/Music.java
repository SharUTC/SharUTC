package fr.utc.lo23.sharutc.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Peer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main object of the application, contains all relatives informations about
 * the music file in memory via the catalogs.
 * <p/>
 * id3tags are parsed when the music is added locally, user changes are reported
 * to the file also, a copy of these informations is stored with the Music
 * object to enhance search trough a list of musics
 * <p/>
 * Music equality is based on the file Byte[] hash value only
 */
@JsonInclude(value = Include.NON_NULL)
public class Music implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(Music.class);
    private static final long serialVersionUID = 6722258623736849911L;
    @JsonIgnore
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private Long mId;
    private String mFileName;
    private String mRealName;
    /**
     * No JsonIgnore annotation, but when searching a music, file must be set to
     * null before returning the results, file must be present for transfers
     */
    @JsonInclude(value = Include.NON_NULL)
    private Byte[] mFile;
    private Long mFrames;
    @JsonInclude(value = Include.NON_NULL)
    private Boolean mFileMissing;
    private Long mOwnerPeerId;
    private Integer mHash;
    private Set<Integer> mCategoryIds;
    private String mTitle;
    private String mArtist;
    private String mAlbum;
    private String mTrack;
    private Integer mTrackLength;
    private String mYear;
    private List<Comment> mComments;
    private Integer mCurrentMaxCommentIndex;
    private Set<Score> mScores;
    private Set<String> mTags;
    private Boolean mMayReadInfo;
    private Boolean mMayListen;
    private Boolean mMayCommentAndNote;

    /**
     *
     */
    public Music() {
        this.mComments = new ArrayList<Comment>();
        this.mCurrentMaxCommentIndex = 0;
        this.mScores = new HashSet<Score>();
        this.mCategoryIds = new HashSet<Integer>();
        this.mCategoryIds.add(Category.PUBLIC_CATEGORY_ID);
        this.mTags = new HashSet<String>();
    }

    /**
     * Clone for sending result list
     *
     * @param music
     */
    public Music(Music music) {

        this.mId = new Long(music.mId);
        this.mFileName = music.mFileName;
        this.mRealName = music.mRealName;
        if (music.getFileBytes() != null) {
            this.mFile = new Byte[music.getFileBytes().length];
            System.arraycopy(music.mFile, 0, this.mFile, 0, music.getFileBytes().length);
        } else {
            this.mFile = null;
        }
        this.mFrames = music.mFrames;
        this.mFileMissing = music.mFileMissing;
        this.mOwnerPeerId = new Long(music.mOwnerPeerId);
        this.mHash = new Integer(music.mHash);

        this.mCategoryIds = new HashSet<Integer>();
        for (Integer i : music.mCategoryIds) {
            this.mCategoryIds.add(new Integer(i));
        }
        this.mTitle = music.mTitle;
        this.mArtist = music.mArtist;
        this.mAlbum = music.mAlbum;
        this.mTrack = music.mTrack;
        this.mTrackLength = new Integer(music.mTrackLength);
        this.mYear = music.mYear;
        this.mComments = new ArrayList<Comment>();
        // using a clone also for contained classes, always
        for (Comment comment : music.mComments) {
            this.mComments.add(comment.clone());
        }
        this.mCurrentMaxCommentIndex = music.mCurrentMaxCommentIndex;
        this.mScores = new HashSet<Score>();
        for (Score score : music.mScores) {
            this.mScores.add(score);
        }
        this.mTags = music.mTags;
        this.mMayReadInfo = music.mMayReadInfo;
        this.mMayListen = music.mMayListen;
        this.mMayCommentAndNote = music.mMayCommentAndNote;
    }

    /**
     * To use for importing a new local file
     *
     * @param id
     * @param ownerPeerId
     * @param file
     * @param fileName
     * @param title
     * @param artist
     * @param album
     * @param track
     * @param trackLength
     */
    public Music(Long id, Long ownerPeerId, Byte[] file, String fileName, String realName, Integer hashcode, String title, String artist, String album, String year, String track, Integer trackLength, Long frames) {
        this.mId = id;
        this.mFileName = fileName;
        this.mRealName = realName;
        this.mFileMissing = false;
        this.mOwnerPeerId = ownerPeerId;
        this.mHash = hashcode;
        this.mCategoryIds = new HashSet<Integer>();
        this.mCategoryIds.add(Category.PUBLIC_CATEGORY_ID);
        this.mFile = file;
        this.mFrames = frames;
        this.mComments = new ArrayList<Comment>();
        this.mCurrentMaxCommentIndex = 0;
        this.mScores = new HashSet<Score>();
        this.mTags = new HashSet<String>();
        this.mTitle = title;
        this.mArtist = artist;
        this.mAlbum = album;
        this.mYear = year;
        this.mTrack = track;
        this.mTrackLength = trackLength;
        this.mMayReadInfo = null;
        this.mMayListen = null;
        this.mMayCommentAndNote = null;
    }

    /**
     * The id of this music, unique only locally
     *
     * @return the id of this music
     */
    public Long getId() {
        return mId;
    }

    /**
     * Set the id of this music
     *
     * @param id the id of this music
     */
    public void setId(Long id) {
        this.mId = id;
    }

    /**
     * Return the name of the local music file, may differ from RealName if
     * several files have the same RealName
     *
     * @return the name of the local music file
     */
    public String getFileName() {
        return mFileName;
    }

    /**
     * Set the name of the local music file.
     *
     * @param fileName the name of the local music file (as stored on HDD)
     */
    public void setFileName(String fileName) {
        String oldFileName = this.mFileName;
        this.mFileName = fileName;
        propertyChangeSupport.firePropertyChange(Music.Property.FILENAME.name(), oldFileName, fileName);
    }

    /**
     * Return the name of the local music file as it was before adding it to the
     * application
     *
     * @return the real name of the local music file (its original name)
     */
    public String getRealName() {
        return mRealName;
    }

    /**
     * Set the original name of the file, the one used out of the application
     *
     * @param realName the original name of the file, the one used out of the
     * application
     */
    public void setRealName(String realName) {
        this.mRealName = realName;
    }

    /**
     * Return the content of the file if loaded. Most of the time this value is
     * empty, only the player needs these data, and 2 commands (for sending a
     * music).
     *
     * @return the content of the file, if loaded
     */
    public Byte[] getFileBytes() {
        return mFile;
    }

    /**
     * Set the content of the file. A copy of the local music should be used to
     * load the data, instead of setting this value and save it with the model
     * tree
     *
     * @param bytes the file content to set
     */
    public void setFileBytes(Byte[] bytes) {
        this.mFile = bytes;
    }

    /**
     * Return true if the file has not been found
     *
     * @return true if the file has not been found
     */
    public Boolean getFileMissing() {
        return mFileMissing;
    }

    /**
     * @param fileMissing
     */
    public void setFileMissing(Boolean fileMissing) {
        Boolean oldFileMissing = this.mFileMissing;
        this.mFileMissing = fileMissing;
        propertyChangeSupport.firePropertyChange(Music.Property.FILE_MISSING.name(), oldFileMissing, fileMissing);
    }

    /**
     * Return the length of the music in frames
     *
     * @return the length of the music in frames
     */
    public Long getFrames() {
        return mFrames;
    }

    /**
     * Set the length of the music in frames
     *
     * @param frames the length of the music in frames
     */
    public void setFrames(Long frames) {
        this.mFrames = frames;
    }

    /**
     * Return the id of the music owner, where is stored this music
     *
     * @return the id of the music owner, where is stored this music
     */
    public Long getOwnerPeerId() {
        return mOwnerPeerId;
    }

    /**
     * Set the id of the music owner, where is stored this music
     *
     * @param ownerPeerId the id of the music owner, where is stored this music
     */
    public void setOwnerPeerId(Long ownerPeerId) {
        this.mOwnerPeerId = ownerPeerId;
    }

    /**
     * The hash value of the music
     *
     * @return the hash value of the music
     */
    public Integer getHash() {
        return mHash;
    }

    /**
     * Set the hash value of the music
     *
     * @param hash the hash value of the music
     */
    public void setHash(Integer hash) {
        this.mHash = hash;
    }

    /**
     * Return the list of categories to which this music is linked
     *
     * @return the list of categories to which this music is linked
     */
    public Set<Integer> getCategoryIds() {
        return Collections.unmodifiableSet(mCategoryIds);
    }

    /**
     * Set the list of categories to which this music is linked
     *
     * @param categoryIds the list of categories to which this music is linked
     */
    public void setCategoryIds(Set<Integer> categoryIds) {
        this.mCategoryIds = categoryIds;
    }

    /**
     * Attach this music to a categorie.
     *
     * @param categoryId the id of the category that is to be linked to this
     * music
     */
    public void addCategoryId(Integer categoryId) {
        Set<Integer> oldCategoryIds = this.mCategoryIds;
        Set<Integer> categoryIds = new HashSet<Integer>(this.mCategoryIds);
        if (categoryIds.add(categoryId)) {
            this.mCategoryIds = categoryIds;
            propertyChangeSupport.firePropertyChange(Music.Property.CATEGORY_IDS.name(), oldCategoryIds, this.mCategoryIds);
        }
    }

    /**
     * Detach this music from a categorie.
     *
     * @param categoryId the id of the category that is to be unlinked to this
     * music
     */
    public void removeCategoryId(Integer categoryId) {
        Set<Integer> oldCategoryIds = this.mCategoryIds;
        Set<Integer> categoryIds = new HashSet<Integer>(this.mCategoryIds);
        if (categoryIds.remove(categoryId)) {
            this.mCategoryIds = categoryIds;
            propertyChangeSupport.firePropertyChange(Music.Property.CATEGORY_IDS.name(), oldCategoryIds, this.mCategoryIds);
        }
    }

    /**
     * Return the title of the music (from id3Tag)
     *
     * @return the title of the music
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Set the title of the music
     *
     * @param title the title of the music
     */
    public void setTitle(String title) {
        String oldTitle = this.mTitle;
        this.mTitle = title;
        propertyChangeSupport.firePropertyChange(Music.Property.TITLE.name(), oldTitle, title);
    }

    /**
     * Return the artist of the music
     *
     * @return the artist of the music
     */
    public String getArtist() {
        return mArtist;
    }

    /**
     * Set the artist of the music
     *
     * @param artist the artist of the music
     */
    public void setArtist(String artist) {
        String oldArtist = this.mArtist;
        this.mArtist = artist;
        propertyChangeSupport.firePropertyChange(Music.Property.ARTIST.name(), oldArtist, artist);
    }

    /**
     * Return the album of the music
     *
     * @return the album of the music
     */
    public String getAlbum() {
        return mAlbum;
    }

    /**
     * Set the album of the music
     *
     * @param album the album of the music
     */
    public void setAlbum(String album) {
        String oldAlbum = this.mAlbum;
        this.mAlbum = album;
        propertyChangeSupport.firePropertyChange(Music.Property.ALBUM.name(), oldAlbum, album);
    }

    /**
     * Return the track of the music
     *
     * @return the track of the music
     */
    public String getTrack() {
        return mTrack;
    }

    /**
     * Set the track of the music
     *
     * @param num the track of the music
     */
    public void setTrack(String track) {
        String oldTrack = this.mTrack;
        this.mTrack = track;
        propertyChangeSupport.firePropertyChange(Music.Property.TRACK.name(), oldTrack, track);
    }

    /**
     * Return the music duration in seconds
     *
     * @return the music duration in seconds
     */
    public Integer getTrackLength() {
        return mTrackLength;
    }

    /**
     * Set the music duration in seconds
     *
     * @param trackLength the music duration in seconds
     */
    public void setTrackLength(Integer trackLength) {
        this.mTrackLength = trackLength;
    }

    /**
     * Return the year of the music
     *
     * @return the year of the music
     */
    public String getYear() {
        return mYear;
    }

    /**
     * Set the year of the music
     *
     * @param year the year of the music
     */
    public void setYear(String year) {
        String oldYear = this.mYear;
        this.mYear = year;
        propertyChangeSupport.firePropertyChange(Music.Property.YEAR.name(), oldYear, year);
    }

    /**
     * Return the list of comments
     *
     * @return The list of comments
     */
    public List<Comment> getComments() {
        return Collections.unmodifiableList(mComments);
    }

    /**
     * Return a comment of the peer
     *
     * @param peer The author of the comment
     * @param commentIndex The index of comment
     * @return A comment of the peer if it is attached and found in this music
     */
    public Comment getComment(Peer peer, Integer commentIndex) {
        return peer == null ? null : getComment(peer.getId(), commentIndex);
    }

    /**
     * Return a comment of the peer
     *
     * @param peerId The peerId of the author of the comment
     * @param commentIndex The index of comment
     * @return A comment of the peer if it is attached and found in this music
     */
    public Comment getComment(Long peerId, Integer commentIndex) {
        Comment comment = null;
        if (peerId != null && commentIndex != null) {
            for (Comment tmpComment : getComments()) {
                if (tmpComment.getAuthorPeerId().equals(peerId)
                        && tmpComment.getIndex().equals(commentIndex)) {
                    comment = tmpComment;
                }
            }
        }
        return comment;
    }

    /**
     * Define the list of comments
     *
     * @param comments The list of comments
     */
    public void setComments(List<Comment> comments) {
        this.mComments = comments;
        for (Comment tmpComment : getComments()) {
            this.mCurrentMaxCommentIndex = Math.max(tmpComment.getIndex(), this.mCurrentMaxCommentIndex);
        }
    }


    /**
     * use to edit a comment
     *
     * @param peer         editor id
     * @param commentIndex comment index
     * @param comment      new comment
     */
    public void editComment(Peer peer, Integer commentIndex, String comment) {
        Comment commentToEdit = this.getComment(peer, commentIndex);
        if (commentToEdit != null) {
            commentToEdit.setText(comment);
            propertyChangeSupport.firePropertyChange(Property.COMMENT_UPDATE.name(), null, commentToEdit);
        } else {
            log.warn("editComment : Comment to edit not found");
        }
    }

    /**
     * Add a comment to this music. Send COMMENTS update
     *
     * @param comment The comment to add
     */
    public void addComment(Comment comment) {
        comment.setIndex(++mCurrentMaxCommentIndex);
        if (mComments.add(comment)) {
            propertyChangeSupport.firePropertyChange(Music.Property.COMMENTS.name(), null, mComments);
        }
    }

    /**
     * Remove a comment from this music. Send COMMENTS update
     *
     * @param comment The comment to remove
     */
    public void removeComment(Comment comment) {
        if (mComments.remove(comment)) {
            propertyChangeSupport.firePropertyChange(Music.Property.COMMENTS.name(), null, mComments);
        }
    }

    /**
     * Return the list of scores
     *
     * @return The list of scores
     */
    public Set<Score> getScores() {
        return Collections.unmodifiableSet(mScores);
    }

    /**
     * Return the score of the peer
     *
     * @param peer The peer
     * @return The list of scores
     */
    public Score getScore(Peer peer) {
        return peer == null ? null : getScore(peer.getId());
    }

    /**
     * Return the score of the peer
     *
     * @param peerId The id of the peer
     * @return The list of scores
     */
    public Score getScore(Long peerId) {
        Score score = null;
        if (peerId != null) {
            for (Score tmpScore : getScores()) {
                if (tmpScore.getPeerId().equals(peerId)) {
                    score = tmpScore;
                }
            }
        }
        return score;
    }

    /**
     * Define the list of scores
     *
     * @param scores The list of scores
     */
    public void setScores(Set<Score> scores) {
        this.mScores = scores;
    }

    /**
     * Add a score to the list of scores. Send new average score in new value,
     * previous in old value
     *
     * @param score The score to add
     */
    public void addScore(Score score) {
        int oldAverageScore = getAverageScore();
        this.mScores.add(score);
        int averageScore = getAverageScore();
        propertyChangeSupport.firePropertyChange(Music.Property.SCORES.name(), oldAverageScore, averageScore);
    }

    /**
     * Remove a score to the list of scores. Send new average score in new
     * value, previous in old value
     *
     * @param score The score to remove
     */
    public void removeScore(Score score) {
        int oldAverageScore = getAverageScore();
        this.mScores.remove(score);
        int averageScore = getAverageScore();
        propertyChangeSupport.firePropertyChange(Music.Property.SCORES.name(), oldAverageScore, averageScore);
    }

    /**
     * Return the list of user tag of this music
     *
     * @return the list of user tag of this music
     */
    public Set<String> getTags() {
        return Collections.unmodifiableSet(mTags);
    }

    /**
     * Set the list of user tag of this music
     *
     * @param tags the list of user tag of this music
     */
    public void setTags(Set<String> tags) {
        this.mTags = tags;
    }

    /**
     * Tmp remote value to inform user if the music is accessible to another
     * peer
     *
     * @return true if this music is authorized to be read by another peer
     */
    public Boolean getMayReadInfo() {
        return mMayReadInfo;
    }

    /**
     * Set the right value telling if this music may be read by another peer
     *
     * @param mayReadInfo true if this music is authorized to be read by another
     * peer
     */
    public void setMayReadInfo(Boolean mayReadInfo) {
        this.mMayReadInfo = mayReadInfo;
    }

    /**
     * Tmp remote value to inform user if the music is playable by another peer
     *
     * @return true if this music is authorized to be played by another peer
     */
    public Boolean getMayListen() {
        return mMayListen;
    }

    /**
     * Set the right value telling if this music may be played by another peer
     *
     * @param mayListen true if this music is authorized to be played by another
     * peer
     */
    public void setMayListen(Boolean mayListen) {
        this.mMayListen = mayListen;
    }

    /**
     * Tmp remote value to inform user if another peer may Comment and Note a
     * music
     *
     * @return true if this music is authorized to be commented and noted
     */
    public Boolean getMayCommentAndNote() {
        return mMayCommentAndNote;
    }

    /**
     * Set the right value telling if this music may be commented and noted by
     * another peer
     *
     * @param mayCommentAndNote true if this music is authorized to be commented
     * and noted by another peer
     */
    public void setMayCommentAndNote(Boolean mayCommentAndNote) {
        this.mMayCommentAndNote = mayCommentAndNote;
    }

    /**
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Attach a new tag to a music. Whatever is the String in argument, only the
     * first letter will be upper case, other will be lower case. Send TAGS
     * update.
     *
     * @param tag the name of the tag
     * @return true is the tag was added
     */
    public boolean addTag(String tag) {
        boolean added = false;
        if (tag != null && tag.trim().length() > 0) {
            tag = tag.toLowerCase();
            tag = tag.substring(0, 1).toUpperCase() + (tag.length() > 1 ? tag.substring(1) : "");
            if (mTags.add(tag)) {
                propertyChangeSupport.firePropertyChange(Music.Property.TAGS.name(), null, tag);
                added = true;
            }
        }
        return added;
    }

    /**
     * Removes the given tag from this music. The tag value is also formatted
     * before removal. Send TAGS update.
     *
     * @param tag the name of the tag
     * @return true is the tag was removed
     */
    public boolean removeTag(String tag) {
        boolean removed = false;
        if (tag != null && tag.trim().length() > 0) {
            tag = tag.toLowerCase();
            tag = tag.substring(0, 1).toUpperCase() + (tag.length() > 1 ? tag.substring(1) : "");
            if (mTags.remove(tag)) {
                propertyChangeSupport.firePropertyChange(Music.Property.TAGS.name(), tag, null);
                removed = true;
            }
        }
        return removed;
    }

    @Override
    public boolean equals(Object obj) {
        // be careful, don't use public int hashCode() here, equals is realized with from music Bytes directly
        return (obj != null && obj instanceof Music
                && (((Music) obj).mHash.equals(this.mHash) /*|| ((Music) obj).mId.equals(this.mId) && ((Music) obj).mOwnerPeerId.equals(this.mOwnerPeerId)*/));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.mHash != null ? this.mHash.hashCode() : 0);
        return hash;
    }

    /**
     * Clear categories list and set fileMissing to false, in order to send this
     * music to a remote Peer
     */
    public void cleanForPreview() {
        mCategoryIds.clear();
        mFileMissing = false;
    }

    private int getAverageScore() {
        int averageScore = 0;
        if (mScores != null && !mScores.isEmpty()) {
            int i = 0;
            Iterator<Score> iter = mScores.iterator();
            while (iter.hasNext()) {
                averageScore += iter.next().getValue();
                i++;
            }
            averageScore /= (i != 0 ? i : 1);
        }
        return averageScore;
    }

    /**
     *
     */
    public enum Property {

        /**
         * On filename value change
         */
        FILENAME,
        /**
         * On title value change
         */
        TITLE,
        /**
         * On title value change
         */
        ARTIST,
        /**
         * On artist value change
         */
        ALBUM,
        /**
         * On album value change
         */
        TRACK,
        /**
         * On fileMissing value change
         */
        FILE_MISSING,
        /**
         * On year value change
         */
        YEAR,
        /**
         * When a tag is added, its name is available in new Value, when
         * removed, new value is null, removed tag name is available in old
         * value of event
         */
        TAGS,
        /**
         * When the list of categories is updated
         */
        CATEGORY_IDS,
        /**
         * When a score is added or removed
         */
        SCORES,
        /**
         * When a comment is added or removed, whole comments list is sent
         */
        COMMENTS
    }

    @Override
    public Music clone() {
        return new Music(this);
    }
}

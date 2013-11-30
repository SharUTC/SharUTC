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
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main object of the application, contains all relatives informations about
 * the music file in memory via the catalogs.
 *
 * id3tags are parsed when the music is added locally, user changes are reported
 * to the file also, a copy of these informations is stored with the Music
 * object to enhance search trough a list of musics
 *
 * Music equality is based on the file Byte[] hash value only
 *
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
     *
     * @return
     */
    public Long getId() {
        return mId;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.mId = id;
    }

    /**
     *
     * @return
     */
    public String getFileName() {
        return mFileName;
    }

    /**
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        String oldFileName = this.mFileName;
        this.mFileName = fileName;
        propertyChangeSupport.firePropertyChange(Property.FILENAME.name(), oldFileName, fileName);
    }

    /**
     *
     * @return
     */
    public String getRealName() {
        return mRealName;
    }

    /**
     *
     * @param realName
     */
    public void setRealName(String realName) {
        this.mRealName = realName;
    }

    /**
     *
     * @return
     */
    public Byte[] getFileBytes() {
        return mFile;
    }

    /**
     *
     * @param file
     */
    public void setFileBytes(Byte[] file) {
        this.mFile = file;
    }

    /**
     *
     * @return
     */
    public Boolean getFileMissing() {
        return mFileMissing;
    }

    /**
     *
     * @return
     */
    public Long getFrames() {
        return mFrames;
    }

    /**
     *
     * @param frames
     */
    public void setFrames(Long frames) {
        this.mFrames = frames;
    }

    /**
     *
     * @param fileMissing
     */
    public void setFileMissing(Boolean fileMissing) {
        Boolean oldFileMissing = this.mFileMissing;
        this.mFileMissing = fileMissing;
        propertyChangeSupport.firePropertyChange(Property.FILE_MISSING.name(), oldFileMissing, fileMissing);
    }

    /**
     *
     * @return
     */
    public Long getOwnerPeerId() {
        return mOwnerPeerId;
    }

    /**
     *
     * @param ownerPeerId
     */
    public void setOwnerPeerId(Long ownerPeerId) {
        this.mOwnerPeerId = ownerPeerId;
    }

    /**
     *
     * @return
     */
    public Integer getHash() {
        return mHash;
    }

    /**
     *
     * @param hash
     */
    public void setHash(Integer hash) {
        this.mHash = hash;
    }

    /**
     *
     * @return
     */
    public Set<Integer> getCategoryIds() {
        return Collections.unmodifiableSet(mCategoryIds);
    }

    /**
     *
     * @param categoryIds
     */
    public void setCategoryIds(Set<Integer> categoryIds) {
        this.mCategoryIds = categoryIds;
    }

    /**
     *
     * @param categoryId
     */
    public void addCategoryId(Integer categoryId) {
        Set<Integer> oldCategoryIds = this.mCategoryIds;
        Set<Integer> categoryIds = new HashSet<Integer>(this.mCategoryIds);
        if (categoryIds.add(categoryId)) {
            this.mCategoryIds = categoryIds;
            propertyChangeSupport.firePropertyChange(Property.CATEGORY_IDS.name(), oldCategoryIds, this.mCategoryIds);
        }
    }

    /**
     *
     * @param categoryId
     */
    public void removeCategoryId(Integer categoryId) {
        Set<Integer> oldCategoryIds = this.mCategoryIds;
        Set<Integer> categoryIds = new HashSet<Integer>(this.mCategoryIds);
        if (categoryIds.remove(categoryId)) {
            this.mCategoryIds = categoryIds;
            propertyChangeSupport.firePropertyChange(Property.CATEGORY_IDS.name(), oldCategoryIds, this.mCategoryIds);
        }
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        String oldTitle = this.mTitle;
        this.mTitle = title;
        propertyChangeSupport.firePropertyChange(Property.TITLE.name(), oldTitle, title);
    }

    /**
     *
     * @return
     */
    public String getArtist() {
        return mArtist;
    }

    /**
     *
     * @param artist
     */
    public void setArtist(String artist) {
        String oldArtist = this.mArtist;
        this.mArtist = artist;
        propertyChangeSupport.firePropertyChange(Property.ARTIST.name(), oldArtist, artist);
    }

    /**
     *
     * @return
     */
    public String getAlbum() {
        return mAlbum;
    }

    /**
     *
     * @param album
     */
    public void setAlbum(String album) {
        String oldAlbum = this.mAlbum;
        this.mAlbum = album;
        propertyChangeSupport.firePropertyChange(Property.ALBUM.name(), oldAlbum, album);
    }

    /**
     *
     * @return
     */
    public String getTrack() {
        return mTrack;
    }

    /**
     *
     * @param num
     */
    public void setTrack(String track) {
        String oldTrack = this.mTrack;
        this.mTrack = track;
        propertyChangeSupport.firePropertyChange(Property.TRACK.name(), oldTrack, track);
    }

    public Integer getTrackLength() {
        return mTrackLength;
    }

    public void setTrackLength(Integer trackLength) {
        this.mTrackLength = trackLength;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String year) {
        String oldYear = this.mYear;
        this.mYear = year;
        propertyChangeSupport.firePropertyChange(Property.YEAR.name(), oldYear, year);

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
     * @param peer The peer
     * @param commentIndex The index of comment
     * @return A comment of the peer
     */
    public Comment getComment(Peer peer, Integer commentIndex) {
        return peer == null ? null : getComment(peer.getId(), commentIndex);
    }

    /**
     * Return a comment of the peer
     *
     * @param peerId The id of the peer
     * @param commentIndex The index of comment
     * @return A comment of the peer
     */
    public Comment getComment(Long peerId, Integer commentIndex) {
        Comment comment = null;
        for (Comment tmpComment : getComments()) {
            if (tmpComment.getAuthorPeerId().equals(peerId)) {
                comment = tmpComment;
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
    }

    /**
     * Add a comment
     *
     * @param comment The comment
     */
    public void addComment(Comment comment) {
        comment.setIndex(++mCurrentMaxCommentIndex);
        this.mComments.add(comment);
    }

    /**
     * Remove a comment
     *
     * @param comment The comment
     */
    public void removeComment(Comment comment) {
        this.mComments.remove(comment);
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
        for (Score tmpScore : getScores()) {
            if (tmpScore.getPeerId().equals(peerId)) {
                score = tmpScore;
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
     * Add a score to the list of scores
     *
     * @param score The score to add
     */
    public void addScore(Score score) {
        this.mScores.add(score);
    }

    /**
     * Remove a score to the list of scores
     *
     * @param score The score to remove
     */
    public void removeScore(Score score) {
        this.mScores.remove(score);
    }

    /**
     *
     * @return
     */
    public Set<String> getTags() {
        return Collections.unmodifiableSet(mTags);
    }

    /**
     *
     * @param tags
     */
    public void setTags(Set<String> tags) {
        this.mTags = tags;
    }

    /**
     *
     * @return
     */
    public Boolean getMayReadInfo() {
        return mMayReadInfo;
    }

    /**
     *
     * @param mayReadInfo
     */
    public void setMayReadInfo(Boolean mayReadInfo) {
        this.mMayReadInfo = mayReadInfo;
    }

    /**
     *
     * @return
     */
    public Boolean getMayListen() {
        return mMayListen;
    }

    /**
     *
     * @param mayListen
     */
    public void setMayListen(Boolean mayListen) {
        this.mMayListen = mayListen;
    }

    /**
     *
     * @return
     */
    public Boolean getMayCommentAndNote() {
        return mMayCommentAndNote;
    }

    /**
     *
     * @param mayCommentAndNote
     */
    public void setMayCommentAndNote(Boolean mayCommentAndNote) {
        this.mMayCommentAndNote = mayCommentAndNote;
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

    public boolean addTag(String tag) {
        boolean added = false;
        if (tag != null && tag.trim().length() > 0) {
            tag = tag.toLowerCase();
            tag = tag.substring(0, 1).toUpperCase() + (tag.length() > 1 ? tag.substring(1) : "");
            if (mTags.add(tag)) {
                propertyChangeSupport.firePropertyChange(Property.TAGS.name(), null, tag);
                added = true;
            }
        }
        return added;
    }

    public boolean removeTag(String tag) {
        boolean removed = false;
        if (tag != null && tag.trim().length() > 0) {
            tag = tag.toLowerCase();
            tag = tag.substring(0, 1).toUpperCase() + (tag.length() > 1 ? tag.substring(1) : "");
            if (mTags.remove(tag)) {
                propertyChangeSupport.firePropertyChange(Property.TAGS.name(), tag, null);
                removed = true;
            }
        }
        return removed;
    }

    public void setCommentAuthor(Integer index, String authorName) {
        Comment comment = mComments.get(index);
        comment.setAuthorName(authorName);
    }

    @Override
    public boolean equals(Object obj) {
        // be careful, don't use public int hashCode() here, equals is realized with from music Bytes directly
        return (obj != null && obj instanceof Music
                && (((Music) obj).mHash.equals(this.mHash)
                || ((Music) obj).mId.equals(this.mId) && ((Music) obj).mOwnerPeerId.equals(this.mOwnerPeerId)));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.mHash != null ? this.mHash.hashCode() : 0);
        return hash;
    }

    public void cleanForPreview() {
        mCategoryIds.clear();
        mFileMissing = null;
    }

    /**
     *
     */
    public enum Property {

        /**
         *
         */
        FILENAME,
        /**
         *
         */
        LOCAL,
        /**
         *
         */
        TITLE,
        /**
         *
         */
        ARTIST,
        /**
         *
         */
        ALBUM,
        /**
         *
         */
        TRACK,
        /**
         *
         */
        FILE_MISSING,
        /**
         *
         */
        YEAR,
        /**
         *
         */
        TAGS,
        /**
         *
         */
        CATEGORY_IDS
    }

    @Override
    public Music clone() {
        return new Music(this);
    }
}

package fr.utc.lo23.sharutc.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.utc.lo23.sharutc.model.userdata.Categories;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *

 */
public class Music implements Serializable {

    private static final long serialVersionUID = 6722258623736849911L;
    @JsonIgnore
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private Long id;
    private String fileName;
    private String realName;
    /**
     * No JsonIgnore annotation, but when searching a music, file must be set to
     * null before returning the results, file must be present for transfers
     */
    private File file;
    private Boolean fileMissing;
    private Long ownerPeerId;
    private Integer hash;
    private Categories categories;
    private String title;
    private String artist;
    private String album;
    private Integer num;
    private List<Comment> comments;
    private Set<Score> scores;
    private Set<String> tags;
    private Boolean mayReadInfo;
    private Boolean mayListen;
    private Boolean mayCommentAndNote;

    /**
     *
     */
    public Music() {
    }

    /**
     * To use for importing a new local file
     *
     * @param id
     * @param fileName
     * @param realName
     * @param fileMissing
     * @param ownerPeerId
     * @param hash
     * @param categories
     * @param file
     * @param title
     * @param artist
     * @param album
     * @param num
     * @param comments
     * @param scores
     * @param tags
     */
    public Music(Long id, String fileName, String realName, Boolean fileMissing, Long ownerPeerId, Integer hash, Categories categories, File file, String title, String artist, String album, Integer num, List<Comment> comments, Set<Score> scores, Set<String> tags) {
        this.id = id;
        this.fileName = fileName;
        this.realName = realName;
        this.fileMissing = fileMissing;
        this.ownerPeerId = ownerPeerId;
        this.hash = hash;
        this.categories = categories;
        this.file = file;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.num = num;
        this.comments = comments;
        this.scores = scores;
        this.tags = tags;
    }

    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        String oldFileName = this.fileName;
        this.fileName = fileName;
        propertyChangeSupport.firePropertyChange(Property.FILENAME.name(), oldFileName, fileName);
    }

    /**
     *
     * @return
     */
    public String getRealName() {
        return realName;
    }

    /**
     *
     * @param realName
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     *
     * @return
     */
    public File getFile() {
        return file;
    }

    /**
     *
     * @param file
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     *
     * @return
     */
    public Boolean getFileMissing() {
        return fileMissing;
    }

    /**
     *
     * @param fileMissing
     */
    public void setFileMissing(Boolean fileMissing) {
        this.fileMissing = fileMissing;
        propertyChangeSupport.firePropertyChange(Property.FILE_MISSING.name(), !fileMissing.booleanValue(), fileMissing.booleanValue());
    }

    /**
     *
     * @return
     */
    public Long getOwnerPeerId() {
        return ownerPeerId;
    }

    /**
     *
     * @param ownerPeerId
     */
    public void setOwnerPeerId(Long ownerPeerId) {
        this.ownerPeerId = ownerPeerId;
    }

    /**
     *
     * @return
     */
    public Integer getHash() {
        return hash;
    }

    /**
     *
     * @param hash
     */
    public void setHash(Integer hash) {
        this.hash = hash;
    }

    /**
     *
     * @return
     */
    public Categories getCategories() {
        return categories;
    }

    /**
     *
     * @param categories
     */
    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        String oldTitle = this.title;
        this.title = title;
        propertyChangeSupport.firePropertyChange(Property.TITLE.name(), oldTitle, title);
    }

    /**
     *
     * @return
     */
    public String getArtist() {
        return artist;
    }

    /**
     *
     * @param artist
     */
    public void setArtist(String artist) {
        String oldArtist = this.artist;
        this.artist = artist;
        propertyChangeSupport.firePropertyChange(Property.ARTIST.name(), oldArtist, artist);
    }

    /**
     *
     * @return
     */
    public String getAlbum() {
        return album;
    }

    /**
     *
     * @param album
     */
    public void setAlbum(String album) {
        String oldAlbum = this.album;
        this.album = album;
        propertyChangeSupport.firePropertyChange(Property.ALBUM.name(), oldAlbum, album);
    }

    /**
     *
     * @return
     */
    public Integer getNum() {
        return num;
    }

    /**
     *
     * @param num
     */
    public void setNum(Integer num) {
        Integer oldNum = this.num;
        this.num = num;
        propertyChangeSupport.firePropertyChange(Property.NUM.name(), oldNum, num);
    }

    /**
     *
     * @return
     */
    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    /**
     *
     * @param comments
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     *
     * @return
     */
    public Set<Score> getScores() {
        return Collections.unmodifiableSet(scores);
    }

    /**
     *
     * @param scores
     */
    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    /**
     *
     * @return
     */
    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     *
     * @param tags
     */
    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     */
    public Boolean getMayReadInfo() {
        return mayReadInfo;
    }

    /**
     *
     * @param mayReadInfo
     */
    public void setMayReadInfo(Boolean mayReadInfo) {
        this.mayReadInfo = mayReadInfo;
    }

    /**
     *
     * @return
     */
    public Boolean getMayListen() {
        return mayListen;
    }

    /**
     *
     * @param mayListen
     */
    public void setMayListen(Boolean mayListen) {
        this.mayListen = mayListen;
    }

    /**
     *
     * @return
     */
    public Boolean getMayCommentAndNote() {
        return mayCommentAndNote;
    }

    /**
     *
     * @param mayCommentAndNote
     */
    public void setMayCommentAndNote(Boolean mayCommentAndNote) {
        this.mayCommentAndNote = mayCommentAndNote;
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
     * @return
     */
    public int getMusicHash() {
        return file != null ? file.hashCode() : 0;
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
        NUM,
        /**
         *
         */
        FILE_MISSING
    }
}

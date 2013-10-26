package fr.utc.lo23.sharutc.model.domain;

import java.io.Serializable;

/**
 *
 */
public class Rights implements Serializable {

    private static final long serialVersionUID = -5429412462872354915L;
    private Integer categoryId;
    private Long musicId;
    private Boolean mayReadInfo;
    private Boolean mayListen;
    private Boolean mayNoteAndComment;

    /**
     *
     */
    public Rights() {
    }

    /**
     *
     * @param categoryId
     * @param musicId
     * @param mayReadInfo
     * @param mayListen
     * @param mayNoteAndComment
     */
    public Rights(Integer categoryId, Long musicId, Boolean mayReadInfo, Boolean mayListen, Boolean mayNoteAndComment) {
        this.categoryId = categoryId;
        this.musicId = musicId;
        this.mayReadInfo = mayReadInfo;
        this.mayListen = mayListen;
        this.mayNoteAndComment = mayNoteAndComment;
    }

    /**
     *
     * @return
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     *
     * @param categoryId
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     *
     * @return
     */
    public Long getMusicId() {
        return musicId;
    }

    /**
     *
     * @param musicId
     */
    public void setMusicId(Long musicId) {
        this.musicId = musicId;
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
    public Boolean getMayNoteAndComment() {
        return mayNoteAndComment;
    }

    /**
     *
     * @param mayNoteAndComment
     */
    public void setMayNoteAndComment(Boolean mayNoteAndComment) {
        this.mayNoteAndComment = mayNoteAndComment;
    }
}

package fr.utc.lo23.sharutc.model.domain;

import java.io.Serializable;

/**
 * Represents the 3 rights a user may have on a music. readInfo, listen,
 * noteAndComment. A Rights instance concerns only one category and one music
 */
public class Rights implements Serializable {

    private static final long serialVersionUID = -5429412462872354915L;
    public static final boolean DEFAULT_MAY_READ_INFO = true;
    public static final boolean DEFAULT_LISTEN = true;
    public static final boolean DEFAULT_MAY_NOTE_AND_COMMENT = true;
    private Integer mCategoryId;
    private Long mMusicId;
    private Boolean mMayReadInfo;
    private Boolean mMayListen;
    private Boolean mMayNoteAndComment;

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
        this.mCategoryId = categoryId;
        this.mMusicId = musicId;
        this.mMayReadInfo = mayReadInfo;
        this.mMayListen = mayListen;
        this.mMayNoteAndComment = mayNoteAndComment;
    }

    /**
     *
     * @return
     */
    public Integer getCategoryId() {
        return mCategoryId;
    }

    /**
     *
     * @param categoryId
     */
    public void setCategoryId(Integer categoryId) {
        this.mCategoryId = categoryId;
    }

    /**
     *
     * @return
     */
    public Long getMusicId() {
        return mMusicId;
    }

    /**
     *
     * @param musicId
     */
    public void setMusicId(Long musicId) {
        this.mMusicId = musicId;
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
    public Boolean getMayNoteAndComment() {
        return mMayNoteAndComment;
    }

    /**
     *
     * @param mayNoteAndComment
     */
    public void setMayNoteAndComment(Boolean mayNoteAndComment) {
        this.mMayNoteAndComment = mayNoteAndComment;
    }

    void copyRightsValues(Rights rights) {
        if (rights != null) {
            mMayReadInfo = rights.getMayReadInfo();
            mMayListen = rights.getMayListen();
            mMayNoteAndComment = rights.getMayNoteAndComment();
        }
    }
}

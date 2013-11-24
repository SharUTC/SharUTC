package fr.utc.lo23.sharutc.controler.command.profile;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.Rights;
import fr.utc.lo23.sharutc.model.userdata.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class ManageRightsCommandImpl implements ManageRightsCommand {

    private static final Logger log = LoggerFactory.getLogger(ManageRightsCommandImpl.class);
    private Category mCategory;
    private Music mMusic;
    private Boolean mMayReadInfo;
    private Boolean mMayListen;
    private Boolean mMayCommentAndScore;
    private final AppModel appModel;

    @Inject
    public ManageRightsCommandImpl(AppModel appModel) {
        this.appModel = appModel;
    }

    @Override
    public Category getCategory() {
        return mCategory;
    }

    @Override
    public void setCategory(Category category) {
        this.mCategory = category;
    }

    @Override
    public Music getMusic() {
        return mMusic;
    }

    @Override
    public void setMusic(Music music) {
        this.mMusic = music;
    }

    @Override
    public boolean getMayReadInfo() {
        return mMayReadInfo;
    }

    @Override
    public void setMayReadInfo(boolean mayReadInfo) {
        this.mMayReadInfo = mayReadInfo;
    }

    @Override
    public boolean getMayListen() {
        return mMayListen;
    }

    @Override
    public void setMayListen(boolean mayListen) {
        this.mMayListen = mayListen;
    }

    @Override
    public boolean getMayCommentAndScore() {
        return mMayCommentAndScore;
    }

    @Override
    public void setMayCommentAndScore(boolean mayCommentAndScore) {
        this.mMayCommentAndScore = mayCommentAndScore;
    }

    @Override
    public void execute() {
        log.info("ManageRightsCommand ...");
        
        appModel.getRightsList().setRights(new Rights(mCategory.getId(), mMusic.getId(), mMayReadInfo, mMayListen, mMayCommentAndScore));
        
        log.info("ManageRightsCommand DONE");
    }
}

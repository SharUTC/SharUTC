package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class RemoveMusicFromCategoryCommandImpl implements RemoveMusicFromCategoryCommand {

    private static final Logger log = LoggerFactory
            .getLogger(RemoveMusicFromCategoryCommandImpl.class);
    private Category mCategory;
    private Music mMusic;
    final private MusicService musicService;

    /**
     * {@inheritDoc}
     */
    @Inject
    public RemoveMusicFromCategoryCommandImpl(MusicService mMusicService) {
        this.musicService = mMusicService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category getCategory() {
        return mCategory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCategory(Category category) {
        this.mCategory = category;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Music getMusic() {
        return mMusic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMusic(Music music) {
        this.mMusic = music;
    }

    /**
     * {@inheritDoc}
     */
    public void execute() {
        log.info("RemoveMusicFromCategoryCommand...");
        musicService.removeMusicFromCategory(mMusic, mCategory);
        musicService.saveUserMusicFile();
        musicService.saveUserRightsListFile();
        log.info("RemoveMusicFromCategoryCommand DONE");
    }
}

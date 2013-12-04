package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Category;

/**
 * {@inheritDoc}
 */
public class AddMusicToCategoryCommandImpl implements AddMusicToCategoryCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddMusicToCategoryCommandImpl.class);
    private Category mCategory;
    private Music mMusic;
    private final MusicService musicService;

    /**
     * {@inheritDoc}
     */
    @Inject
    public AddMusicToCategoryCommandImpl(MusicService musicService) {
        this.musicService = musicService;
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
    @Override
    public void execute() {
        log.info("AddMusicToCategoryCommand...");
        musicService.addMusicToCategory(mMusic, mCategory);
        musicService.saveUserMusicFile();
        musicService.saveUserRightsListFile();
        log.info("AddMusicToCategoryCommand DONE");
    }
}

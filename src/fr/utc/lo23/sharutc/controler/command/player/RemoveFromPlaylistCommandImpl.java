package fr.utc.lo23.sharutc.controler.command.player;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.PlayerService;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class RemoveFromPlaylistCommandImpl implements RemoveFromPlaylistCommand {

    private static final Logger log = LoggerFactory.getLogger(RemoveFromPlaylistCommandImpl.class);
    private List<Music> mPlaylist;
    private final PlayerService playerService;

    @Inject
    public RemoveFromPlaylistCommandImpl(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Music> getMusics() {
        return mPlaylist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMusics(List<Music> musics) {
        this.mPlaylist = musics;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMusic(Music music) {
        if (mPlaylist == null) {
            mPlaylist = new ArrayList<Music>();
        }
        mPlaylist.add(music);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("RemoveFromPlaylistCommand...");

        for (Music m : mPlaylist) {
            playerService.removeFromPlaylist(m);
        }

        log.info("RemoveFromPlaylistCommand DONE");
    }
}

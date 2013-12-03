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

    private static final Logger log = LoggerFactory
            .getLogger(RemoveFromPlaylistCommandImpl.class);
    private final PlayerService playerService;
    private List<Integer> mPlaylist;

    @Inject
    public RemoveFromPlaylistCommandImpl(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getMusicsIndex(){
        return mPlaylist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMusicsIndex(List<Integer> musicsIndex){
        mPlaylist = musicsIndex;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setMusicIndex(Integer musicIndex){
        if(mPlaylist == null){
            mPlaylist = new ArrayList<Integer>();
        }
        mPlaylist.add(musicIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("RemoveFromPlaylistCommand...");
        
        System.out.println(mPlaylist);
        
        if(mPlaylist != null){
            for (Integer index : mPlaylist) {
                playerService.removeFromPlaylist(index);
            }
        }

        log.info("RemoveFromPlaylistCommand DONE");
    }

}
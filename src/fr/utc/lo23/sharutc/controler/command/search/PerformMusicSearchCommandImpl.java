package fr.utc.lo23.sharutc.controler.command.search;

import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.userdata.Peer;


/**
 *
 * @author Amandine
 */
public class PerformMusicSearchCommandImpl implements PerformMusicSearchCommand{
    private Peer mPeer;
    private SearchCriteria mSearchCriteria;
    private final MusicService mMusicService;
    private final AppModel mAppModel;
    private final NetworkService mNetworkService;
    
    
    public PerformMusicSearchCommandImpl(MusicService mMusicService,AppModel mAppModel,NetworkService mNetworkService) {
        this.mAppModel= mAppModel;
        this.mMusicService= mMusicService;
        this.mNetworkService= mNetworkService;
    }

    @Override
    public Peer getPeer() {
       return mPeer;  
    }

    @Override
    public void setPeer(Peer peer) {
        this.mPeer = peer; 
    }

    @Override
    public SearchCriteria getSearchCriteria() {
        return mSearchCriteria; 
    }

    @Override
    public void setSearchCriteria(SearchCriteria searchCriteria) {
        this.mSearchCriteria = searchCriteria; 
    }

    @Override
    public void execute() {
       
       mMusicService.searchMusic(mPeer, mSearchCriteria);
    }
    
   
    }


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.command.search;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class PerformMusicSearchCommandImpl implements PerformMusicSearchCommand {

    private static final Logger log = LoggerFactory.getLogger(PerformMusicSearchCommandImpl.class);
    private Peer mPeer;
    private SearchCriteria mSearchCriteria;
    private final MusicService musicService;
    private final NetworkService networkService;

    @Inject
    public PerformMusicSearchCommandImpl(MusicService musicService, NetworkService networkService) {
        this.musicService = musicService;
        this.networkService = networkService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Peer getPeer() {
        return mPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPeer(Peer peer) {
        this.mPeer = peer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchCriteria getSearchCriteria() {
        return mSearchCriteria;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSearchCriteria(SearchCriteria searchCriteria) {
        this.mSearchCriteria = searchCriteria;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("PerformMusicSearchCommand ...");

        Catalog searchResults = musicService.searchMusic(mPeer, mSearchCriteria);
        if (!searchResults.isEmpty()) {
            networkService.sendMusicSearchResults(mPeer, searchResults);
        }

        log.info("PerformMusicSearchCommand DONE");
    }
}

package fr.utc.lo23.sharutc.controler.command.search;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class MusicSearchCommandImpl implements MusicSearchCommand {

    private static final Logger log = LoggerFactory.getLogger(MusicSearchCommandImpl.class);
    private final AppModel appModel;
    private final NetworkService networkService;
    private final MusicService musicService;
    private SearchCriteria searchCriteria;

    /**
     * {@inheritDoc}
     */
    @Inject
    public MusicSearchCommandImpl(AppModel appModel, NetworkService networkService, MusicService musicService) {
        this.appModel = appModel;
        this.networkService = networkService;
        this.musicService = musicService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSearchCriteria(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("MusicSearchCommand ...");

        if (searchCriteria != null
                && searchCriteria.getSearch() != null
                && !searchCriteria.getSearch().trim().isEmpty()) {
            appModel.getSearchResults().clear();
            appModel.getSearchResults().merge(musicService.searchLocalMusic(searchCriteria));
            networkService.searchRequestBroadcast(searchCriteria);
        }

        log.info("MusicSearchCommand DONE");
    }
}

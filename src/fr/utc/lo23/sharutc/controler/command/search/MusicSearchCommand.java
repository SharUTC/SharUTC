package fr.utc.lo23.sharutc.controler.command.search;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;

/**
 *
 */
public interface MusicSearchCommand extends Command {

    /**
     *
     * @return
     */
    public SearchCriteria getSearchCriteria();

    /**
     *
     * @param searchCriteria
     */
    public void setSearchCriteria(SearchCriteria searchCriteria);
}

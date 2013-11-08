package fr.utc.lo23.sharutc.controler.command.search;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;

/**
 * Send a broadcast request containing the search text if not null or empty
 */
public interface MusicSearchCommand extends Command {

    /**
     * Return the details of the search
     *
     * @return the details of the search
     */
    public SearchCriteria getSearchCriteria();

    /**
     * Set the details of the search
     *
     * @param searchCriteria the details of the search
     */
    public void setSearchCriteria(SearchCriteria searchCriteria);
}

package fr.utc.lo23.sharutc.controler.command.search;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 *
 */
public interface PerformMusicSearchCommand extends Command {

    /**
     *
     * @return
     */
    public Peer getPeer();

    /**
     *
     * @param peer
     */
    public void setPeer(Peer peer);

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

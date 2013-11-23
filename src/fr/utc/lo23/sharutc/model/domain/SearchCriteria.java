package fr.utc.lo23.sharutc.model.domain;

import java.io.Serializable;

/**
 * Simple container for a search, contains the value the user entered
 */
public class SearchCriteria implements Serializable {

    private static final long serialVersionUID = -581620620069244195L;
    private String search;

    /**
     *
     */
    public SearchCriteria() {
    }

    /**
     * Creates a new searchCriteria containing the string used to perform the
     * search
     *
     * @param search the value the user entered
     */
    public SearchCriteria(String search) {
        this.search = search != null ? search.trim() : "";
    }

    /**
     * Return the value the user entered
     *
     * @return the value the user entered
     */
    public String getSearch() {
        return search;
    }

    /**
     * Set the value the user entered
     *
     * @param search the value the user entered
     */
    public void setSearch(String search) {
        this.search = search;
    }
}

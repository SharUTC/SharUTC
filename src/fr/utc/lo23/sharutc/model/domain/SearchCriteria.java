package fr.utc.lo23.sharutc.model.domain;

import java.io.Serializable;

/**
 *
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
     *
     * @param search
     */
    public SearchCriteria(String search) {
        this.search = search;
    }

    /**
     *
     * @return
     */
    public String getSearch() {
        return search;
    }

    /**
     *
     * @param search
     */
    public void setSearch(String search) {
        this.search = search;
    }
}

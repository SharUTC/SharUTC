package fr.utc.lo23.sharutc.model;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.RightsList;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.ActivePeerList;
import fr.utc.lo23.sharutc.model.userdata.KnownPeerList;
import fr.utc.lo23.sharutc.model.userdata.Profile;

/**
 *
 */
public interface AppModel {

    /**
     *
     * @return
     */
    public KnownPeerList getKnownPeerList();

    /**
     *
     * @param knownPeerList
     */
    public void setKnownPeerList(KnownPeerList knownPeerList);

    /**
     *
     * @return
     */
    public ActivePeerList getActivePeerList();

    /**
     *
     * @param activePeerList
     */
    public void setActivePeerList(ActivePeerList activePeerList);

    /**
     *
     * @return
     */
    public Profile getProfile();

    /**
     *
     * @param profile
     */
    public void setProfile(Profile profile);

    /**
     *
     * @return
     */
    public TagMap getNetworkTagMap();

    /**
     *
     * @param networkTagMap
     */
    public void setNetworkTagMap(TagMap networkTagMap);

    /**
     *
     * @return
     */
    public Long getCurrentConversationId();

    /**
     *
     */
    public Long getNextConversationId();

    /**
     *
     * @return
     */
    public ErrorBus getErrorBus();

    /**
     *
     * @param errorBus
     */
    public void setErrorBus(ErrorBus errorBus);

    /**
     *
     * @return
     */
    public Catalog getLocalCatalog();

    /**
     *
     * @param localCatalog
     */
    public void setLocalCatalog(Catalog localCatalog);

    /**
     *
     * @return
     */
    public Catalog getTmpCatalog();

    /**
     *
     * @param tmpCatalog
     */
    public void setTmpCatalog(Catalog tmpCatalog);

    /**
     *
     * @return
     */
    public Catalog getSearchResults();

    /**
     *
     * @param searchResults
     */
    public void setSearchResults(Catalog searchResults);

    /**
     *
     * @return
     */
    public Catalog getRemoteUserCatalog();

    /**
     *
     * @param remoteUserCatalog
     */
    public void setRemoteUserCatalog(Catalog remoteUserCatalog);

    /**
     *
     * @return
     */
    public RightsList getRightsList();

    /**
     *
     * @param rightsList
     */
    public void setRightsList(RightsList rightsList);
}

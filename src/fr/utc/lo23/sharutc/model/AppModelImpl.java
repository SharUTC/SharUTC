package fr.utc.lo23.sharutc.model;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.RightsList;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.ActivePeerList;
import fr.utc.lo23.sharutc.model.userdata.KnownPeerList;
import fr.utc.lo23.sharutc.model.userdata.Profile;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Singleton
public class AppModelImpl implements AppModel, Serializable {

    private static final long serialVersionUID = -8328268947100193264L;
    private static final Logger log = LoggerFactory
            .getLogger(AppModelImpl.class);
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private ErrorBus errorBus = new ErrorBus();
    private ActivePeerList activePeerList = new ActivePeerList();
    private TagMap networkTagMap = new TagMap();
    private Catalog tmpCatalog = new Catalog();
    private Catalog remoteUserCatalog = new Catalog();
    private Catalog searchResults = new Catalog();
    private Long currentConversationId = new Long(0);
    private Profile profile;
    private Catalog localCatalog;
    private KnownPeerList knownPeerList;
    private RightsList rightsList;

    /**
     *
     */
    public AppModelImpl() {
    }

    @Override
    public Long getCurrentConversationId() {
        return currentConversationId;
    }

    @Override
    public void incrementConversationId() {
        currentConversationId++;
    }

    @Override
    public KnownPeerList getKnownPeerList() {
        return knownPeerList;
    }

    @Override
    public void setKnownPeerList(KnownPeerList knownPeerList) {
        if (knownPeerList != null) {
            log.trace("Setting KNOWN peers list (contains {} peer{})", knownPeerList.size(), knownPeerList.size() > 1 ? "s" : "");
        } else {
            log.trace("Setting KNOWN peers list (null)");
        }
        KnownPeerList oldKnownPeerList = this.knownPeerList;
        this.knownPeerList = knownPeerList;
        propertyChangeSupport.firePropertyChange(
                Property.KNOWN_PEERS.name(), oldKnownPeerList,
                knownPeerList);
    }

    @Override
    public ActivePeerList getActivePeerList() {
        return activePeerList;
    }

    @Override
    public void setActivePeerList(ActivePeerList activePeerList) {
        if (activePeerList != null) {
            log.trace("Setting ACTIVE peers list (contains {} peer{})", activePeerList.size(), activePeerList.size() > 1 ? "s" : "");
        } else {
            log.trace("Setting ACTIVE peers list (null)");
        }
        ActivePeerList oldActivePeerList = this.activePeerList;
        this.activePeerList = activePeerList;
        propertyChangeSupport.firePropertyChange(
                Property.ACTIVE_PEERS.name(), oldActivePeerList,
                activePeerList);
    }

    @Override
    public Profile getProfile() {
        return profile;
    }

    @Override
    public void setProfile(Profile profile) {
        log.debug("Setting profile");
        Profile oldLoadedProfile = this.profile;
        this.profile = profile;
        propertyChangeSupport.firePropertyChange(
                Property.PROFILE.name(), oldLoadedProfile,
                profile);
    }

    @Override
    public TagMap getNetworkTagMap() {
        return networkTagMap;
    }

    @Override
    public void setNetworkTagMap(TagMap networkTagMap) {
        log.debug("Setting networkTagMap");
        TagMap oldNetworkTagMap = this.networkTagMap;
        this.networkTagMap = networkTagMap;
        propertyChangeSupport.firePropertyChange(
                Property.NETWORK_TAG_MAP.name(), oldNetworkTagMap,
                networkTagMap);
    }

    /**
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     *
     * @return
     */
    @Override
    public ErrorBus getErrorBus() {
        return errorBus;
    }

    /**
     *
     * @param errorBus
     */
    @Override
    public void setErrorBus(ErrorBus errorBus) {
        this.errorBus = errorBus;
    }

    /**
     *
     * @return
     */
    @Override
    public Catalog getLocalCatalog() {
        return localCatalog;
    }

    /**
     *
     * @param localCatalog
     */
    @Override
    public void setLocalCatalog(Catalog localCatalog) {
        this.localCatalog = localCatalog;
    }

    /**
     *
     * @return
     */
    @Override
    public Catalog getTmpCatalog() {
        return tmpCatalog;
    }

    /**
     *
     * @param tmpCatalog
     */
    @Override
    public void setTmpCatalog(Catalog tmpCatalog) {
        this.tmpCatalog = tmpCatalog;
    }

    /**
     *
     * @return
     */
    @Override
    public Catalog getSearchResults() {
        return searchResults;
    }

    /**
     *
     * @param searchResults
     */
    @Override
    public void setSearchResults(Catalog searchResults) {
        this.searchResults = searchResults;
    }

    /**
     *
     * @return
     */
    @Override
    public Catalog getRemoteUserCatalog() {
        return remoteUserCatalog;
    }

    /**
     *
     * @param remoteUserCatalog
     */
    @Override
    public void setRemoteUserCatalog(Catalog remoteUserCatalog) {
        this.remoteUserCatalog = remoteUserCatalog;
    }

    @Override
    public RightsList getRightsList() {
        return rightsList;
    }

    @Override
    public void setRightsList(RightsList rightsList) {
        this.rightsList = rightsList;
    }

    /**
     *
     */
    public enum Property {

        /**
         *
         */
        ACTIVE_PEERS,
        /**
         *
         */
        KNOWN_PEERS,
        /**
         *
         */
        PROFILE,
        /**
         *
         */
        NETWORK_TAG_MAP
    }
}

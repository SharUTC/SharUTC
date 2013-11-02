package fr.utc.lo23.sharutc.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.RightsList;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.ActivePeerList;
import fr.utc.lo23.sharutc.model.userdata.KnownPeerList;
import fr.utc.lo23.sharutc.model.userdata.Profile;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
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
    
    private static final String dataPath = "";

    /**
     * Empty constructor, no real use in application, created by injection
     */
    public AppModelImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getCurrentConversationId() {
        return currentConversationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getNextConversationId() {
        return ++currentConversationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KnownPeerList getKnownPeerList() {
        return knownPeerList;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivePeerList getActivePeerList() {
        return activePeerList;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Profile getProfile() {
        return profile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProfile(Profile profile) {
        log.debug("Setting profile");
        Profile oldLoadedProfile = this.profile;
        this.profile = profile;
        propertyChangeSupport.firePropertyChange(
                Property.PROFILE.name(), oldLoadedProfile,
                profile);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProfile(){
        if(profile != null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.writeValue(new File(dataPath + "\\profile.json"), profile);
            } catch (IOException ex) {
                log.error(ex.toString());
            }
        }
        else
            log.warn("Can't save current profile(null)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagMap getNetworkTagMap() {
        return networkTagMap;
    }

    /**
     * {@inheritDoc}
     */
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
     * {@inheritDoc}
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ErrorBus getErrorBus() {
        return errorBus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setErrorBus(ErrorBus errorBus) {
        this.errorBus = errorBus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getLocalCatalog() {
        return localCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocalCatalog(Catalog localCatalog) {
        this.localCatalog = localCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getTmpCatalog() {
        return tmpCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTmpCatalog(Catalog tmpCatalog) {
        this.tmpCatalog = tmpCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getSearchResults() {
        return searchResults;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSearchResults(Catalog searchResults) {
        this.searchResults = searchResults;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getRemoteUserCatalog() {
        return remoteUserCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRemoteUserCatalog(Catalog remoteUserCatalog) {
        this.remoteUserCatalog = remoteUserCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RightsList getRightsList() {
        return rightsList;
    }

    /**
     * {@inheritDoc}
     */
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

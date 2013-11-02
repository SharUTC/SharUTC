package fr.utc.lo23.sharutc.model;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.RightsList;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.ActivePeerList;
import fr.utc.lo23.sharutc.model.userdata.KnownPeerList;
import fr.utc.lo23.sharutc.model.userdata.Profile;

/**
 * The Root object of the Model part in the MVC schema. Instance is Observable
 * via a PropertyChangeListener to detect user connection at least.
 */
public interface AppModel {

    /**
     * Return the object containing the list of known peers
     *
     * @return the object containing the list of known peers
     */
    public KnownPeerList getKnownPeerList();

    /**
     * Set the object containing the list of known peers
     *
     * @param knownPeerList the object containing the list of known peers
     */
    public void setKnownPeerList(KnownPeerList knownPeerList);

    /**
     * Return the object containing the list of active peers
     *
     * @return the object containing the list of active peers
     */
    public ActivePeerList getActivePeerList();

    /**
     * Set the object containing the list of active peers
     *
     * @param activePeerList the object containing the list of active peers
     */
    public void setActivePeerList(ActivePeerList activePeerList);

    /**
     * Return the currently connected user profile
     *
     * @return the currently connected user profile, or null if no one is
     * connected
     */
    public Profile getProfile();

    /**
     * Set the currently connected user profile, updates listeners to inform the
     * view that a user successfully connected to his or her account
     *
     * @param profile the currently connected user profile
     */
    public void setProfile(Profile profile);
    
    /**
     * Save the currently connected user profile, by writing the java data into
     * a JSON file.
     */
    public void saveProfile();

    /**
     * Return the current image of the network tagmap, connected to view via
     * listeners
     *
     * @return the current image of the network tagmap, connected to view via
     * listeners
     */
    public TagMap getNetworkTagMap();

    /**
     * Set the current TagMap instance, this musn't be used by developpers in
     * the application, since view has listeners setted on the only instance
     * needed
     *
     * @param networkTagMap the new TagMap that replace the actual
     */
    public void setNetworkTagMap(TagMap networkTagMap);

    /**
     * Return the current conversation id, this value is not stored with the
     * profile, its only purpose is to filter incoming message when the are too
     * old
     *
     * @return the current conversation id
     */
    public Long getCurrentConversationId();

    /**
     * Increment and return the conversation id
     *
     * @return the current conversation id after incrementation
     */
    public Long getNextConversationId();

    /**
     * Return the error bus to inform view about user errors
     *
     * @return the error Bus
     */
    public ErrorBus getErrorBus();

    /**
     * Set the ErrorBus used by View, musn't be used by developpers, injected
     * when the application starts
     *
     * @param errorBus the error Bus
     */
    public void setErrorBus(ErrorBus errorBus);

    /**
     * Return the catalog of local musics owned by the current user
     *
     * @return the catalog of local musics owned by the current user
     */
    public Catalog getLocalCatalog();

    /**
     * Set the catalog of local musics owned by the current user, shouldn't be
     * used by developpers
     *
     * @param localCatalog the catalog of local musics owned by the current user
     */
    public void setLocalCatalog(Catalog localCatalog);

    /**
     * Return the catalog of temporary musics downloaded for listening
     *
     * @return the catalog of temporary musics downloaded for listening
     */
    public Catalog getTmpCatalog();

    /**
     * Set the catalog of temporary musics downloaded for listening, shouldn't
     * be used by developpers
     *
     * @param tmpCatalog the catalog of temporary musics downloaded for
     * listening
     */
    public void setTmpCatalog(Catalog tmpCatalog);

    /**
     * Return the catalog containing the actual results returned by peers, the
     * catalog is cleared at each new serach
     *
     * @return the catalog containing the actual results returned by peers
     */
    public Catalog getSearchResults();

    /**
     * Set the catalog containing the actual results returned by peers,
     * shouldn't be used by developpers
     *
     * @param searchResults the catalog containing the actual results returned
     * by peers
     */
    public void setSearchResults(Catalog searchResults);

    /**
     * Return the catalog containing a copy of the catalog of a remote peer
     * (modulo the rights attached to each music)
     *
     * @return the catalog containing a copy of the catalog of a remote peer
     */
    public Catalog getRemoteUserCatalog();

    /**
     * Set the catalog containing a copy of the catalog of a remote peer (modulo
     * the rights attached to each music)
     *
     * @param remoteUserCatalog the catalog containing a copy of the catalog of
     * a remote peer
     */
    public void setRemoteUserCatalog(Catalog remoteUserCatalog);

    /**
     * Return the RightsList containing all the Rights instances for the current
     * user
     *
     * @return the RightsList containing all the Rights instances for the
     * current user
     */
    public RightsList getRightsList();

    /**
     * Set the RightsList containing all the Rights instances for the current
     * user
     *
     * @param rightsList the RightsList containing all the Rights instances for
     * the current user
     */
    public void setRightsList(RightsList rightsList);
}

package fr.utc.lo23.sharutc.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionChangeSupport;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the list of the currently connected peers
 * 
 * Don't use index from CollectionChangeSupport (HashMap inside)
 */
public class ActivePeerList implements Serializable {

    private static final long serialVersionUID = 7777378837435596771L;
    @JsonIgnore
    private CollectionChangeSupport mCollectionChangeSupport = new CollectionChangeSupport(this);
    private HashMap<Peer, Date> mActivePeers = new HashMap<Peer, Date>();

    /**
     * Default constructor
     */
    public ActivePeerList() {
    }

    /**
     * Return the list of connected peers
     * 
     * @return the list of connected peers
     */
    public HashMap<Peer, Date> getActivePeers() {
        return mActivePeers;
    }

    /**
     * Set the list of connected peers
     * 
     * @param activePeers - list of connected peers
     */
    public void setActivePeers(HashMap<Peer, Date> activePeers) {
        this.mActivePeers = activePeers;
    }

    /**
     * Update the active peer list with a new peer, given in parameter
     * 
     * @param peer
     */
    public void update(Peer peer) {
        boolean update = mActivePeers.put(peer, new Date()) != null;
        if (update) {
            mCollectionChangeSupport.fireCollectionChanged(peer, CollectionEvent.Type.ADD);
        }
    }

    /**
     * Remove a peer from the active peer list
     * 
     * @param peer
     */
    public void remove(Peer peer) {
        if (mActivePeers.containsKey(peer)) {
            mActivePeers.remove(peer);
            mCollectionChangeSupport.fireCollectionChanged(peer, CollectionEvent.Type.REMOVE);
        }
    }

    /**
     * Empty the active peer list
     * 
     */
    public void clear() {
        if (!mActivePeers.isEmpty()) {
            mActivePeers.clear();
            mCollectionChangeSupport.fireCollectionChanged(null, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     * Return the size of the active peer list
     * 
     * @return the size of the active peer list
     */
    public int size() {
        return mActivePeers.size();
    }

    /**
     * Check if the active peer list contains the peer given in parameter
     * 
     * @param peer
     * @return a boolean
     */
    public boolean contains(Peer peer) {
        return mActivePeers.containsKey(peer);
    }

    /**
     * Check if the active peer list is empty
     * 
     * @return a boolean
     */
    public boolean isEmpty() {
        return mActivePeers.isEmpty();
    }

    /**
     * 
     * @param listener
     */
    public void addPropertyChangeListener(CollectionChangeListener listener) {
        mCollectionChangeSupport.addCollectionListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removePropertyChangeListener(CollectionChangeListener listener) {
        mCollectionChangeSupport.removeCollectionListener(listener);
    }

    /**
     * Return the peer thanks to its id given in parameter if it exists
     * 
     * @param peerId
     * @return a peer or null if it does not exist
     */
    public Peer getByPeerId(Long peerId) {
        Peer peer = null;
        if (peerId != null) {
            for (Map.Entry<Peer, Date> activePeer : mActivePeers.entrySet()) {
                if (activePeer.getKey().getId() == peerId.longValue()) {
                    peer = activePeer.getKey();
                }
            }
        }
        return peer;
    }
}

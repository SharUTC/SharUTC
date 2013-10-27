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
 *
 */
public class ActivePeerList implements Serializable {

    private static final long serialVersionUID = 7777378837435596771L;
    @JsonIgnore
    private CollectionChangeSupport collectionChangeSupport = new CollectionChangeSupport(this);
    private HashMap<Peer, Date> activePeers = new HashMap<Peer, Date>();

    /**
     *
     */
    public ActivePeerList() {
    }

    /**
     *
     * @return
     */
    public HashMap<Peer, Date> getActivePeers() {
        return activePeers;
    }

    /**
     *
     * @param activePeers
     */
    public void setActivePeers(HashMap<Peer, Date> activePeers) {
        this.activePeers = activePeers;
    }

    /**
     *
     * @param peer
     */
    public void update(Peer peer) {
        activePeers.put(peer, new Date());
        collectionChangeSupport.fireCollectionChanged(peer, -1, CollectionEvent.Type.ADD);
    }

    /**
     *
     * @param peer
     */
    public void remove(Peer peer) {
        if (activePeers.containsKey(peer)) {
            activePeers.remove(peer);
            collectionChangeSupport.fireCollectionChanged(peer, -1, CollectionEvent.Type.REMOVE);
        }
    }

    /**
     *
     */
    public void clear() {
        if (!activePeers.isEmpty()) {
            activePeers.clear();
            collectionChangeSupport.fireCollectionChanged(null, -1, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     *
     * @return
     */
    public int size() {
        return activePeers.size();
    }

    /**
     *
     * @param peer
     * @return
     */
    public boolean contains(Peer peer) {
        return activePeers.containsKey(peer);
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return activePeers.isEmpty();
    }

    /**
     *
     * @param listener
     */
    public void addPropertyChangeListener(CollectionChangeListener listener) {
        collectionChangeSupport.addCollectionListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removePropertyChangeListener(CollectionChangeListener listener) {
        collectionChangeSupport.removeCollectionListener(listener);
    }

    /**
     *
     * @param peerId
     * @return
     */
    public Peer getByPeerId(Long peerId) {
        if (peerId != null) {
            for (Map.Entry<Peer, Date> activePeer : activePeers.entrySet()) {
                if (activePeer.getKey().getId() == peerId.longValue()) {
                    return activePeer.getKey();
                }
            }
        }
        return null;
    }
}
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

    private static final long sSerialVersionUID = 7777378837435596771L;
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
        return mActivePeers;
    }

    /**
     *
     * @param activePeers
     */
    public void setActivePeers(HashMap<Peer, Date> activePeers) {
        this.mActivePeers = activePeers;
    }

    /**
     *
     * @param peer
     */
    public void update(Peer peer) {
        mActivePeers.put(peer, new Date());
        mCollectionChangeSupport.fireCollectionChanged(peer, -1, CollectionEvent.Type.ADD);
    }

    /**
     *
     * @param peer
     */
    public void remove(Peer peer) {
        if (mActivePeers.containsKey(peer)) {
            mActivePeers.remove(peer);
            mCollectionChangeSupport.fireCollectionChanged(peer, -1, CollectionEvent.Type.REMOVE);
        }
    }

    /**
     *
     */
    public void clear() {
        if (!mActivePeers.isEmpty()) {
            mActivePeers.clear();
            mCollectionChangeSupport.fireCollectionChanged(null, -1, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     *
     * @return
     */
    public int size() {
        return mActivePeers.size();
    }

    /**
     *
     * @param peer
     * @return
     */
    public boolean contains(Peer peer) {
        return mActivePeers.containsKey(peer);
    }

    /**
     *
     * @return
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
     *
     * @param peerId
     * @return
     */
    public Peer getByPeerId(Long peerId) {
        if (peerId != null) {
            for (Map.Entry<Peer, Date> activePeer : mActivePeers.entrySet()) {
                if (activePeer.getKey().getId() == peerId.longValue()) {
                    return activePeer.getKey();
                }
            }
        }
        return null;
    }
}

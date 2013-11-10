package fr.utc.lo23.sharutc.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionChangeSupport;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 */
public class Contacts implements Serializable {

    private static final long serialVersionUID = -8656809069835780866L;
    private HashSet<Peer> mPeers;
    @JsonIgnore
    private CollectionChangeSupport mCollectionChangeSupport = new CollectionChangeSupport(this);

    /**
     *
     */
    public Contacts() {
    }


    /**
     * Return the Peer who has the Id given in parameter if exists
     *
     * @param peerID the Peer id of a Long
     * @return the Peer who has the Id given in parameter, null is the peer
 isn't a Long
     */
    public Peer findById(Long peerID) {
        for (Peer p : mPeers) {
            if (p.getId() == peerID) {
                return p;
            }
        }
        return null;
    }

    /**
     *
     * @param contact
     * @return
     */
    public boolean add(Peer peer) {
        boolean added = mPeers.add(peer);
        if (added) {
            mCollectionChangeSupport.fireCollectionChanged(peer, mPeers.size() - 1, CollectionEvent.Type.ADD);
        }
        return added;
    }

    /**
     *
     * @param peers
     */
    public void addAll(Collection<Peer> peers) {
        if (peers != null && !peers.isEmpty()) {
            for (Peer p : peers) {
                this.add(p);
            }
        }
    }

    /**
     *
     * @param contact
     * @return
     */
    public boolean remove(Peer peer) {
        boolean removed = mPeers.remove(peer);
        if (removed) {
            mCollectionChangeSupport.fireCollectionChanged(peer, -1, CollectionEvent.Type.REMOVE);
        }
        return removed;
    }

    /**
     *
     */
    public void clear() {
        if (!mPeers.isEmpty()) {
            mPeers.clear();
            mCollectionChangeSupport.fireCollectionChanged(null, -1, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     *
     * @return
     */
    public int size() {
        return mPeers.size();
    }

    /**
     *
     * @param peer
     * @return
     */
    public boolean contains(Peer peer) {
        return mPeers.contains(peer);
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return mPeers.isEmpty();
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
}

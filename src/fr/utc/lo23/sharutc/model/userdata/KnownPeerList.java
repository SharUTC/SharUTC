package fr.utc.lo23.sharutc.model.userdata;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 */
public class KnownPeerList implements Serializable {

    private static final long serialVersionUID = -8430180174515199083L;
    private HashMap<Long, String> knownPeers;

    /**
     *
     */
    public KnownPeerList() {
    }

    /**
     *
     * @return
     */
    public HashMap<Long, String> getKnownPeers() {
        return knownPeers;
    }

    /**
     *
     * @param knownPeers
     */
    public void setKnownPeers(HashMap<Long, String> knownPeers) {
        this.knownPeers = knownPeers;
    }

    /**
     *
     * @param id
     * @return
     */
    public String getPeerNameById(Long id) {
        return knownPeers.get(id);
    }

    /**
     *
     * @param peer
     */
    public void update(Peer peer) {
        knownPeers.put(peer.getId(), peer.getDisplayName());
    }

    /**
     *
     * @return
     */
    public int size() {
        return knownPeers.size();
    }

    /**
     *
     * @param peer
     * @return
     */
    public boolean contains(Peer peer) {
        return knownPeers.containsKey(peer.getId());
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return knownPeers.isEmpty();
    }
}

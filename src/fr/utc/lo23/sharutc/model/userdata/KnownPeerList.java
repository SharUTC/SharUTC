package fr.utc.lo23.sharutc.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 */
public class KnownPeerList implements Serializable {

    private static final long serialVersionUID = -8430180174515199083L;
    private HashMap<Long, String> mKnownPeers = new HashMap<Long, String>();

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
        return mKnownPeers;
    }

    /**
     *
     * @param knownPeers
     */
    public void setKnownPeers(HashMap<Long, String> knownPeers) {
        this.mKnownPeers = knownPeers;
    }

    /**
     *
     * @param id
     * @return
     */
    public String getPeerNameById(Long id) {
        return mKnownPeers.get(id);
    }

    /**
     *
     * @param peer
     */
    public void update(Peer peer) {
        mKnownPeers.put(peer.getId(), peer.getDisplayName());
    }

    /**
     *
     * @return
     */
    public int size() {
        return mKnownPeers.size();
    }

    /**
     *
     * @param peer
     * @return
     */
    public boolean contains(Peer peer) {
        return mKnownPeers.containsKey(peer.getId());
    }

    /**
     *
     * @return
     */
    @JsonIgnore
    public boolean isEmpty() {
        return mKnownPeers.isEmpty();
    }
}

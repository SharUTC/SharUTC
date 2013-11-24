package fr.utc.lo23.sharutc.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Represents the known peer list dedicated to have the name of comment authors
 */
public class KnownPeerList implements Serializable {

    private static final long serialVersionUID = -8430180174515199083L;
    private HashMap<Long, String> mKnownPeers = new HashMap<Long, String>();

    /**
     * Default constructor
     */
    public KnownPeerList() {
    }

    /**
     * Return the known peer list
     * 
     * @return the known peer list
     */
    public HashMap<Long, String> getKnownPeers() {
        return mKnownPeers;
    }

    /**
     * Set the known peer list
     * 
     * @param knownPeers - the known peer list
     */
    public void setKnownPeers(HashMap<Long, String> knownPeers) {
        this.mKnownPeers = knownPeers;
    }

    /**
     * Return peer's name thanks to its id given in parameter
     * 
     * @param id
     * @return the name
     */
    public String getPeerNameById(Long id) {
        return mKnownPeers.get(id);
    }

    /**
     * Update the known peer list with a new peer, given in parameter
     * 
     * @param peer
     */
    public void update(Peer peer) {
        mKnownPeers.put(peer.getId(), peer.getDisplayName());
    }

    /**
     * Return the size of the known peer list
     * 
     * @return the size of the known peer list
     */
    public int size() {
        return mKnownPeers.size();
    }

    /**
     * Check if the known peer list contains the peer given in parameter
     * 
     * @param peer
     * @return a boolean
     */
    public boolean contains(Peer peer) {
        return mKnownPeers.containsKey(peer.getId());
    }

    /**
     * Check if the known peer list is empty
     * 
     * @return a boolean
     */
    @JsonIgnore
    public boolean isEmpty() {
        return mKnownPeers.isEmpty();
    }
}

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
    private HashSet<Long> mContactIds;
    @JsonIgnore
    private CollectionChangeSupport mCollectionChangeSupport = new CollectionChangeSupport(this);

    /**
     *
     */
    public Contacts() {
    }

    /**
     *
     * @return
     */
    public HashSet<Long> getContactIds() {
        return mContactIds;
    }

    /**
     *
     * @param contacts
     */
    public void setContactIds(HashSet<Long> contacts) {
        this.mContactIds = contacts;
    }

    /**
     * Return the Peer who has the Id given in parameter if exists
     *
     * @param peerID the Peer id of a Long
     * @return the Peer who has the Id given in parameter, null is the peer
 isn't a Long
     */
    public Long findById(Long peerID) {
        for (Long contactId : mContactIds) {
            if (contactId.equals(peerID)) {
                return contactId;
            }
        }
        return null;
    }

    /**
     *
     * @param contact
     * @return
     */
    public boolean add(Long contact) {
        boolean added = mContactIds.add(contact);
        if (added) {
            mCollectionChangeSupport.fireCollectionChanged(contact, mContactIds.size() - 1, CollectionEvent.Type.ADD);
        }
        return added;
    }

    /**
     *
     * @param contacts
     */
    public void addAll(Collection<Long> contacts) {
        if (contacts != null && !contacts.isEmpty()) {
            for (Long contact : contacts) {
                this.add(contact);
            }
        }
    }

    /**
     *
     * @param contact
     * @return
     */
    public boolean remove(Long contact) {
        boolean removed = mContactIds.remove(contact);
        if (removed) {
            mCollectionChangeSupport.fireCollectionChanged(contact, -1, CollectionEvent.Type.REMOVE);
        }
        return removed;
    }

    /**
     *
     */
    public void clear() {
        if (!mContactIds.isEmpty()) {
            mContactIds.clear();
            mCollectionChangeSupport.fireCollectionChanged(null, -1, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     *
     * @return
     */
    public int size() {
        return mContactIds.size();
    }

    /**
     *
     * @param contact
     * @return
     */
    public boolean contains(Long contact) {
        return mContactIds.contains(contact);
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return mContactIds.isEmpty();
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

package fr.utc.lo23.sharutc.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionChangeSupport;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * Reprents a list of contact
 * Contains all Peers that user has promoted to Contact Status. Don't use index
 * from CollectionChangeSupport (HashSet inside)
 */
public class Contacts implements Serializable {

    private static final long serialVersionUID = -8656809069835780866L;
    private HashSet<Contact> mContacts;
    @JsonIgnore
    private CollectionChangeSupport mCollectionChangeSupport = new CollectionChangeSupport(this);

    /**
     * Constructor
     */
    public Contacts() {
        mContacts = new HashSet<Contact>();
    }

    /**
     * Return the Peer who has the Id given in parameter if exists
     *
     * @param peerID the Peer id of a Long
     * @return the Peer who has the Id given in parameter, null if the peer
     * isn't a Long
     */
    public Contact findById(Long peerID) {
        Contact contact = null;
        for (Contact p : mContacts) {
            if (p.getUserInfo().getPeerId() == peerID) {
                contact = p;
            }
        }
        return contact;
    }

    /**
     * Return the contact list
     * 
     * @return the contact list
     */
    public HashSet<Contact> getContacts() {
        return mContacts;
    }

    /**
     * Add a Contact to Contacts (list). Don't use index from
     * CollectionChangeSupport
     *
     * @param contact
     * @return true if the contact was added, false otherwise
     */
    public boolean add(Contact contact) {
        boolean added = mContacts.add(contact);
        if (added) {
            mCollectionChangeSupport.fireCollectionChanged(contact, CollectionEvent.Type.ADD);
        }
        return added;
    }

    /**
     * Add a contact list
     * 
     * @param contacts - a contact list
     */
    public void addAll(Collection<Contact> contacts) {
        if (contacts != null && !contacts.isEmpty()) {
            for (Contact p : contacts) {
                this.add(p);
            }
        }
    }

    /**
     * Remove a contact from this container. Don't use index from
     * CollectionChangeSupport
     *
     * @param contact
     * @return a boolean
     */
    public boolean remove(Contact contact) {
        boolean removed = mContacts.remove(contact);
        if (removed) {
            mCollectionChangeSupport.fireCollectionChanged(contact, CollectionEvent.Type.REMOVE);
        }
        return removed;
    }

    /**
     * Empty the contact list
     */
    public void clear() {
        if (!mContacts.isEmpty()) {
            mContacts.clear();
            mCollectionChangeSupport.fireCollectionChanged(null, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     * Return the size of the contact list
     * 
     * @return the size of the contact list
     */
    public int size() {
        return mContacts.size();
    }

    /**
     * Check if the contact list contains the contact given in parameter
     * 
     * @param contact
     * @return a boolean
     */
    public boolean contains(Contact contact) {
        return mContacts.contains(contact);
    }

    /**
     * Check if the contact list is empty
     * 
     * @return a booelan
     */
    @JsonIgnore
    public boolean isEmpty() {
        return mContacts.isEmpty();
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

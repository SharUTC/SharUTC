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
    private HashSet<Contact> mContacts;
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
    public HashSet<Contact> getContacts() {
        return mContacts;
    }

    /**
     *
     * @param contacts
     */
    public void setContacts(HashSet<Contact> contacts) {
        this.mContacts = contacts;
    }

    /**
     *
     * @param contact
     * @return
     */
    public boolean add(Contact contact) {
        boolean added = mContacts.add(contact);
        if (added) {
            mCollectionChangeSupport.fireCollectionChanged(contact, mContacts.size() - 1, CollectionEvent.Type.ADD);
        }
        return added;
    }

    /**
     *
     * @param contacts
     */
    public void addAll(Collection<Contact> contacts) {
        if (contacts != null && !contacts.isEmpty()) {
            for (Contact contact : contacts) {
                this.add(contact);
            }
        }
    }

    /**
     *
     * @param contact
     * @return
     */
    public boolean remove(Contact contact) {
        boolean removed = mContacts.remove(contact);
        if (removed) {
            mCollectionChangeSupport.fireCollectionChanged(contact, -1, CollectionEvent.Type.REMOVE);
        }
        return removed;
    }

    /**
     *
     */
    public void clear() {
        if (!mContacts.isEmpty()) {
            mContacts.clear();
            mCollectionChangeSupport.fireCollectionChanged(null, -1, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     *
     * @return
     */
    public int size() {
        return mContacts.size();
    }

    /**
     *
     * @param contact
     * @return
     */
    public boolean contains(Contact contact) {
        return mContacts.contains(contact);
    }

    /**
     *
     * @return
     */
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

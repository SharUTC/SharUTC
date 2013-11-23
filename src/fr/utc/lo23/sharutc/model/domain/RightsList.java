package fr.utc.lo23.sharutc.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionChangeSupport;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains all rights relative to an account, stored with categoryId and
 * musicId to perform searches. Send updates when a Rights instance is addeed,
 * removed, updated, and when the list is cleared through a
 * CollectionChangeListener
 *
 */
public class RightsList implements Serializable {

    private static final long serialVersionUID = 3450643720510945491L;
    private ArrayList<Rights> mRightsList = new ArrayList<Rights>();
    @JsonIgnore
    private CollectionChangeSupport collectionChangeSupport = new CollectionChangeSupport(this);

    /**
     *
     */
    public RightsList() {
    }

    /**
     * Give access to the contained list of rights, but its content is locked
     * and cannot be modified this way
     *
     * @return an unmodifiable List of Rights instances
     */
    public List<Rights> getRightsList() {
        return Collections.unmodifiableList(mRightsList);
    }

    /**
     * Clear all previous Rights from this RightLists and add the new rights
     *
     * @param rightsList the rights to put in this catalog
     */
    public void setRightsList(List<Rights> rightsList) {
        clear();
        addAll(rightsList);
    }

    /**
     * Add a Rights instance to the RightsList, send update (ADD)
     *
     * @param rights the rights to add
     * @return true if the rights was added (java norm, not used here)
     */
    public boolean add(Rights rights) {
        add(mRightsList.size(), rights);
        return true;
    }

    /**
     * Add a Rights instancve to the RightsList at the specified index, send
     * update (ADD)
     *
     * @param index where to add the rights, must be inside [0; size()]
     * @param rights the rights to add
     */
    public void add(int index, Rights rights) {
        mRightsList.add(index, rights);
        collectionChangeSupport.fireCollectionChanged(rights, index, CollectionEvent.Type.ADD);
    }

    /**
     * Replace the Rights instance at the specified index, send update (UPDATE)
     *
     * @param index where to update the Rights instance, must be inside [0;
     * size()[
     * @param rights the rights to add
     * @return the previously element at the given position
     */
    public Rights set(int index, Rights rights) {
        Rights set = mRightsList.set(index, rights);
        collectionChangeSupport.fireCollectionChanged(rights, index, CollectionEvent.Type.UPDATE);
        return set;
    }

    /**
     * Add all rights to this rightsList, send updates (ADD)
     *
     * @param rightsList the rights to add
     */
    public void addAll(List<Rights> rightsList) {
        if (rightsList != null && !rightsList.isEmpty()) {
            for (Rights rights : rightsList) {
                this.add(rights);
            }
        }
    }

    /**
     * Remove a given rights instance from this rightsList if it exists, send
     * updates (REMOVE) with index removed
     *
     * @param rights the rights instance to remove
     * @return true is the rights instance was removed, else false
     */
    public boolean remove(Rights rights) {
        int indexRemoved = mRightsList.indexOf(rights);
        boolean removed = false;
        if (indexRemoved != -1) {
            removed = mRightsList.remove(rights);
            if (removed) {
                collectionChangeSupport.fireCollectionChanged(rights, indexRemoved, CollectionEvent.Type.REMOVE);
            }
        }
        return removed;
    }

    /**
     * Remove all rights from this rightsList, send update (CLEAR)
     */
    public void clear() {
        if (!mRightsList.isEmpty()) {
            mRightsList.clear();
            collectionChangeSupport.fireCollectionChanged(null, -1, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     * The number of Rights contained in this RightsList
     *
     * @return the number of Rights contained in this RightsList
     */
    public int size() {
        return mRightsList.size();
    }

    /**
     * Gives the index of the rights instance in this rightsList
     *
     * @param rights the right instance to find in this rightsList
     * @return index of the rights instance in this catalog, -1 if there's no
     * such instance
     */
    public int indexOf(Rights rights) {
        return mRightsList.indexOf(rights);
    }

    /**
     * Return true if the rights instance exists in this rightsList, else false
     *
     * @param rights the rights instance to find in this rightsList
     * @return true if the rights instance exists in this rightsList, else false
     */
    public boolean contains(Rights rights) {
        return mRightsList.contains(rights);
    }

    /**
     * Return true if the rightsList has no rights instances, else false
     *
     * @return true if the rightsList has no rights instances, else false
     */
    @JsonIgnore
    public boolean isEmpty() {
        return mRightsList.isEmpty();
    }

    /**
     * Add the listener in parameter to the list of listeners that may be
     * notified
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(CollectionChangeListener listener) {
        collectionChangeSupport.addCollectionListener(listener);
    }

    /**
     * Removes the listener in parameter to the list of listeners that may be
     * notified
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(CollectionChangeListener listener) {
        collectionChangeSupport.removeCollectionListener(listener);
    }

    public Rights getByMusicIdAndCategoryId(Long musicId, Integer categoryId) {
        Rights rights = null;
        for (Rights r : mRightsList) {
            if (r != null && r.getMusicId() != null && r.getCategoryId() != null && r.getMusicId().equals(musicId) && r.getCategoryId().equals(categoryId)) {
                r = rights;
            }
        }
        return rights;
    }

    public void setRights(Rights rights) {
        if (rights != null) {
            Rights r = getByMusicIdAndCategoryId(rights.getMusicId(), rights.getCategoryId());
            if (r != null) {
                r.copyRightsValues(rights);
            } else {
                mRightsList.add(rights);
            }
        }
    }
}

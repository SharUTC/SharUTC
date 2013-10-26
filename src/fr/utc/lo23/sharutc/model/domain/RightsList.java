package fr.utc.lo23.sharutc.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionChangeSupport;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RightsList implements Serializable {

    private static final long serialVersionUID = 3450643720510945491L;
    @JsonIgnore
    private CollectionChangeSupport collectionChangeSupport = new CollectionChangeSupport(this);
    private ArrayList<Rights> rightsList = new ArrayList<Rights>();

    /**
     *
     */
    public RightsList() {
    }

    /**
     *
     * @return
     */
    public ArrayList<Rights> getRightsList() {
        return rightsList;
    }

    /**
     *
     * @param rightsList
     */
    public void setRightsList(ArrayList<Rights> rightsList) {
        this.rightsList = rightsList;
    }

    /**
     *
     * @param rights
     * @return
     */
    public boolean add(Rights rights) {
        boolean added = rightsList.add(rights);
        if (added) {
            collectionChangeSupport.fireCollectionChanged(rights, rightsList.size() - 1, CollectionEvent.Type.ADD);
        }
        return added;
    }

    /**
     *
     * @param index
     * @param rights
     */
    public void add(int index, Rights rights) {
        rightsList.add(index, rights);
        collectionChangeSupport.fireCollectionChanged(rights, index, CollectionEvent.Type.ADD);
    }

    /**
     *
     * @param index
     * @param rights
     * @return
     */
    public Rights set(int index, Rights rights) {
        Rights set = rightsList.set(index, rights);
        collectionChangeSupport.fireCollectionChanged(rights, index, CollectionEvent.Type.UPDATE);
        return set;
    }

    /**
     *
     * @param rightsList
     */
    public void addAll(List<Rights> rightsList) {
        if (rightsList != null && !rightsList.isEmpty()) {
            for (Rights rights : rightsList) {
                this.add(rights);
            }
        }
    }

    /**
     *
     * @param rights
     * @return
     */
    public boolean remove(Rights rights) {
        boolean removed = rightsList.remove(rights);
        if (removed) {
            collectionChangeSupport.fireCollectionChanged(rights, -1, CollectionEvent.Type.REMOVE);
        }
        return removed;
    }

    /**
     *
     */
    public void clear() {
        if (!rightsList.isEmpty()) {
            rightsList.clear();
            collectionChangeSupport.fireCollectionChanged(null, -1, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     *
     * @return
     */
    public int size() {
        return rightsList.size();
    }

    /**
     *
     * @param rights
     * @return
     */
    public int indexOf(Rights rights) {
        return rightsList.indexOf(rights);
    }

    /**
     *
     * @param rights
     * @return
     */
    public boolean contains(Rights rights) {
        return rightsList.contains(rights);
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return rightsList.isEmpty();
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
}

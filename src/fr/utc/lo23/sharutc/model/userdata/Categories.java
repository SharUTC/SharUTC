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
public class Categories implements Serializable {

    private static final long serialVersionUID = -1740797994155053145L;
    @JsonIgnore
    private CollectionChangeSupport collectionChangeSupport = new CollectionChangeSupport(this);
    private HashSet<Category> categories;

    /**
     *
     */
    public Categories() {
    }

    /**
     *
     * @return
     */
    public HashSet<Category> getCategories() {
        return categories;
    }

    /**
     *
     * @param categories
     */
    public void setCategories(HashSet<Category> categories) {
        this.categories = categories;
    }

    /**
     *
     * @param category
     * @return
     */
    public boolean add(Category category) {
        boolean added = categories.add(category);
        if (added) {
            collectionChangeSupport.fireCollectionChanged(category, -1, CollectionEvent.Type.ADD);
        }
        return added;
    }

    /**
     *
     * @param categories
     */
    public void addAll(Collection<Category> categories) {
        if (categories != null && !categories.isEmpty()) {
            for (Category category : categories) {
                this.add(category);
            }
        }
    }

    /**
     *
     * @param category
     * @return
     */
    public boolean remove(Category category) {
        boolean removed = categories.remove(category);
        if (removed) {
            collectionChangeSupport.fireCollectionChanged(category, -1, CollectionEvent.Type.REMOVE);
        }
        return removed;
    }

    /**
     *
     */
    public void clear() {
        if (!categories.isEmpty()) {
            categories.clear();
            collectionChangeSupport.fireCollectionChanged(null, -1, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     *
     * @return
     */
    public int size() {
        return categories.size();
    }

    /**
     *
     * @param category
     * @return
     */
    public boolean contains(Category category) {
        return categories.contains(category);
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return categories.isEmpty();
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

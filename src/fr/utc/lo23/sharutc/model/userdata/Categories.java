package fr.utc.lo23.sharutc.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionChangeSupport;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class Categories implements Serializable {

    private static final long serialVersionUID = -1740797994155053145L;
    @JsonIgnore
    private CollectionChangeSupport mCollectionChangeSupport = new CollectionChangeSupport(this);
    private HashSet<Category> mCategories;

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
        return mCategories;
    }

    /**
     *
     * @param categories
     */
    public void setCategories(HashSet<Category> categories) {
        this.mCategories = categories;
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public Category findCategoryByName(String name) {
        for (Category category : mCategories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    public Set<Integer> getCategoriesIdsByContactId(Long id) {
        Set<Integer> categoriesIds = new HashSet<Integer>();
        for (Category category : mCategories) {
            if (category.findContactId(id) != null) {
                categoriesIds.add(category.getId());
            }
        }
        return categoriesIds;
    }

    /**
     *
     * @param category
     * @return
     */
    public boolean add(Category category) {
        boolean added = mCategories.add(category);
        if (added) {
            mCollectionChangeSupport.fireCollectionChanged(category, -1, CollectionEvent.Type.ADD);
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
        boolean removed = mCategories.remove(category);
        if (removed) {
            mCollectionChangeSupport.fireCollectionChanged(category, -1, CollectionEvent.Type.REMOVE);
        }
        return removed;
    }

    /**
     *
     */
    public void clear() {
        if (!mCategories.isEmpty()) {
            mCategories.clear();
            mCollectionChangeSupport.fireCollectionChanged(null, -1, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     *
     * @return
     */
    public int size() {
        return mCategories.size();
    }

    /**
     *
     * @param category
     * @return
     */
    public boolean contains(Category category) {
        return mCategories.contains(category);
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return mCategories.isEmpty();
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

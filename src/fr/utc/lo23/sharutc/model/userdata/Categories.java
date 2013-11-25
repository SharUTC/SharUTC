package fr.utc.lo23.sharutc.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionChangeSupport;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import java.io.Serializable;
import java.util.Collection;

import java.util.LinkedHashSet;


/**
 * Represents a list of categories
 * 
 * Don't use index from CollectionChangeSupport (HashSet inside)
 */
public class Categories implements Serializable {

    private static final long serialVersionUID = -1740797994155053145L;
    @JsonIgnore
    private CollectionChangeSupport mCollectionChangeSupport = new CollectionChangeSupport(this);
    private LinkedHashSet<Category> mCategories;

    /**
     * Constructor
     */
    public Categories() {
        this.mCategories = new LinkedHashSet<Category>();
    }

    /**
     * Return the category list
     * 
     * @return the category list
     */
    public LinkedHashSet<Category> getCategories() {
        return mCategories;
    }

    /**
     * Set the category list
     * 
     * @param categories - a category list
     */
    public void setCategories(LinkedHashSet<Category> categories) {
        this.mCategories = categories;
    }

    /**
     * Find the category thanks to its id given in parameter if it exists
     * 
     * @param id
     * @return the category or null if it does not exist
     */
    public Category findCategoryById(Integer id) {
        Category category = null;
        for (Category c : mCategories) {
            if (c.getId() == id) {
                category = c;
            }
        }
        return category;
    }

    /**
     * Add a category to the category list
     * 
     * @param category
     * @return a boolean
     */
    public boolean add(Category category) {
        boolean added = mCategories.add(category);
        if (added) {
            mCollectionChangeSupport.fireCollectionChanged(category, -1, CollectionEvent.Type.ADD);
        }
        return added;
    }

    /**
     * Add a category list
     * 
     * @param categories - a category list
     */
    public void addAll(Collection<Category> categories) {
        if (categories != null && !categories.isEmpty()) {
            for (Category category : categories) {
                this.add(category);
            }
        }
    }

    /**
     * Remove a category from the category list
     * 
     * @param category
     * @return a boolean
     */
    public boolean remove(Category category) {
        boolean removed = mCategories.remove(category);
        if (removed) {
            mCollectionChangeSupport.fireCollectionChanged(category, CollectionEvent.Type.REMOVE);
        }
        return removed;
    }

    /**
     * Empty the category list
     */
    public void clear() {
        if (!mCategories.isEmpty()) {
            mCategories.clear();
            mCollectionChangeSupport.fireCollectionChanged(null, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     * Return the size of the category list
     * 
     * @return the size of the category list
     */
    public int size() {
        return mCategories.size();
    }

    /**
     * Check if the category list contains the category given in parameter
     * 
     * @param category
     * @return a boolean
     */
    public boolean contains(Category category) {
        return mCategories.contains(category);
    }

    /**
     * Check if the category list is empty
     * 
     * @return a boolean
     */
    @JsonIgnore
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

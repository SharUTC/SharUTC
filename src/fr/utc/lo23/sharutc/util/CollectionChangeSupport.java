package fr.utc.lo23.sharutc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A helper class for {@link CollectionListener CollectionListener} management.
 * <code>T</code> is the type of item stored in the collection.
 *
 * @param <T>
 */
public class CollectionChangeSupport<T> {

    private final Object source;
    private final List<CollectionChangeListener<T>> collectionListeners;

    /**
     * Creates a collection change support.
     *
     * @param source
     */
    public CollectionChangeSupport(Object source) {
        this.source = source;
        this.collectionListeners = new ArrayList<CollectionChangeListener<T>>(5);
    }

    /**
     * Adds the
     * <code>listener</code> in parameter to the list of listeners that may be
     * notified.
     *
     * @param listener
     */
    public void addCollectionListener(CollectionChangeListener<T> listener) {
        this.collectionListeners.add(listener);
    }

    /**
     * Removes the
     * <code>listener</code> in parameter to the list of listeners that may be
     * notified.
     *
     * @param listener
     */
    public void removeCollectionListener(CollectionChangeListener<T> listener) {
        this.collectionListeners.remove(listener);
    }

    /**
     * Fires a collection event about
     * <code>item</code>.
     *
     * @param item
     * @param eventType
     */
    public void fireCollectionChanged(T item, CollectionEvent.Type eventType) {
        fireCollectionChanged(item, -1, eventType);
    }

    /**
     * Fires a collection event about
     * <code>item</code> at a given
     * <code>index</code>.
     *
     * @param item
     * @param eventType
     * @param index
     */
    @SuppressWarnings("unchecked")
    public void fireCollectionChanged(T item, int index,
            CollectionEvent.Type eventType) {
        if (!this.collectionListeners.isEmpty()) {
            CollectionEvent<T> collectionEvent =
                    new CollectionEvent<T>(this.source, item, index, eventType);
            // Work on a copy of collectionListeners to ensure a listener
            // can modify safely listeners list
            CollectionChangeListener<T>[] listeners = this.collectionListeners.
                    toArray(new CollectionChangeListener[this.collectionListeners.size()]);
            for (CollectionChangeListener<T> listener : listeners) {
                listener.collectionChanged(collectionEvent);
            }
        }
    }
}
package fr.utc.lo23.sharutc.util;

import java.util.EventObject;

/**
 * Type of event notified when an item is added or deleted from a list.
 * <code>T</code> is the type of item stored in the collection.
 *
 * @param <T>
 */
public class CollectionEvent<T> extends EventObject {

    private static final long serialVersionUID = -7204625178235814823L;
    private final T item;
    private final int index;
    private final Type type;

    /**
     * Creates an event for an item that has no index.
     *
     * @param source
     * @param item
     * @param type
     */
    public CollectionEvent(Object source, T item, Type type) {
        this(source, item, -1, type);
    }

    /**
     * Creates an event for an item with its index.
     *
     * @param source
     * @param type
     * @param item
     * @param index
     */
    public CollectionEvent(Object source, T item, int index, Type type) {
        super(source);
        this.item = item;
        this.index = index;
        this.type = type;
    }

    /**
     * Returns the added or deleted item.
     *
     * @return
     */
    public T getItem() {
        return this.item;
    }

    /**
     * Returns the index of the item in collection or -1 if this index is
     * unknown.
     *
     * @return
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns the type of event.
     *
     * @return
     */
    public Type getType() {
        return this.type;
    }

    /**
     * The type of change in the collection.
     */
    public enum Type {

        /**
         *
         */
        ADD,
        /**
         *
         */
        REMOVE,
        /**
         *
         */
        CLEAR,
        /**
         *
         */
        UPDATE
    }
}
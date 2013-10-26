/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.util;

import java.util.EventListener;

/**
 * A listener notified when items are added or removed from a collection.
 * <code>T</code> is the type of item stored in the collection.
 *
 * @param <T> 
 */
public interface CollectionChangeListener<T> extends EventListener {

    /**
     * Called when an item is added or deleted from a collection.
     * @param ev 
     */
    public void collectionChanged(CollectionEvent<T> ev);
}
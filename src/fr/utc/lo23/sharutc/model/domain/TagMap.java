package fr.utc.lo23.sharutc.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A TagMap contains a HashMap of each Tag with a weight, each music tag has a
 * weight of 1. Send updates to view on new tag, when a tag occurrence changes
 * and when the map is cleared. A Tag name must have at least 1 character
 *
 */
public class TagMap implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(TagMap.class);
    private static final long serialVersionUID = 803156793863616658L;
    @JsonIgnore
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private HashMap<String, Integer> map = new HashMap<String, Integer>();

    /**
     * Create an empty TagMap
     */
    public TagMap() {
    }

    /**
     * Create an TagMap with all existing tags in the catalog
     *
     * @param localCatalog
     */
    public TagMap(Catalog localCatalog) {
        for (Music m : localCatalog.getMusics()) {
            this.merge(m);
        }
    }

    /**
     *
     * @return
     */
    public HashMap<String, Integer> getMap() {
        return map;
    }

    /**
     *
     * @param map
     */
    public void setMap(HashMap<String, Integer> map) {
        this.map = map;
    }

    /**
     * Create or update tag weight with another TagMap
     *
     * @param tagMap
     */
    public void merge(TagMap tagMap) {
        if (tagMap != null) {
            for (Entry<String, Integer> entry : tagMap.entrySet()) {
                merge(entry.getKey(), entry.getValue());
            }
        } else {
            log.warn("Merging null TagMap");
        }
    }

    /**
     * Add or update TagMap with tags associated to a music
     *
     * @param music 
     */
    public final void merge(Music music) {
        for (String tag : music.getTags()) {
            merge(tag, 1);
        }
    }

    /**
     * Add or update TagMap with one tag, update view (TAG_NEW, TAG_OCCURENCE)
     *
     * @param tag
     * @param occurence
     */
    public void merge(String tag, int occurence) {
        if (tag != null) {
            tag = tag.trim();
            if (tag.length() > 0) {
                tag = tag.toLowerCase();
                tag = tag.substring(0, 1).toUpperCase() + (tag.length() > 1 ? tag.substring(1) : "");
                int oldOccurence = map.containsKey(tag) ? (map.get(tag).intValue()) : 0;
                map.put(tag, occurence + oldOccurence);
                if (oldOccurence == 0) {
                    propertyChangeSupport.firePropertyChange(Property.TAG_NEW.name(), 0, occurence);
                } else {
                    propertyChangeSupport.firePropertyChange(Property.TAG_OCCURENCE.name(), oldOccurence, occurence + oldOccurence);
                }
            } else {
                log.warn("Using empty tag name, skipping it");
            }
        } else {
            log.warn("Using null tag name, skipping it");
        }
    }

    /**
     * Clear TagMap content, update view (MAP_CLEAR)
     */
    public void clear() {
        map.clear();
        propertyChangeSupport.firePropertyChange(Property.MAP_CLEAR.name(), null, null);
    }

    private Iterable<Entry<String, Integer>> entrySet() {
        return map.entrySet();
    }

    /**
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public boolean equals(Object tagMap) {
        return tagMap != null && (tagMap instanceof TagMap) && ((TagMap) tagMap).map.equals(map);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.map != null ? this.map.hashCode() : 0);
        return hash;
    }

    /**
     *
     */
    public enum Property {

        /**
         * When a tag name is already listed in the map, occurrence is only
         * updated
         */
        TAG_OCCURENCE,
        /**
         * When the tag map is cleared
         */
        MAP_CLEAR,
        /**
         * When a new tag name appears in the map
         */
        TAG_NEW
    }
}

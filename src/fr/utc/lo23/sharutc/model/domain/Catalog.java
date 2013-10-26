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
 *
 */
public class Catalog implements Serializable {

    private static final long serialVersionUID = -6927278754684107936L;
    private List<Music> musics = new ArrayList<Music>();
    @JsonIgnore
    private CollectionChangeSupport collectionChangeSupport = new CollectionChangeSupport(this);

    /**
     *
     */
    public Catalog() {
    }

    /**
     * Getter for serialization DO NOT do any changes on this list, use methods
     * of the catalog instead to update the view
     *
     * @return direct references to all musics
     */
    public List<Music> getMusics() {
        return Collections.unmodifiableList(musics);
    }

    /**
     *
     * @param musics
     */
    public void setMusics(List<Music> musics) {
        clear();
        addAll(musics);
    }

    /**
     *
     * @param index
     * @return
     */
    public Music get(int index) {
        return musics.get(index);
    }

    /**
     *
     * @param id
     * @return
     */
    public Music findMusicById(long id) {
        for (Music m : musics) {
            if (m.getId().longValue() == id) {
                return m;
            }
        }
        return null;
    }

    /**
     *
     * @param hash
     * @return
     */
    public Music findMusicByHash(int hash) {
        for (Music m : musics) {
            if (m.getFile() != null && m.getMusicHash() == hash) {
                return m;
            }
        }
        return null;
    }

    /**
     *
     * @param music
     * @return
     */
    public boolean add(Music music) {
        boolean added = musics.add(music);
        if (added) {
            collectionChangeSupport.fireCollectionChanged(music, musics.size() - 1, CollectionEvent.Type.ADD);
        }
        return added;
    }

    /**
     *
     * @param index
     * @param music
     */
    public void add(int index, Music music) {
        musics.add(index, music);
        collectionChangeSupport.fireCollectionChanged(music, index, CollectionEvent.Type.ADD);
    }

    /**
     *
     * @param index
     * @param music
     * @return
     */
    public Music set(int index, Music music) {
        Music set = musics.set(index, music);
        collectionChangeSupport.fireCollectionChanged(music, index, CollectionEvent.Type.UPDATE);
        return set;
    }

    /**
     *
     * @param musics
     */
    public void addAll(List<Music> musics) {
        if (musics != null && !musics.isEmpty()) {
            for (Music music : musics) {
                this.add(music);
            }
        }
    }

    /**
     *
     * @param music
     * @return
     */
    public boolean remove(Music music) {
        boolean removed = musics.remove(music);
        if (removed) {
            collectionChangeSupport.fireCollectionChanged(music, -1, CollectionEvent.Type.REMOVE);
        }
        return removed;
    }

    /**
     *
     */
    public void clear() {
        if (!musics.isEmpty()) {
            musics.clear();
            collectionChangeSupport.fireCollectionChanged(null, -1, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     *
     * @return
     */
    public int size() {
        return musics.size();
    }

    /**
     *
     * @param music
     * @return
     */
    public int indexOf(Music music) {
        return musics.indexOf(music);
    }

    /**
     *
     * @param music
     * @return
     */
    public boolean contains(Music music) {
        return musics.contains(music);
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return musics.isEmpty();
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

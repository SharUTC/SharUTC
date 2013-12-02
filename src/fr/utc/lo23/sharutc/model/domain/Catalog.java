package fr.utc.lo23.sharutc.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionChangeSupport;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains a list of Music instance. Send updates when a music is addeed,
 * removed, updated, and when the list is cleared through a
 * CollectionChangeListener. Inform listener if a contained music has been
 * updated or is missing
 *
 * getter for Musics returns an unmodifiable list, use the catalog to change
 * them, getter is here for looping purpose or others, not altering
 */
public class Catalog implements Serializable, PropertyChangeListener {

    private static final long serialVersionUID = -6927278754684107936L;
    private ArrayList<Music> mMusics = new ArrayList<Music>();
    @JsonIgnore
    private CollectionChangeSupport collectionChangeSupport = new CollectionChangeSupport(this);

    /**
     *
     */
    public Catalog() {
    }

    /**
     * Give access to the contained list of musics, but its content is locked
     * and cannot be modified this way
     *
     * @return an unmodifiable List of Music instances
     */
    public List<Music> getMusics() {
        return Collections.unmodifiableList(mMusics);
    }

    /**
     * Clear all previous Musics from this catalog and add the new musics
     *
     * @param musics the musics to put in this catalog
     */
    public void setMusics(List<Music> musics) {
        clear();
        addAll(musics);
    }

    /**
     * Get the musics at the given index, uses same restriction as usual List
     *
     * @param index the index of the given music, >=0 && <size() @ return
     */
    public Music get(int index) {
        return mMusics.get(index);
    }

    /**
     *
     * @param id the id of the music
     * @return the searched music if id exists in this catalog, else null
     */
    public Music findMusicById(long id) {
        Music foundMusic = null;
        for (Music m : mMusics) {
            if (m.getId().longValue() == id) {
                foundMusic = m;
                break;
            }
        }
        return foundMusic;
    }

    /**
     *
     * @param hash the hash of the music
     * @return the searched music if hash exists in this catalog, else null
     */
    public Music findMusicByHash(int hash) {
        Music foundMusic = null;
        for (Music m : mMusics) {
            if (m.getHash() == hash) {
                foundMusic = m;
                break;
            }
        }
        return foundMusic;
    }

    /**
     * Add a music to this catalog, send update (ADD)
     *
     * @param music the music to add
     * @return true if the music was added (java norm, not used here)
     */
    public boolean add(Music music) {
        add(mMusics.size(), music);
        return true;
    }

    /**
     * Add a music to this catalog at the specified index, send update (ADD)
     *
     * @param index where to add the music, must be inside [0; size()]
     * @param music the music to add
     */
    public void add(int index, Music music) {
        Music musicToAdd = music.clone();
        musicToAdd.addPropertyChangeListener(this);
        mMusics.add(index, musicToAdd);
        collectionChangeSupport.fireCollectionChanged(musicToAdd, index, CollectionEvent.Type.ADD);
    }

    /**
     * Replace the music at the specified index, send update (UPDATE)
     *
     * @param index where to update the music, must be inside [0; size()[
     * @param music the music to add
     * @return the previously element at the given position
     */
    public Music set(int index, Music music) {
        Music set = mMusics.set(index, music);
        collectionChangeSupport.fireCollectionChanged(music, index, CollectionEvent.Type.UPDATE);
        return set;
    }

    /**
     * Add all musics to this catalog, send updates (ADD)
     *
     * @param musics the musics to add
     */
    public void addAll(List<Music> musics) {
        if (musics != null && !musics.isEmpty()) {
            for (Music music : musics) {
                this.add(music);
            }
        }
    }

    /**
     * Copy all music from one catalog to another, don't use it on local catalog
     *
     * @param catalog
     */
    public void merge(Catalog catalog) {
        if (catalog != null && !catalog.mMusics.isEmpty()) {
            for (Music music : catalog.mMusics) {
                this.add(music);
            }
        }
    }

    /**
     * Remove a given music from this catalog if it exists, send updates
     * (REMOVE) with removed Id
     *
     * @param music the music to remove
     * @return true if the music was removed, else false
     */
    public boolean remove(Music music) {
        music.removePropertyChangeListener(this);
        int indexRemoved = mMusics.indexOf(music);
        boolean removed = false;
        if (indexRemoved != -1) {
            removed = mMusics.remove(music);
            if (removed) {
                collectionChangeSupport.fireCollectionChanged(music, indexRemoved, CollectionEvent.Type.REMOVE);
            }
        }
        return removed;
    }

    /**
     * Remove all musics from this catalog, send update (CLEAR)
     */
    public void clear() {
        if (!mMusics.isEmpty()) {
            for (Music music : mMusics) {
                music.removePropertyChangeListener(this);
            }
            mMusics.clear();
            collectionChangeSupport.fireCollectionChanged(null, -1, CollectionEvent.Type.CLEAR);
        }
    }

    /**
     * The number of Musics contained in this Catalog
     *
     * @return the number of Musics in this Catalog
     */
    public int size() {
        return mMusics.size();
    }

    /**
     * Gives the index of the music in this catalog
     *
     * @param music the music to find in this catalog
     * @return the index of the music in this catalog, -1 if there's no such
     * music
     */
    public int indexOf(Music music) {
        return mMusics.indexOf(music);
    }

    /**
     * Return true if the music exists in this catalog, else false
     *
     * @param music the music to find in this catalog
     * @return true if the music exists in this catalog, else false
     */
    public boolean contains(Music music) {
        return mMusics.contains(music);
    }

    /**
     * Return true if the catalog has no musics, else false
     *
     * @return true if the catalog has no musics, else false
     */
    @JsonIgnore
    public boolean isEmpty() {
        return mMusics.isEmpty();
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() != null && evt.getSource() instanceof Music && evt.getPropertyName() != null) {
            boolean musicFieldOrNameUpdated = false;
            if (evt.getPropertyName().equals(Music.Property.FILENAME.name())) {
                musicFieldOrNameUpdated = true;
            } else if (evt.getPropertyName().equals(Music.Property.TITLE.name())) {
                musicFieldOrNameUpdated = true;
            } else if (evt.getPropertyName().equals(Music.Property.ARTIST.name())) {
                musicFieldOrNameUpdated = true;
            } else if (evt.getPropertyName().equals(Music.Property.ALBUM.name())) {
                musicFieldOrNameUpdated = true;
            } else if (evt.getPropertyName().equals(Music.Property.TRACK.name())) {
                musicFieldOrNameUpdated = true;
            } else if (evt.getPropertyName().equals(Music.Property.YEAR.name())) {
                musicFieldOrNameUpdated = true;
            } else if (evt.getPropertyName().equals(Music.Property.FILE_MISSING.name())) {
                collectionChangeSupport.fireCollectionChanged(evt.getSource(), indexOf((Music) evt.getSource()), CollectionEvent.Type.UPDATE);
            }
            if (musicFieldOrNameUpdated) {
                collectionChangeSupport.fireCollectionChanged(evt.getSource(), indexOf((Music) evt.getSource()), CollectionEvent.Type.UPDATE);
            }
        }
    }
}

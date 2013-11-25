package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.io.File;
import java.util.Collection;

/**
 *
 */
public interface MusicService {

    /**
     *
     * Adding a collection of mp3 files to user's local catalog.
     *
     * @param mp3Files
     */
    public void addToLocalCatalog(Collection<File> mp3Files);

    /**
     *
     * Removing a collection of musics from local catalog.
     *
     * @param musics
     */
    public void removeFromLocalCatalog(Collection<Music> musics);

    /**
     *
     * Integrating music from catalog parameter to the remote catalog.
     *
     * @param peer
     * @param catalog
     */
    public void integrateRemoteCatalog(Peer peer, Catalog catalog);

    /**
     *
     * Getting available parts of current user's catalog for a given peer, while
     * taking account of current user's rights regarding peer's music
     *
     * @param peer
     * @return
     */
    public Catalog getCatalogForPeer(Peer peer);

    /**
     *
     * @return
     */
    public TagMap getLocalTagMap();

    /**
     *
     * @param tagMap
     */
    public void integrateRemoteTagMap(TagMap tagMap);

    /**
     *
     * @param music
     * @param tag
     */
    public void addTag(Music music, String tag);

    /**
     *
     * @param music
     * @param tag
     */
    public void removeTag(Music music, String tag);

    /**
     * Add the comment on a music given by a peer
     *
     * @param peer The peer who gives the comment
     * @param music The music which has to be commented
     * @param comment The comment of the music
     */
    public void addComment(Peer peer, Music music, String comment);

    /**
     * Edit the comment on a music given by a peer
     *
     * @param peer The peer who gives the comment
     * @param music The music which has to be commented
     * @param comment The comment of the music
     * @param commentIndex The index of the comment to edit
     */
    public void editComment(Peer peer, Music music, String comment, Integer commentIndex);

    /**
     * Remove the comment on a music given by a peer
     *
     * @param peer The peer who gives the comment
     * @param music The music which has to be commented
     * @param commentIndex The index of the comment to remove
     */
    public void removeComment(Peer peer, Music music, Integer commentIndex);

    /**
     * Define the score on a music given by a peer
     *
     * @param peer The peer who gives the score
     * @param music The music which has to be scored
     * @param score The value of the score, might be null to remove value
     * (unsetScore)
     */
    public void setScore(Peer peer, Music music, Integer score);

    /**
     * Unset the score on a music given by a peer
     *
     * @param peer The peer who gives the score
     * @param music The music which has to be scored
     */
    public void unsetScore(Peer peer, Music music);

    /**
     * Write to disk Catalog and RightsList in a JSON file
     */
    public void saveUserMusicFile();

    /**
     *
     */
    public void loadUserMusicFile();

    /**
     *
     */
    public void saveUserRightsListFile();

    /**
     *
     */
    public void loadUserRightsListFile();

    /**
     *
     * @param peer
     * @param criteria
     * @return
     */
    public Catalog searchMusic(Peer peer, SearchCriteria criteria);

    /**
     *
     * @param catalog
     */
    public void integrateMusicSearch(Catalog catalog);

    /**
     *
     * @param catalog
     */
    public void installMusics(Catalog catalog);

    /**
     * Load file into passed music, work with the instance in parameter
     *
     * @param music the music to load
     */
    public void loadMusicFile(Music music);

    /**
     * Load file into passed catalog of musics, work with the instance in
     * parameter
     *
     * @param catalog the catalog containing the music files to load
     */
    public void loadMusicFiles(Catalog catalog);

    /**
     * Create a new Catalog instance and set it to appModel. Used at Profile
     * creation only
     */
    public void createAndSetCatalog();

    /**
     * Create a new RightsList instance and set it to appModel. Used at Profile
     * creation only
     */
    public void createAndSetRightsList();

    /**
     * Changes the realname (visible one) and the filename (hidden).
     *
     * @param music the music to modify
     * @param name with or without .mp3, must be at leacht one char
     */
    public void updateMusicFileName(Music music, String name);

    /**
     * Only used to save updated values of music from local files. Compare
     * updatable fields one by one between the music passed in parameter and the
     * one stored in localCatalog
     *
     * @param music the music to modify, nothing happens if null
     * @param title the title to set, or null if not changed
     * @param artist the artist to set, or null if not changed
     * @param album the album to set, or null if not changed
     * @param track the track to set, or null if not changed
     * @param year the year to set, or null if not changed
     */
    public void saveMusicFieldChanges(Music music, String title, String artist, String album, String track, String year);

    /**
     * Add a music to a category
     *
     * @param music
     * @param category
     */
    public void addMusicToCategory(Music music, Category category);

    /**
     * Remove a music from a category
     *
     * @param music
     * @param category
     */
    public void removeMusicFromCategory(Music music, Category category);

    /**
     * Remove peer data from KnownPeerList if they are useless
     *
     * @param peer the peer whom we're looking for any comment or score
     */
    public void removeFromKnownPeersIfUseless(Peer peer);
}

package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.io.File;
import java.util.Collection;

/**
 *
 */
public interface MusicService {

    /**
     *
     * @param mp3Files
     */
    public void addToLocalCatalog(Collection<File> mp3Files);

    /**
     *
     * @param musics
     */
    public void removeFromLocalCatalog(Collection<Music> musics);

    /**
     *
     * @param peer
     * @param catalog
     */
    public void integrateRemoteCatalog(Peer peer, Catalog catalog);

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
     *
     * @param peer
     * @param music
     * @param comment
     */
    public void addComment(Peer peer, Music music, String comment);

    /**
     *
     * @param peer
     * @param music
     * @param comment
     * @param commentIndex
     */
    public void editComment(Peer peer, Music music, String comment, Integer commentIndex);

    /**
     *
     * @param peer
     * @param music
     * @param commentIndex
     */
    public void removeComment(Peer peer, Music music, Integer commentIndex);

    /**
     * Define the score on a music given by a peer
     *
     * @param peer The peer who gives the score
     * @param music The music which has to be scored
     * @param score The value of the score
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
     *
     */
    public void saveUserMusicFiles();

    /**
     *
     * @param path
     */
    public void loadUserMusicFiles(String path);

    /**
     *
     * @param peer
     * @param criteria
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
     * Load file into passed catalog of musics, work with the instance in
     * parameter
     *
     * @param catalog the catalog containing the music files to load
     * @return the instance of catalog with modified musics inside
     */
    public Catalog loadMusicFiles(Catalog catalog);
}

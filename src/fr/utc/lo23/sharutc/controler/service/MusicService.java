package fr.utc.lo23.sharutc.controler.service;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.io.File;
import java.util.List;

/**
 *
 */
public interface MusicService {

    /**
     *
     * @param mp3Files
     */
    public void addToLocalCatalog(List<File> mp3Files);

    /**
     *
     * @param musics
     */
    public void removeFromLocalCatalog(List<Music> musics);

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
     *
     * @param peer 
     * @param music
     * @param score
     */
    public void setScore(Peer peer, Music music, Integer score);

    /**
     *
     * @param peer 
     * @param music
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
    public void searchMusic(Peer peer, SearchCriteria criteria);

    /**
     *
     * @param catalog
     */
    public void integrateMusicSearch(Catalog catalog);

    /**
     *
     * @param music
     * @return
     */
    public Music getMusicWithFile(Music music);

    /**
     *
     * @param catalog
     */
    public void installMusics(Catalog catalog);
}

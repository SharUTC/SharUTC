package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
@Singleton
public class MusicServiceMock implements MusicService {

    private static final Logger log = LoggerFactory
            .getLogger(MusicServiceMock.class);

    @Override
    public void addToLocalCatalog(List<File> mp3Files) {
        log.warn("Not supported yet.");
    }

    @Override
    public void removeFromLocalCatalog(List<Music> musics) {
        log.warn("Not supported yet.");
    }

    @Override
    public void integrateRemoteCatalog(Peer peer, Catalog catalog) {
        log.warn("Not supported yet.");
    }

    @Override
    public void buildLocalTagMap() {
        log.warn("Not supported yet.");
    }

    @Override
    public TagMap getLocalTagMap() {
        TagMap dummyTagMap = new TagMap();
        dummyTagMap.merge("Rock", 10);
        dummyTagMap.merge("Pop", 3);
        dummyTagMap.merge("Disco", 2000);
        return dummyTagMap;
    }

    @Override
    public void integrateRemoteTagMap(TagMap tagMap) {
        log.warn("Not supported yet.");
    }

    @Override
    public void addTag(Music music, String tag) {
        log.warn("Not supported yet.");
    }

    @Override
    public void removeTag(Music music, String tag) {
        log.warn("Not supported yet.");
    }

    @Override
    public void addComment(Peer peer, Music music, String comment) {
        log.warn("Not supported yet.");
    }

    @Override
    public void editComment(Peer peer, Music music, String comment, Integer commentIndex) {
        log.warn("Not supported yet.");
    }

    @Override
    public void removeComment(Peer peer, Music music, Integer commentIndex) {
        log.warn("Not supported yet.");
    }

    @Override
    public void setScore(Peer peer, Music music, Integer score) {
        log.warn("Not supported yet.");
    }

    @Override
    public void unsetScore(Peer peer, Music music) {
        log.warn("Not supported yet.");
    }

    @Override
    public void saveUserMusicFiles() {
        log.warn("Not supported yet.");
    }

    @Override
    public void loadUserMusicFiles(String path) {
        log.warn("Not supported yet.");
    }

    @Override
    public void searchMusic(Peer peer, SearchCriteria criteria) {
        log.warn("Not supported yet.");
    }

    @Override
    public void integrateMusicSearch(Catalog catalog) {
        log.warn("Not supported yet.");
    }

    @Override
    public Music getMusicWithFile(Music music) {
        log.warn("Not supported yet.");
        return null;
    }

    @Override
    public void installMusics(Catalog catalog) {
        log.warn("Not supported yet.");
    }
}

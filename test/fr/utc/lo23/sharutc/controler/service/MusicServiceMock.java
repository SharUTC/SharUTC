package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.io.File;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
@Singleton
public class MusicServiceMock extends MusicServiceImpl implements MusicService {

    private static final Logger log = LoggerFactory
            .getLogger(MusicServiceMock.class);

    @Inject
    public MusicServiceMock(AppModel appModel, UserService userService, FileService fileService) {
        super(appModel, userService, fileService);
    }

    @Override
    public void addToLocalCatalog(Collection<File> mp3Files) {
        super.addToLocalCatalog(mp3Files);
    }

    @Override
    public void removeFromLocalCatalog(Collection<Music> musics) {
        super.removeFromLocalCatalog(musics);
    }

    @Override
    public void integrateRemoteCatalog(Peer peer, Catalog catalog) {
        super.integrateRemoteCatalog(peer, catalog);
    }

    @Override
    public TagMap getLocalTagMap() {
        return super.getLocalTagMap();
    }

    @Override
    public void integrateRemoteTagMap(TagMap tagMap) {
        super.integrateRemoteTagMap(tagMap);
    }

    @Override
    public void addTag(Music music, String tag) {
        super.addTag(music, tag);
    }

    @Override
    public void removeTag(Music music, String tag) {
        super.removeTag(music, tag);
    }

    @Override
    public void addComment(Peer peer, Music music, String comment) {
        super.addComment(peer, music, comment);
    }

    @Override
    public void editComment(Peer peer, Music music, String comment, Integer commentIndex) {
        super.editComment(peer, music, comment, commentIndex);
    }

    @Override
    public void removeComment(Peer peer, Music music, Integer commentIndex) {
        super.removeComment(peer, music, commentIndex);
    }

    @Override
    public void setScore(Peer peer, Music music, Integer score) {
        super.setScore(peer, music, score);
    }

    @Override
    public void unsetScore(Peer peer, Music music) {
        super.unsetScore(peer, music);
    }

    @Override
    public void saveUserMusicFiles() {
        super.saveUserMusicFiles();
    }

    @Override
    public void loadUserMusicFiles(String path) {
        super.loadUserMusicFiles(path);
    }

    @Override
    public Catalog searchMusic(Peer peer, SearchCriteria criteria) {
        return super.searchMusic(peer, criteria);
    }

    @Override
    public void integrateMusicSearch(Catalog catalog) {
        super.integrateMusicSearch(catalog);
    }

    @Override
    public void loadMusicFiles(Catalog catalog) {
        super.loadMusicFiles(catalog);
    }

    @Override
    public void installMusics(Catalog catalog) {
        super.installMusics(catalog);
    }
}

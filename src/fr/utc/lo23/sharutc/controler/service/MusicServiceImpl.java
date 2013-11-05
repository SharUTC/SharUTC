package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Comment;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.Rights;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
@Singleton
public class MusicServiceImpl implements MusicService {

    private static final Logger log = LoggerFactory.getLogger(MusicServiceImpl.class);
    private final AppModel appModel;
    private final FileService fileService;
    private TagMap localTagMap = null;
    private boolean localTagMapDirty = true;

    /**
     * {@inheritDoc}
     */
    @Inject
    public MusicServiceImpl(AppModel appModel, FileService fileService) {
        this.appModel = appModel;
        this.fileService = fileService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToLocalCatalog(Collection<File> mp3Files) {
        Catalog localCatalog = appModel.getLocalCatalog();

        for (File currentFile : mp3Files) {
            Music musicFromFile = null;
            try {
                musicFromFile = fileService.readFile(currentFile);
            } catch (Exception ex) {
                log.error(ex.toString());
            }
            if (musicFromFile != null) {
                if (localCatalog.contains(musicFromFile)) {
                    log.warn("Skipping add of already existing music (hash equals) :\n{}\n{}",
                            localCatalog.findMusicByHash(musicFromFile.getMusicHash()).getRealName(),
                            musicFromFile.getRealName());
                } else {
                    localCatalog.add(musicFromFile);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromLocalCatalog(Collection<Music> musics) {
        Catalog localCatalog = appModel.getLocalCatalog();

        for (Music currentMusic : musics) {
            if (localCatalog.contains(currentMusic)) {
                localCatalog.remove(currentMusic);
            } else {
                log.warn("Music to delete not found !\n{}", currentMusic.getRealName());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void integrateRemoteCatalog(Peer peer, Catalog catalog) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagMap getLocalTagMap() {
        if (localTagMap == null || isLocalTagMapDirty()) {
            buildLocalTagMap();
        }
        return localTagMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void integrateRemoteTagMap(TagMap tagMap) {
        appModel.getNetworkTagMap().merge(tagMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTag(Music music, String tag) {
        if (music != null && tag != null && !tag.isEmpty()) {
            if (music.addTag(tag)) {
                localTagMapDirty = true;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTag(Music music, String tag) {
        if (music != null && tag != null && !tag.isEmpty()) {
            if (music.removeTag(tag)) {
                localTagMapDirty = true;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComment(Peer peer, Music music, String comment) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editComment(Peer peer, Music music, String comment, Integer commentIndex) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComment(Peer peer, Music music, Integer commentIndex) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScore(Peer peer, Music music, Integer score) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsetScore(Peer peer, Music music) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveUserMusicFiles() {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadUserMusicFiles(String path) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog searchMusic(Peer peer, SearchCriteria criteria) {
        Catalog catalogResult = new Catalog();

        if (criteria.getSearch() != null && criteria.getSearch().trim().length() > 0) {

            // 2 modes : when peer is a contact and when peer isn't a contact
            Contact contact = appModel.getProfile().getContacts().findById(peer.getId());
            if (contact != null) {
                Set<Integer> contactCategoryIds = contact.getCategoryIds();

                // looping on whole catalog, searching for matching music informations
                for (Music music : appModel.getLocalCatalog().getMusics()) {
                    // only deal with needed musics
                    if (musicMatchesSearchCriteria(music, criteria)) {

                        // searching for useful categories only
                        List<Integer> matchingCategoryIds = getAllMatchingCategoryIds(music, contactCategoryIds);

                        // searching Rights values to be set directly on a copy of music instance if added to the results
                        boolean mayReadInfo = false;
                        boolean mayListen = false;
                        boolean mayNoteAndComment = false;
                        if (matchingCategoryIds.isEmpty()) {
                            for (Integer categoryId : matchingCategoryIds) {
                                // avoid useless loop
                                if (mayReadInfo && mayListen && mayNoteAndComment) {
                                    break; //true > false, then skip the remaining ids since all is already set to true
                                }
                                // get the unique Rights instance for this music and category from RightsList
                                // set tmp boolean values to true if rights values are set to true
                                Rights rights = appModel.getRightsList().getByMusicIdAndCategoryId(music.getId(), categoryId);
                                if (rights.getMayReadInfo()) {
                                    mayReadInfo = true;
                                }
                                if (rights.getMayListen()) {
                                    mayListen = true;
                                }
                                if (rights.getMayNoteAndComment()) {
                                    mayNoteAndComment = true;
                                }
                            }
                        }
                        // if the peer is autorized to get the music
                        if (mayReadInfo) {
                            // using a new instance to set specific attributes
                            Music musicToReturn = music.clone();
                            // copying rights values
                            musicToReturn.setMayReadInfo(true); // useless... not used by other peers
                            musicToReturn.setMayListen(mayListen);
                            musicToReturn.setMayCommentAndNote(mayNoteAndComment);
                            // loading last used and known peer name
                            fillCommentAuthorNames(musicToReturn);
                            // add the music to the returned set of music
                            catalogResult.add(musicToReturn);
                        }
                    }
                }
            } else {
                // peer isn't a contact, check PUBLIC category only and associated rights values, as they may change like others
                /*
                 * 
                 * TODO : complete method
                 * 
                 */
            }
        }
        return catalogResult;
    }

    private void fillCommentAuthorNames(Music musicToReturn) {
        // use known peer list from app model to set each author name in the comments
        for (Comment comment : musicToReturn.getComments()) {
            musicToReturn.setCommentAuthor(comment.getIndex(), appModel.getKnownPeerList().getPeerNameById(comment.getAuthorPeerId()));
        }
    }

    private boolean musicMatchesSearchCriteria(Music music, SearchCriteria criteria) {
        String searchString = criteria.getSearch().toLowerCase();
        boolean match = music.getTitle().toLowerCase().contains(searchString)
                || music.getArtist().toLowerCase().contains(searchString)
                || music.getAlbum().toLowerCase().contains(searchString);
        if (match) {
            return true;
        }
        for (String tag : music.getTags()) {
            if (tag.toLowerCase().contains(searchString)) {
                return true;
            }
        }
        return false;
    }

    private List<Integer> getAllMatchingCategoryIds(Music music, Set<Integer> contactCategoryIds) {
        Set<Integer> musicCategories = music.getCategoryIds();
        List<Integer> matchingCategoryIds = new ArrayList<Integer>();
        for (Integer musicCategoryId : musicCategories) {
            if (contactCategoryIds.contains(musicCategoryId)) {
                matchingCategoryIds.add(musicCategoryId);
            }
        }
        return matchingCategoryIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void integrateMusicSearch(Catalog catalog) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog loadMusicFiles(Catalog catalog) {
        for (Music music : catalog.getMusics()) {
            Music modifiableMusicFromRequestCatalog = catalog.get(catalog.indexOf(music));
            Byte[] byteArray;
            try {
                byteArray = fileService.getFileAsByteArray(new File(".\\" + appModel.getProfile().getUserInfo().getLogin() + "\\" + music.getFileName()));
                modifiableMusicFromRequestCatalog.setFile(byteArray);
            } catch (IOException ex) {
                log.error(ex.toString());
            }
        }
        return catalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void installMusics(Catalog catalog) {
        log.warn("Not supported yet.");
    }

    private synchronized boolean isLocalTagMapDirty() {
        return localTagMapDirty;
    }

    private synchronized void buildLocalTagMap() {
        localTagMap = new TagMap(appModel.getLocalCatalog());
        localTagMapDirty = false;
    }
}

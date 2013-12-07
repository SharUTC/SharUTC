package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.ErrorMessage;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Comment;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.Rights;
import fr.utc.lo23.sharutc.model.domain.RightsList;
import fr.utc.lo23.sharutc.model.domain.Score;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.util.Utils;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
@Singleton
public class MusicServiceImpl implements MusicService {

    private static final Logger log = LoggerFactory.getLogger(MusicServiceImpl.class);
    private final AppModel appModel;
    private final UserService userService;
    private final FileService fileService;
    private TagMap localTagMap = null;
    protected boolean localTagMapDirty = true;

    /**
     * Constructor of MusicServiceImpl
     *
     * @param appModel The model of the application
     * @param userService The service of users
     * @param fileService The service of files
     */
    @Inject
    public MusicServiceImpl(AppModel appModel, UserService userService, FileService fileService) {
        this.appModel = appModel;
        this.userService = userService;
        this.fileService = fileService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToLocalCatalog(Collection<File> mp3Files) {
        log.debug("addToLocalCatalog ...");
        if (mp3Files == null) {
            throwMissingParameter();
        } else {
            Catalog localCatalog = appModel.getLocalCatalog();

            for (File currentFile : mp3Files) {
                Music musicFromFile = null;
                try {
                    musicFromFile = fileService.createMusicFromFile(currentFile);
                    String filename = fileService.computeFileName("", currentFile.getName());
                    musicFromFile.setFileName(filename);
                } catch (Exception ex) {
                    log.error(ex.toString());
                }
                if (musicFromFile != null) {
                    try {
                        byte[] bytes = fileService.getFileAsByteArray(currentFile);
                        fileService.createMusicFile(bytes, musicFromFile.getFileName(), appModel.getProfile().getUserInfo().getLogin());
                    } catch (Exception ex) {
                        log.error("Error while copying music file to user folder ({})", ex.toString());
                        throw new RuntimeException("Error while copying music file to user folder", ex);
                    }
                    musicFromFile.setFileBytes(null);
                    appModel.getRightsList().setRights(new Rights(Category.PUBLIC_CATEGORY_ID, musicFromFile.getId(),
                            Rights.DEFAULT_MAY_READ_INFO,
                            Rights.DEFAULT_LISTEN,
                            Rights.DEFAULT_MAY_NOTE_AND_COMMENT));
                    localCatalog.add(musicFromFile);
                    log.debug("addToLocalCatalog : music added to localCatalog");
                }
            }
        }
        log.debug("addToLocalCatalog DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromLocalCatalog(Collection<Music> musics) {
        log.debug("removeFromLocalCatalog ...");
        //TODO : clean RightList with removed music
        if (musics == null) {
            throwMissingParameter();
        } else {
            Catalog localCatalog = appModel.getLocalCatalog();

            for (Music currentMusic : musics) {
                if (localCatalog.contains(currentMusic)) {
                    for(String tag : currentMusic.getTags()){
                        this.removeTag(currentMusic, tag);
                    }
                    localCatalog.remove(currentMusic);
                } else {
                    log.warn("Music to delete not found !\n{}", currentMusic.getRealName());
                }
            }
        }
        log.debug("removeFromLocalCatalog DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void integrateRemoteCatalog(Peer peer, Catalog catalog) {
        log.debug("integrateRemoteCatalog ...");
        // TODO: check if peeer parameter is really needed here (for UI purposes?) refactor by deleting it if it's not the case
        appModel.getRemoteUserCatalog().addAll(catalog.getMusics());
        log.debug("integrateRemoteCatalog DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalogForPeer(Peer peer) {
        // TODO : refactor method to avoid redundant parts with searchMusic
        log.debug("getCatalogForPeer ...");

        Catalog userFilteredCatalog = new Catalog();

        if (peer == null) {
            throwMissingParameter();
        } else {
            Contact contact = userService.findContactByPeerId(peer.getId());
            if (contact != null) {
                Set<Integer> contactCategoryIds = contact.getCategoryIds();

                for (Music music : appModel.getLocalCatalog().getMusics()) {
                    Set<Integer> matchingCategoryIds = getAllMatchingCategoryIds(music, contactCategoryIds);

                    boolean mayReadInfo = false;
                    boolean mayListen = false;
                    boolean mayNoteAndComment = false;
                    if (!matchingCategoryIds.isEmpty()) {
                        if (matchingCategoryIds.size() == 1 && matchingCategoryIds.iterator().next().equals(Category.PUBLIC_CATEGORY_ID)) {
                            Rights rights = appModel.getRightsList().getByMusicIdAndCategoryId(music.getId(), Category.PUBLIC_CATEGORY_ID);
                            if (rights.getMayReadInfo()) {
                                mayReadInfo = true;
                            }
                            if (rights.getMayListen()) {
                                mayListen = true;
                            }
                            if (rights.getMayNoteAndComment()) {
                                mayNoteAndComment = true;
                            }
                        } else {
                            for (Integer categoryId : matchingCategoryIds) {
                                if (mayReadInfo && mayListen && mayNoteAndComment) {
                                    break;
                                }
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
                    }
                    if (mayReadInfo) {
                        Music musicToReturn = music.clone();
                        musicToReturn.setMayReadInfo(true);
                        musicToReturn.setMayListen(mayListen);
                        musicToReturn.setMayCommentAndNote(mayNoteAndComment);
                        fillCommentAuthorNames(musicToReturn);
                        musicToReturn.cleanForPreview();

                        userFilteredCatalog.add(musicToReturn);
                    }
                }
            } else {
                // peer isn't a contact, only check PUBLIC category and associated rights values, as they may change like others
                for (Music music : appModel.getLocalCatalog().getMusics()) {
                    Rights rights = appModel.getRightsList().getByMusicIdAndCategoryId(music.getId(), Category.PUBLIC_CATEGORY_ID);
                    if (rights != null) {
                        boolean mayReadInfo = false;
                        boolean mayListen = false;
                        boolean mayNoteAndComment = false;

                        if (rights.getMayReadInfo()) {
                            mayReadInfo = true;
                        }
                        if (rights.getMayListen()) {
                            mayListen = true;
                        }
                        if (rights.getMayNoteAndComment()) {
                            mayNoteAndComment = true;
                        }
                        if (mayReadInfo) {
                            Music musicToReturn = music.clone();
                            musicToReturn.setMayReadInfo(true);
                            musicToReturn.setMayListen(mayListen);
                            musicToReturn.setMayCommentAndNote(mayNoteAndComment);
                            fillCommentAuthorNames(musicToReturn);
                            musicToReturn.cleanForPreview();

                            userFilteredCatalog.add(musicToReturn);
                        }
                    } else {
                        log.warn("Wrong state of categories, peer isn't a contact and there should be a Rights for music ({}) and PUBLIC category", music.getRealName());
                    }
                }
            }
        }
        log.debug("getCatalogForPeer DONE");
        return userFilteredCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagMap getLocalTagMap() {
        log.debug("getLocalTagMap ...");
        if (localTagMap == null || isLocalTagMapDirty()) {
            buildLocalTagMap();
        }
        log.debug("getLocalTagMap DONE");
        return localTagMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void integrateRemoteTagMap(TagMap tagMap) {
        log.debug("integrateRemoteTagMap ...");
        if (tagMap == null) {
            throwMissingParameter();
        } else {
            appModel.getNetworkTagMap().merge(tagMap);
        }
        log.debug("integrateRemoteTagMap DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTag(Music music, String tag) {
        log.debug("addTag ...");
        if (music == null || tag == null || tag.trim().isEmpty()) {
            throwMissingParameter();
        } else {
            if (music.addTag(tag)) {
                localTagMapDirty = true;
            }
        }
        log.debug("addTag DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTag(Music music, String tag) {
        log.debug("removeTag ...");
        if (music == null || tag == null || tag.trim().isEmpty()) {
            throwMissingParameter();
        } else {
            if (music.removeTag(tag)) {
                localTagMapDirty = true;
            }
        }
        log.debug("removeTag DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComment(Peer peer, Music music, String comment) {
        log.debug("addComment ...");
        if (peer == null || music == null || comment == null || comment.trim().isEmpty()) {
            throwMissingParameter();
        } else {
            if (!comment.isEmpty()) {
                Comment myComment = new Comment(comment, peer.getId());
                myComment.setAuthorName(peer.getDisplayName());
                music.addComment(myComment);
            }
        }
        log.debug("addComment DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editComment(Peer peer, Music music, String comment, Integer commentIndex) {
        log.debug("editComment ...");
        if (peer == null || music == null || comment == null || comment.trim().isEmpty() || commentIndex == null) {
            throwMissingParameter();
        } else {
            Comment commentToEdit = music.getComment(peer, commentIndex);
            if (commentToEdit != null) {
                commentToEdit.setText(comment);
            } else {
                log.warn("editComment : Comment to edit not found");
            }
        }
        log.debug("editComment DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComment(Peer peer, Music music, Integer commentIndex) {
        log.debug("removeComment ...");
        if (peer == null || music == null || commentIndex == null) {
            throwMissingParameter();
        } else {
            Comment myComment = music.getComment(peer, commentIndex);
            if (myComment != null) {
                music.removeComment(myComment);
            } else {
                log.warn("removeComment : Comment to remove not found");
            }
        }
        log.debug("removeComment DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScore(Peer peer, Music music, Integer score) {
        log.debug("setScore ...");
        if (peer == null || music == null) {
            throwMissingParameter();
        } else {
            if (score == null) {
                unsetScore(peer, music);
            } else if (score >= Score.MIN_VALUE && score <= Score.MAX_VALUE) {
                Score musicScore = music.getScore(peer);
                if (musicScore != null) {
                    // peer already scored the music
                    musicScore.setValue(score);
                    log.debug("setScore : update score value");
                } else {
                    log.debug("setScore : add score");
                    musicScore = new Score(score, peer.getId());
                    music.addScore(musicScore);
                }
            }
        }
        log.debug("setScore DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsetScore(Peer peer, Music music) {
        log.debug("unsetScore ...");
        if (peer == null || music == null) {
            throwMissingParameter();
        } else {
            Score musicScore = music.getScore(peer);
            if (musicScore != null) {
                music.removeScore(musicScore);
            }
        }
        log.debug("unsetScore DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveUserMusicFile() {
        log.debug("saveUserMusicFile ...");
        Catalog localCatalog = appModel.getLocalCatalog();
        if (localCatalog != null) {
            fileService.saveToFile(SharUTCFile.CATALOG, localCatalog);
        } else {
            log.warn("Can't save current music Catalog(null)");
        }
        log.debug("saveUserMusicFile DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveUserRightsListFile() {
        log.trace("saveUserRightsListFiles ...");
        RightsList rightsList = appModel.getRightsList();
        if (rightsList != null) {
            fileService.saveToFile(SharUTCFile.RIGHTSLIST, rightsList);
        } else {
            log.warn("Can't save current rightsList (null)");
        }
        log.trace("saveUserRightsListFiles DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadUserRightsListFile() {
        log.debug("loadUserRightsListFiles ...");
        RightsList rightsList = fileService.readFile(SharUTCFile.RIGHTSLIST, RightsList.class);
        if (rightsList != null) {
            appModel.setRightsList(rightsList);
        } else {
            log.warn("loadUserRightsListFiles : no rightsList loaded");
        }
        log.debug("loadUserRightsListFiles DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadUserMusicFile() {
        log.debug("loadUserMusicFile ...");

        Catalog catalog = fileService.readFile(SharUTCFile.CATALOG, Catalog.class);
        if (catalog != null) {
            appModel.setLocalCatalog(catalog);
        } else {
            log.warn("loadUserMusicFiles : no catalog loaded");
        }

        log.debug("loadUserMusicFile DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog searchMusic(Peer peer, SearchCriteria criteria) {
        log.debug("searchMusic ... ({} : {})",
                peer != null ? peer.getDisplayName() : "",
                criteria != null ? criteria.getSearch() : "");
        Catalog catalogResult = null;
        if (peer == null || criteria == null) {
            throwMissingParameter();
        } else {
            /*
             * Searching algorithm :
             * # Recovery of the categories associated in contact which asks
             * # For each music of the catalog
             * #    Recovery of the categories associated in this music
             * #    Filtering of both sets of IDs
             * #        For each remaining category :
             * #            If public (0, exclusive) : check of the rights for this music and this category in RightsList
             * #            If other(s) : load of all the available rights for this music and this category using RightsList as long as a value "true" is not found
             */

            catalogResult = new Catalog();
            if (criteria.getSearch() != null && criteria.getSearch().trim().length() > 0) {
                Contact contact = userService.findContactByPeerId(peer.getId());
                if (contact != null) {
                    Set<Integer> contactCategoryIds = contact.getCategoryIds();

                    // looping on whole catalog, searching for matching music informations
                    for (Music music : appModel.getLocalCatalog().getMusics()) {
                        // only deal with needed musics
                        if (musicMatchesSearchCriteria(music, criteria)) {
                            //Pour l'autre fonction --> addtoCatalog
                            // searching for useful categories only
                            Set<Integer> matchingCategoryIds = getAllMatchingCategoryIds(music, contactCategoryIds);

                            // searching Rights values to be set directly on a copy of music instance if added to the results
                            boolean mayReadInfo = false;
                            boolean mayListen = false;
                            boolean mayNoteAndComment = false;
                            if (!matchingCategoryIds.isEmpty()) {
                                if (matchingCategoryIds.size() == 1 && matchingCategoryIds.iterator().next().equals(Category.PUBLIC_CATEGORY_ID)) {
                                    Rights rights = appModel.getRightsList().getByMusicIdAndCategoryId(music.getId(), Category.PUBLIC_CATEGORY_ID);
                                    if (rights.getMayReadInfo()) {
                                        mayReadInfo = true;
                                    }
                                    if (rights.getMayListen()) {
                                        mayListen = true;
                                    }
                                    if (rights.getMayNoteAndComment()) {
                                        mayNoteAndComment = true;
                                    }
                                } else {
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
                                musicToReturn.cleanForPreview();
                                // add the music to the returned set of music
                                catalogResult.add(musicToReturn);
                            }
                        }
                    }
                } else {
                    // peer isn't a contact, only check PUBLIC category and associated rights values, as they may change like others
                    for (Music music : appModel.getLocalCatalog().getMusics()) {
                        Rights rights = appModel.getRightsList().getByMusicIdAndCategoryId(music.getId(), Category.PUBLIC_CATEGORY_ID);
                        if (rights != null) {
                            // only deal with needed musics
                            if (musicMatchesSearchCriteria(music, criteria)) {
                                // searching Rights values to be set directly on a copy of music instance if added to the results
                                boolean mayReadInfo = false;
                                boolean mayListen = false;
                                boolean mayNoteAndComment = false;

                                if (rights.getMayReadInfo()) {
                                    mayReadInfo = true;
                                }
                                if (rights.getMayListen()) {
                                    mayListen = true;
                                }
                                if (rights.getMayNoteAndComment()) {
                                    mayNoteAndComment = true;
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
                                    musicToReturn.cleanForPreview();
                                    // add the music to the returned set of music
                                    catalogResult.add(musicToReturn);
                                }
                            }
                        } else {
                            log.warn("Wrong state of categories, peer isn't a contact and there should be a Rights for music ({}) and PUBLIC category", music.getRealName());
                        }
                    }
                }
            }
        }
        log.debug("searchMusic DONE");
        return catalogResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog searchLocalMusic(SearchCriteria criteria) {
        log.debug("searchLocalMusic ... ({} : {})",
                criteria != null ? criteria.getSearch() : "");
        Catalog catalogResult = null;
        if (criteria == null) {
            throwMissingParameter();
        } else {
            catalogResult = new Catalog();
            if (criteria.getSearch() != null && criteria.getSearch().trim().length() > 0) {
                // looping on whole catalog, searching for matching music informations
                for (Music music : appModel.getLocalCatalog().getMusics()) {
                    // only deal with needed musics
                    if (musicMatchesSearchCriteria(music, criteria)) {
                        //Pour l'autre fonction --> addtoCatalog
                        catalogResult.add(music.clone());
                    }
                }
            }
        }
        log.debug("searchLocalMusic DONE");
        return catalogResult;
    }

    /**
     * {@inheritDoc}
     */
    private void fillCommentAuthorNames(Music musicToReturn) {
        // use known peer list from profile to set each author name in the comments
        for (Comment comment : musicToReturn.getComments()) {
            Long authorPeerId = comment.getAuthorPeerId();
            String author;
            if (appModel.getProfile().getUserInfo().getPeerId().equals(authorPeerId)) {
                author = new StringBuilder(appModel.getProfile().getUserInfo().getFirstName()).append(" ").append(appModel.getProfile().getUserInfo().getLastName()).toString();
            } else {
                author = appModel.getProfile().getKnownPeerList().getPeerNameById(comment.getAuthorPeerId());
            }
            comment.setAuthorName(author);
            //musicToReturn.setCommentAuthor(comment.getIndex(), author);
        }
    }

    /**
     * {@inheritDoc}
     */
    private boolean musicMatchesSearchCriteria(Music music, SearchCriteria criteria) {
        String searchString = criteria.getSearch().toLowerCase();
        boolean match = music.getTitle().toLowerCase().contains(searchString)
                || music.getArtist().toLowerCase().contains(searchString)
                || music.getAlbum().toLowerCase().contains(searchString);
        if (!match) {
            for (String tag : music.getTags()) {
                if (tag.toLowerCase().contains(searchString)) {
                    match = true;
                    break;
                }
            }
        }
        return match;
    }

    /**
     * {@inheritDoc}
     */
    private Set<Integer> getAllMatchingCategoryIds(Music music, Set<Integer> contactCategoryIds) {
        Set<Integer> matchingCategories = new HashSet<Integer>(Math.min(contactCategoryIds.size(), music.getCategoryIds().size()));
        if (contactCategoryIds.size() < music.getCategoryIds().size()) {
            matchingCategories.addAll(contactCategoryIds);
            matchingCategories.retainAll(music.getCategoryIds());
        } else {
            matchingCategories.addAll(music.getCategoryIds());
            matchingCategories.retainAll(contactCategoryIds);
        }
        return matchingCategories;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void integrateMusicSearch(Catalog catalog) {
        if (catalog == null) {
            throwMissingParameter();
        } else {
            appModel.getSearchResults().merge(catalog);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadMusicFiles(Catalog catalog) {
        log.debug("loadMusicFiles ...");
        if (catalog == null) {
            throwMissingParameter();
        } else {
            for (int i = 0; i < catalog.size(); i++) {
                loadMusicFile(catalog.get(i));
            }
        }
        log.debug("loadMusicFiles DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadMusicFile(Music music) {
        log.debug("loadMusicFile ...");
        if (music == null) {
            throwMissingParameter();
        } else {
            byte[] byteArray;
            try {
                byteArray = fileService.getFileAsByteArray(new File(fileService.getAppFolder() + FileService.ROOT_FOLDER_USERS + File.separator + appModel.getProfile().getUserInfo().getLogin() + File.separator + FileService.FOLDER_MUSICS + File.separator + music.getFileName()));
                Byte[] bytes = new Byte[byteArray.length];
                for (int i = 0; i < byteArray.length; i++) {
                    bytes[i] = byteArray[i];
                }
                music.setFileBytes(bytes);
            } catch (IOException ex) {
                log.error(ex.toString());
            }
        }
        log.debug("loadMusicFile DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void installMusics(Catalog catalog) {
        if (catalog == null) {
            throwMissingParameter();
        } else {
            if (!catalog.isEmpty()) {
                List<File> tmpFiles = null;
                try {
                    tmpFiles = fileService.buildTmpMusicFilesForInstall(catalog);
                } catch (Exception ex) {
                    log.error(ex.toString());
                }
                if (tmpFiles != null && !tmpFiles.isEmpty()) {
                    addToLocalCatalog(tmpFiles);
                    for (File tmpFile : tmpFiles) {
                        tmpFile.delete();
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    private synchronized boolean isLocalTagMapDirty() {
        return localTagMapDirty;
    }

    /**
     * {@inheritDoc}
     */
    private synchronized void buildLocalTagMap() {
        localTagMap = new TagMap(appModel.getLocalCatalog());
        localTagMapDirty = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAndSetCatalog() {
        log.debug("createAndSetCatalog ...");

        appModel.setLocalCatalog(new Catalog());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAndSetRightsList() {
        log.debug("createAndSetRightsList ...");
        appModel.setRightsList(new RightsList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMusicFileName(Music music, String name) {
        log.debug("updateMusicFileName ...");
        if (music == null || name == null) {
            throwMissingParameter();
        } else {
            Music localMusic = appModel.getLocalCatalog().findMusicById(music.getId());
            /*
             * we need to check if musicId is found, and also need to check the owner id else client
             * might be trying to rename a music that doesn't belongs to him
             */
            if (localMusic != null && localMusic.getOwnerPeerId().equals(music.getOwnerPeerId())) {
                String filename = null;
                String realname = null;
                name = name.trim();
                if (name.length() < FileService.MIN_FILENAME_LENGTH) {
                    realname = fileService.computeRealName(name);
                    if (!localMusic.getRealName().equals(realname)) {
                        // user changed the name of the file, but another file may already use it,
                        // we allow the change, we have to :
                        // check if the value is different from previous (realname)
                        // check if other files are using the same "realname"
                        //  if no other, then let the name as is
                        //  if other(s) exist, find next x following :  filename (x).mp3
                        filename = fileService.computeFileName(music.getRealName(), realname);
                    }
                }
                if (realname != null && filename != null) {
                    File file = fileService.getFileOfLocalMusic(localMusic);

                    String oldMusicFilePath = null;
                    try {
                        oldMusicFilePath = file.getCanonicalPath();
                    } catch (IOException ex) {
                        log.error(ex.toString());
                    }
                    if (oldMusicFilePath != null) {
                        String newMusicFilePath = oldMusicFilePath.replaceAll(localMusic.getFileName(), filename);
                        log.info("Renaming file from '{}' to '{}'", localMusic.getFileName(), filename);
                        localMusic.setFileName(filename);
                        log.info("Moving file from '{}' to '{}'", oldMusicFilePath, newMusicFilePath);
                        file.renameTo(new File(newMusicFilePath));
                        log.info("Setting realName to '{}'", realname);
                        localMusic.setRealName(realname);
                    }
                }
                log.info("Saving music update");
                saveUserMusicFile();
            }
        }
        log.debug("updateMusicFileName DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveMusicFieldChanges(Music music, String title, String artist, String album, String track, String year) {
        log.debug("saveMusicFieldChanges ...");
        if (music == null) {
            throwMissingParameter();
        } else {
            Music localMusic = appModel.getLocalCatalog().findMusicById(music.getId());
            /*
             * we need to check if musicId is found, and also need to check the owner id else client
             * might be trying to change a music that doesn't belongs to him
             */ if (localMusic != null && localMusic.getOwnerPeerId().equals(music.getOwnerPeerId())) {
                boolean atLeastOneId3TagHasChanged = false;
                if (title != null && localMusic.getTitle() != null && !localMusic.getTitle().equals(title)
                        || artist != null && localMusic.getArtist() != null && !localMusic.getArtist().equals(artist)
                        || album != null && localMusic.getAlbum() != null && !localMusic.getAlbum().equals(album)
                        || track != null && localMusic.getTrack() != null && !localMusic.getTrack().equals(track)
                        || year != null && localMusic.getYear() != null && !localMusic.getYear().equals(year)) {
                    atLeastOneId3TagHasChanged = true;
                }

                if (atLeastOneId3TagHasChanged) {
                    File file = fileService.getFileOfLocalMusic(localMusic);
                    if (file != null) {
                        if (atLeastOneId3TagHasChanged) {
                            try {
                                AudioFile audioFile = AudioFileIO.read(file);
                                Tag tag = audioFile.getTag();
                                if (title != null) {
                                    tag.setField(FieldKey.TITLE, title);
                                    localMusic.setTitle(title);
                                }
                                if (artist != null) {
                                    tag.setField(FieldKey.ARTIST, artist);
                                    localMusic.setArtist(artist);
                                }
                                if (album != null) {
                                    tag.setField(FieldKey.ALBUM, album);
                                    localMusic.setAlbum(album);
                                }
                                if (track != null) {
                                    tag.setField(FieldKey.TRACK, track);
                                    localMusic.setTrack(track);
                                }
                                if (year != null) {
                                    tag.setField(FieldKey.YEAR, year);
                                    localMusic.setYear(year);
                                }
                                audioFile.setTag(tag);
                                audioFile.commit();
                            } catch (Exception ex) {
                                log.error("Unable to write music file informations : {}", ex.toString());
                            }
                        }
                    }
                    log.info("Saving music field(s) update");
                    saveUserMusicFile();
                }
            }
        }
        log.debug("saveMusicFieldChanges DONE");
    }

    /**
     * {@inheritDoc}
     */
    private void throwMissingParameter() {
        Utils.throwMissingParameter(log, new Throwable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMusicToCategory(Music music, Category category) {

        //Check that the music does not exist in this category
        if (!music.getCategoryIds().contains(category.getId())) {
            appModel.getLocalCatalog().findMusicById(music.getId()).addCategoryId(category.getId());
        } else {
            log.warn("This music already exists in this category");
            ErrorMessage nErrorMessage = new ErrorMessage("This music already exists in this category");
            appModel.getErrorBus().pushErrorMessage(nErrorMessage);
        }
        appModel.getRightsList().setRights(new Rights(category.getId(), music.getId(), localTagMapDirty, localTagMapDirty, localTagMapDirty));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMusicFromCategory(Music music, Category category) {
        Music m = appModel.getLocalCatalog().findMusicById(music.getId());
        m.removeCategoryId(category.getId());
        // if this category was the only one, we put the music in the public one
        /*
         * if (m.getCategoryIds().isEmpty()) {
         *  m.addCategoryId(Category.PUBLIC_CATEGORY_ID);
         * }
         */
        appModel.getRightsList().remove(appModel.getRightsList().getByMusicIdAndCategoryId(music.getId(), category.getId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromKnownPeersIfUseless(Peer peer) {
        boolean keepPeer = false;
        if (appModel.getProfile() != null && appModel.getProfile().getContacts() != null && !appModel.getProfile().getContacts().isEmpty()) {
            for (Contact contact : appModel.getProfile().getContacts().getContacts()) {
                if (contact.getUserInfo().getPeerId().equals(peer.getId())) {
                    keepPeer = true;
                    break;
                }
            }
        }
        if (!keepPeer) {
            if (appModel.getLocalCatalog() != null && !appModel.getLocalCatalog().isEmpty()) {
                for (Music m : appModel.getLocalCatalog().getMusics()) {
                    if (m.getScores() != null && !m.getScores().isEmpty()) {
                        for (Score score : m.getScores()) {
                            if (score.getPeerId().equals(peer.getId())) {
                                keepPeer = true;
                                break;
                            }
                        }
                    }
                    if (keepPeer) {
                        break;
                    }
                    if (m.getComments() != null && !m.getComments().isEmpty()) {
                        for (Comment comment : m.getComments()) {
                            if (comment.getAuthorPeerId().equals(peer.getId())) {
                                keepPeer = true;
                                break;
                            }
                        }
                    }
                    if (keepPeer) {
                        break;
                    }
                    if (m.getScores() != null && !m.getScores().isEmpty()) {
                        for (Score score : m.getScores()) {
                            if (score.getPeerId().equals(peer.getId())) {
                                keepPeer = true;
                                break;
                            }
                        }
                    }
                    if (keepPeer) {
                        break;
                    }
                }
            }
        }
        if (!keepPeer) {
            appModel.getProfile().getKnownPeerList().remove(peer);
            userService.saveProfileFiles();
        }
    }
}

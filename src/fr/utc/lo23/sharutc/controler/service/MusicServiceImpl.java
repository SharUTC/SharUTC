package fr.utc.lo23.sharutc.controler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Comment;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.Rights;
import fr.utc.lo23.sharutc.model.domain.RightsList;
import fr.utc.lo23.sharutc.model.domain.Score;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.util.Utils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
    private boolean localTagMapDirty = true;
    private static final String dataPath = "";

    /**
     * {@inheritDoc}
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
        log.trace("addToLocalCatalog ...");
        if (mp3Files == null) {
            throwMissingParameter();
        } else {
            Catalog localCatalog = appModel.getLocalCatalog();

            for (File currentFile : mp3Files) {
                Music musicFromFile = null;
                try {
                    musicFromFile = fileService.readFile(currentFile);
                    String filename = fileService.computeFileName("", currentFile.getName());
                    musicFromFile.setFileName(filename);
                } catch (Exception ex) {
                    log.error(ex.toString());
                }
                if (musicFromFile != null) {
                    if (localCatalog.contains(musicFromFile)) {
                        log.warn("Skipping add of already existing music (hash equals) :\n{}\n{}",
                                localCatalog.findMusicByHash(musicFromFile.getHash()).getRealName(),
                                musicFromFile.getRealName());
                    } else {
                        localCatalog.add(musicFromFile);
                    }
                }
            }
        }
        log.trace("addToLocalCatalog DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromLocalCatalog(Collection<Music> musics) {
        log.trace("removeFromLocalCatalog ...");
        if (musics == null) {
            throwMissingParameter();
        } else {
            Catalog localCatalog = appModel.getLocalCatalog();

            for (Music currentMusic : musics) {
                if (localCatalog.contains(currentMusic)) {
                    localCatalog.remove(currentMusic);
                } else {
                    log.warn("Music to delete not found !\n{}", currentMusic.getRealName());
                }
            }
        }
        log.trace("removeFromLocalCatalog DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void integrateRemoteCatalog(Peer peer, Catalog catalog) {
        
            appModel.getRemoteUserCatalog().clear();
        
            // 2 modes : when peer is a contact and when peer isn't a contact
            Long contactId = userService.findContactIdByPeerId(peer.getId());
            if (contactId != null) {
                Set<Integer> contactCategoryIds = appModel.getProfile().getCategories().getCategoriesIdsByContactId(contactId);


                // looping on whole catalog, searching for matching music informations
                for (Music music : appModel.getLocalCatalog().getMusics()) {
                    // only deal with needed musics
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
                            appModel.getRemoteUserCatalog().add(musicToReturn);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public TagMap getLocalTagMap() {
        log.trace("getLocalTagMap ...");
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
        log.trace("integrateRemoteTagMap ...");
        if (tagMap == null) {
            throwMissingParameter();
        } else {
            appModel.getNetworkTagMap().merge(tagMap);
        }
        log.trace("integrateRemoteTagMap DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTag(Music music, String tag) {
        log.trace("addTag ...");
        if (music == null || tag == null || tag.trim().isEmpty()) {
            throwMissingParameter();
        } else {
            if (music.addTag(tag)) {
                localTagMapDirty = true;
            }
        }
        log.trace("addTag DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTag(Music music, String tag) {
        log.trace("removeTag ...");
        if (music == null || tag == null || tag.trim().isEmpty()) {
            throwMissingParameter();
        } else {
            if (music.removeTag(tag)) {
                localTagMapDirty = true;
            }
        }
        log.trace("removeTag DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComment(Peer peer, Music music, String comment) {
        log.trace("addComment ...");
        if (peer == null || music == null || comment == null || comment.trim().isEmpty()) {
            throwMissingParameter();
        } else {
            int nextIndex = 0;
            for (Comment c : music.getComments()) {
                if (c.getIndex() > nextIndex) {
                    nextIndex = c.getIndex() + 1;
                }
            }

            if (!comment.isEmpty()) {
                Comment myComment = new Comment();
                myComment.setAuthorPeerId(peer.getId());
                // loaded at display time only, not set at creation
                //myComment.setAuthorName(peer.getDisplayName());
                myComment.setText(comment);
                myComment.setIndex(nextIndex);
                myComment.setCreationDate(new Date());
                music.addComment(myComment);
            }
        }
        log.trace("addComment DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editComment(Peer peer, Music music, String commentText, Integer commentIndex) {
        log.trace("editComment ...");
        if (peer == null || music == null || commentText == null || commentText.trim().isEmpty() || commentIndex == null) {
            throwMissingParameter();
        } else {
            Comment commentToEdit = music.getComment(peer, commentIndex);
            if (commentToEdit != null) {
                commentToEdit.setText(commentText);
            } else {
                log.warn("editComment : Comment to edit not found");
            }
        }
        log.trace("editComment DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComment(Peer peer, Music music, Integer commentIndex) {
        log.trace("removeComment ...");
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
        log.trace("removeComment DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScore(Peer peer, Music music, Integer score) {
        log.trace("setScore ...");
        if (peer == null || music == null) {
            throwMissingParameter();
        } else {
            if (score == null || score.intValue() == Score.MIN_VALUE) {
                unsetScore(peer, music);
            } else if (score > Score.MIN_VALUE && score <= Score.MAX_VALUE) {
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
        log.trace("setScore DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsetScore(Peer peer, Music music) {
        log.trace("unsetScore ...");
        if (peer == null || music == null) {
            throwMissingParameter();
        } else {
            Score musicScore = music.getScore(peer);
            if (musicScore != null) {
                music.removeScore(musicScore);
            }
        }
        log.trace("unsetScore DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveUserMusicFile() {
        log.trace("saveUserMusicFiles ...");
        Catalog localCatalog = appModel.getLocalCatalog();
        if (localCatalog != null) {
            fileService.saveToFile(SharUTCFile.CATALOG, localCatalog);
        } else {
            log.warn("Can't save current music Catalog(null)");
        }
        log.trace("saveUserMusicFiles DONE");
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
        log.trace("loadUserRightsListFiles ...");
        RightsList rightsList = fileService.readFile(SharUTCFile.RIGHTSLIST, RightsList.class);
        if (rightsList != null) {
            appModel.setRightsList(rightsList);
        } else {
            log.warn("loadUserRightsListFiles : no rightsList loaded");
        }
        log.trace("loadUserRightsListFiles DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadUserMusicFile() {
        log.trace("loadUserMusicFiles ...");

        Catalog catalog = fileService.readFile(SharUTCFile.CATALOG, Catalog.class);
        if (catalog != null) {
            appModel.setLocalCatalog(catalog);
        } else {
            log.warn("loadUserMusicFiles : no catalog loaded");
        }

        log.trace("loadUserMusicFiles DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog searchMusic(Peer peer, SearchCriteria criteria) {
        log.trace("searchMusic ... ({} : {})",
                peer != null ? peer.getDisplayName() : "",
                criteria != null ? criteria.getSearch() : "");
        Catalog catalogResult = null;
        if (peer == null || criteria == null) {
            throwMissingParameter();
        } else {
            catalogResult = new Catalog();
            if (criteria.getSearch() != null && criteria.getSearch().trim().length() > 0) {
                Long contactId = userService.findContactIdByPeerId(peer.getId());
                if (contactId != null) {
                    Set<Integer> contactCategoryIds = appModel.getProfile().getCategories().getCategoriesIdsByContactId(contactId);

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
                            if (!matchingCategoryIds.isEmpty()) {
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
        }
        log.trace("searchMusic DONE");
        return catalogResult;
    }

    private void fillCommentAuthorNames(Music musicToReturn) {
        // use known peer list from profile to set each author name in the comments
        for (Comment comment : musicToReturn.getComments()) {
            musicToReturn.setCommentAuthor(comment.getIndex(), appModel.getProfile().getKnownPeerList().getPeerNameById(comment.getAuthorPeerId()));
        }
    }

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
        if (catalog == null) {
            throwMissingParameter();
        } else {
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadMusicFiles(Catalog catalog) {
        log.trace("loadMusicFiles ...");
        if (catalog == null) {
            throwMissingParameter();
        } else {
            for (int i = 0; i < catalog.size(); i++) {
                loadMusicFile(catalog.get(i));
            }
        }
        log.trace("loadMusicFiles DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadMusicFile(Music music) {
        log.trace("loadMusicFile ...");
        if (music == null) {
            throwMissingParameter();
        } else {
            Byte[] byteArray;
            try {
                byteArray = fileService.getFileAsByteArray(new File(".\\" + appModel.getProfile().getUserInfo().getLogin() + "\\" + music.getFileName()));
                music.setFileByte(byteArray);
            } catch (IOException ex) {
                log.error(ex.toString());
            }
        }
        log.trace("loadMusicFile DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void installMusics(Catalog catalog) {
        log.warn("Not supported yet.");
        if (catalog == null) {
            throwMissingParameter();
        } else {
        }
    }

    private synchronized boolean isLocalTagMapDirty() {
        return localTagMapDirty;
    }

    private synchronized void buildLocalTagMap() {
        localTagMap = new TagMap(appModel.getLocalCatalog());
        localTagMapDirty = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAndSetCatalog() {
        appModel.setLocalCatalog(new Catalog());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAndSetRightsList() {
        appModel.setRightsList(new RightsList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMusicFileName(Music music, String name) {
        log.trace("updateMusicFileName ...");
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
        log.trace("updateMusicFileName DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveMusicFieldChanges(Music music, String title, String artist, String album, String track, String year) {
        log.trace("saveMusicFieldChanges ...");
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
        log.trace("saveMusicFieldChanges DONE");
    }

    private void throwMissingParameter() {
        Utils.throwMissingParameter(log, new Throwable());
    }
}

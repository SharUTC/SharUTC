package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Categories;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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
    public void addToLocalCatalog(List<File> mp3Files) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromLocalCatalog(List<Music> musics) {
        log.warn("Not supported yet.");
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
        return new TagMap(appModel.getLocalCatalog());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void integrateRemoteTagMap(TagMap tagMap) {
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTag(Music music, String tag) {
        if (tag != null && tag.trim().length() > 0) {
            tag = tag.toLowerCase();
            tag = tag.substring(0, 1).toUpperCase() + (tag.length() > 1 ? tag.substring(1) : "");
            music.addTag(tag);
        } else {
            log.warn("Using null or empty tag name, skipping it");
        }
        log.warn("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTag(Music music, String tag) {
        if (tag != null && tag.trim().length() > 0) {
            tag = tag.toLowerCase();
            tag = tag.substring(0, 1).toUpperCase() + (tag.length() > 1 ? tag.substring(1) : "");
            music.removeTag(tag);
        } else {
            log.warn("Using null or empty tag name, skipping it");
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
        Catalog searchResults = new Catalog();

        /*
         * DO NOT SET RIGHTS DIRECTLY ON MUSIC CONTAINED IN THE CATALOG
         * use a copy of each instead
         */

        Contact contact = null;//appModel.getProfile().getContacts().findById(peer.getId());

        if (contact != null) {
            Categories contactCategories = contact.getCategories();
            // use categories to filter :
            // loop each music in the local catalog, see music.getCategoryIds and check if the peer has at least one of them
            // -> one music may be linked to several categories, we must loop on all the results unless we find that all right values are set to true
            // if at least one "mayReadInfo" is set to true, then first make a copy of the music instance (add a clone method to Music class)
            // then use RightsList to load the rights for this music and category (set directly the music attribute on the copy)
            for (Music music : appModel.getLocalCatalog().getMusics()) {
                List<Integer> musicCategories = music.getCategoryIds();
                List<Integer> matchingCategoryIds = new ArrayList<Integer>();

                // ...  

                if (matchingCategoryIds.isEmpty()) {
                    // check true/false, use tmp boolean values in a 
                    // small loop to check true/false values
                }
                // if at least one "mayReadInfo" ...
                //searchResults.add(copyOfMusic);

            }
        } else {
            // simply check for each music in the catalog if the categorie is 0 (=PUBLIC, which one is exclusive) and if the music for this category has readInfo right = true
            // we still have to copy each PUBLIC right value since user may also change rights on PUBLIC category
            // if readInfo = true then searchResults.add(copyOfMusic);
        }
        log.warn("Not supported yet.");
        return searchResults;
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
}
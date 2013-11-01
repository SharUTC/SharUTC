package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.RightsList;
import fr.utc.lo23.sharutc.model.domain.Rights;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Categories;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.io.File;
import java.io.IOException;
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
         
        if (criteria.getSearch() != null && criteria.getSearch().trim().length() >0 ) {
        Catalog catalogResult=new Catalog();
        Catalog catalogLocal= appModel.getLocalCatalog();
        
        //Arnaud's part
        Contact contact = appModel.getProfile().getContacts().findById(peer.getId());
       // RightsList listRights = appModel.getRightsList();
        
        if (contact!=null){
            Categories contactCategories = contact.getCategories();
             for (Music music : catalogLocal.getMusics()) {
               // List<Integer> musicCategories = music.getCategoryIds();
               // List<Integer> matchingCategoryIds = new ArrayList<Integer>();

                // ...  

              //  if (matchingCategoryIds.isEmpty()) {
                    // check true/false, use tmp boolean values in a 
                    // small loop to check true/false values
              //  }
                // if at least one "mayReadInfo" ...
                //searchResults.add(copyOfMusic);
             }
        }
        
        // Amandine's part
        for (Music music : catalogLocal.getMusics()) {
        //Vérifier les droits avant de vérifier le reste
        //Récupère les droits, et je les copies dans le champs Musics.
            for(Rights rights : appModel.getRightsList().getRightsList()) {
                if(rights.getMusicId()==music.getId()){ 
                    if (rights.getMayReadInfo()==true) {
                       //Search based on title, album, artist
                        if (music.getTitle().contains(criteria.getSearch()) || music.getArtist().contains(criteria.getSearch()) ||  music.getAlbum().contains(criteria.getSearch())) {
                        catalogResult.add(music);
                        }
                     } 
                }
            }
        }
                   
      return catalogResult;
        }
     return null;
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
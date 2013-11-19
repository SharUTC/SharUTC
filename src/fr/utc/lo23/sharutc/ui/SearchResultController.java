package fr.utc.lo23.sharutc.ui;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.search.MusicSearchCommand;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelImpl;
import fr.utc.lo23.sharutc.model.ErrorBus;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.card.SimpleCard;
import fr.utc.lo23.sharutc.ui.custom.CardList;
import fr.utc.lo23.sharutc.ui.custom.card.AlbumCard;
import fr.utc.lo23.sharutc.ui.custom.card.ArtistCard;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import fr.utc.lo23.sharutc.ui.custom.card.UserCard;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchResultController extends SongSelectorController implements CollectionChangeListener ,Initializable,AlbumCard.IAlbumCard, UserCard.IUserCard, ArtistCard.IArtistCard {
    
    private static final Logger log = LoggerFactory
            .getLogger(SearchResultController.class);
    
    public VBox gridpane;
    private String search;
    private CardList songList;
    private CardList friendList;
    private CardList artistList;
    private CardList albumList;
    private ISearchResultController mInterface;

   
    @Inject
    private GuiceFXMLLoader mFxmlLoader;
    @Inject
    private AppModel mAppModel;
    
    @Inject
    private MusicSearchCommand mMusicSearchCommand;
        
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        
        
        //listen for changes on the AppModel
        mAppModel.getSearchResults().addPropertyChangeListener(this);

 
        
        
    
        songList = new CardList  ("Songs", "bgBlue");
        friendList = new CardList("Friends", "bgGreen");
        artistList = new CardList("Artists", "bgRed");
        albumList = new CardList ("Albums", "bgOrange");
        
        gridpane.getChildren().add(songList);
        gridpane.getChildren().add(friendList);
        gridpane.getChildren().add(artistList);
        gridpane.getChildren().add(albumList);
        
        
        SearchCriteria critera = new SearchCriteria(search);
        mMusicSearchCommand.setSearchCriteria(critera);
       
         if (resourceBundle != null) {
            search = resourceBundle.getString("search");
        } else {
            search = "";
        }
        
        
        UserInfo u = new UserInfo();
        u.setFirstName("bob");
        addChild(new UserCard(u, this));
        addChild(new UserCard(u, this));
        addChild(new UserCard(u, this));
        addChild(new UserCard(u, this));
        addChild(new UserCard(u, this));
        addChild(new UserCard(u, this));
        
        final Music m = new Music();
            m.setFileName("Music ");
            m.setAlbum("Album");
            m.setArtist("Artist");
        SongCard newCard = new SongCard(m, this, true);
       
        addChild(newCard);
        
        SimpleCard card = new ArtistCard(m, this);
        this.addChild(card);
        
        card = new AlbumCard(m, this);
        this.addChild(card);
        
    }
    
    public void setInterface(ISearchResultController i){
        this.mInterface = i;
    }
    
    public void addChild(SimpleCard card){
        if(card instanceof UserCard){
            friendList.addChild(card);
            
        }else if(card instanceof SongCard){
            songList.addChild(card);
        }else if(card instanceof ArtistCard){
            artistList.addChild(card);
        }else if(card instanceof AlbumCard){
            albumList.addChild(card);
        }
    }

    @Override
    public void onPeopleDetailsRequested(UserInfo userInfo) {
        mInterface.onPeopleDetailRequested(userInfo);
    }

 

    @Override
    public void onArtistDetailRequested(Music music) {
        mInterface.onArtistDetailRequested(music);
    }
    
     @Override
    public void onAlbumDetailRequested(Music music) {
         mInterface.onAlbumDetailRequested(music);
    }

    @Override
    public void collectionChanged(CollectionEvent ev) {
        switch(ev.getType()){
            case ADD:
                Music m = ((Music)ev.getSource());
                if(m.getAlbum().contains(search)){
                    albumList.addChild(new AlbumCard(m, this));
                }
                if(m.getArtist().contains(search)){
                    artistList.addChild(new ArtistCard(m, this));
                }
                if(m.getTitle().contains(search)){
                    songList.addChild(new SongCard(m, this, mAppModel.getProfile().getUserInfo().getPeerId() == m.getOwnerPeerId()));
                }
                break;
             case CLEAR:
                 log.info("Search Clear");
                 break;
            
        }
       
    }

   

    
    
   
    
    public interface ISearchResultController{
         /**
         * display user details
         *
         * @param user
         */
        void onPeopleDetailRequested(UserInfo user);
        
        /**
         * display user details
         *
         * @param music
         */
        public void onArtistDetailRequested(Music music);
        
         /**
         * display album details
         *
         * @param music
         */
        public void onAlbumDetailRequested(Music music);
        
    }
    
    
}

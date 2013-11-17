package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.card.SimpleCard;
import fr.utc.lo23.sharutc.ui.custom.CardList;
import fr.utc.lo23.sharutc.ui.custom.card.AlbumCard;
import fr.utc.lo23.sharutc.ui.custom.card.ArtistCard;
import fr.utc.lo23.sharutc.ui.custom.card.DraggableCard;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import fr.utc.lo23.sharutc.ui.custom.card.UserCard;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SearchResultController implements Initializable, UserCard.IUserCard, SongCard.ISongCard, ArtistCard.IArtistCard {
    public VBox gridpane;
    private String search;
    private CardList songList;
    private CardList friendList;
    private CardList artistList;
    private CardList albumList;
    private ISearchResultController mInterface;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (resourceBundle != null) {
            search = resourceBundle.getString("search");
        } else {
            search = "";
        }
       
        songList = new CardList  ("Songs", "bgBlue");
        friendList = new CardList("Friends", "bgGreen");
        artistList = new CardList("Artists", "bgRed");
        albumList = new CardList ("Albums", "bgOrange");
        
        gridpane.getChildren().add(songList);
        gridpane.getChildren().add(friendList);
        gridpane.getChildren().add(artistList);
        gridpane.getChildren().add(albumList);
        
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
        SongCard newCard = new SongCard(m, this, true);
       
        addChild(newCard);
        
        SimpleCard card = new ArtistCard(m, this);
        this.addChild(card);
        
        card = new AlbumCard(m);
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
    public void onPlayRequested(Music music) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSongRemoveFromMusicRequested(Music music) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSongDetailsRequested(Music music) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSongAddToMusicRequested(Music music) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSongCardSelected(SongCard songCard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDragStart(MouseEvent event, DraggableCard draggableCard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDragStop(DraggableCard draggableCard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onArtistDetailRequested(Music music) {
        mInterface.onArtistDetailRequested(music);
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
        
    }
    
    
}

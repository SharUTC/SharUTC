package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.PeopleCard;
import fr.utc.lo23.sharutc.ui.custom.SimpleCard;
import fr.utc.lo23.sharutc.ui.custom.CardList;
import fr.utc.lo23.sharutc.ui.custom.UserCard;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;

public class SearchResultController implements Initializable, UserCard.IUserCard {
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
       
    }
    
    public void setInterface(ISearchResultController i){
        this.mInterface = i;
    }
    
    public void addChild(SimpleCard card){
        if(card instanceof UserCard){
            friendList.addChild(card);
            
        }//TODO else if(card instanceof )
    }

    @Override
    public void onPeopleDetailsRequested(UserInfo userInfo) {
        mInterface.onPeopleDetailRequested(userInfo);
    }
    
    public interface ISearchResultController{
         /**
         * display user details
         *
         * @param user
         */
        void onPeopleDetailRequested(UserInfo user);
        
    }
    
    
}

package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.PeopleCard;
import fr.utc.lo23.sharutc.ui.custom.SimpleCard;
import fr.utc.lo23.sharutc.ui.widget.CardList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;

public class SearchResultController implements Initializable {
    public VBox gridpane;
    private String search;
    private CardList songList;
    private CardList friendList;
    private CardList artistList;
    private CardList albumList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (resourceBundle != null) {
            search = resourceBundle.getString("search");
        } else {
            search = "";
        }
       
        songList = new CardList("Songs");
        friendList = new CardList("Friends");
        artistList = new CardList("Artists");
        albumList = new CardList("Albums");
        
        gridpane.getChildren().add(songList.buildPane());
        gridpane.getChildren().add(friendList.buildPane());
        gridpane.getChildren().add(artistList.buildPane());
        gridpane.getChildren().add(albumList.buildPane());
        
        UserInfo u = new UserInfo();
        u.setFirstName("bob");
        addChild(new PeopleCard(u));
        addChild(new PeopleCard(u));
        addChild(new PeopleCard(u));
        addChild(new PeopleCard(u));
        addChild(new PeopleCard(u));
        addChild(new PeopleCard(u));
       
    }
    
    public void addChild(SimpleCard card){
        if(card instanceof PeopleCard){
            friendList.addChild(card);
            
        }//TODO else if(card instanceof )
    }
}

package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.card.ArtistCard;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import fr.utc.lo23.sharutc.ui.custom.card.TagCard;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class PeopleDetailController extends SongSelectorController implements Initializable {

    public Label login;
    public Button addToFriendsButton;
    public Button seeMoreSongs;
    public Button seeMoreArtists;
    public Button seeMoreTags;
    public FlowPane songsContainer;
    public FlowPane artistsContainer;
    public FlowPane tagsContainer;

    @Inject
    private AppModel mAppModel;

    private UserInfo mUserInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addToFriendsButton.getStyleClass().add("bgGreen");
        seeMoreArtists.getStyleClass().add("bgRed");
        seeMoreSongs.getStyleClass().add("bgBlue");

        for (int i = 0; i < 3; i++) {
            TagCard newCard = new TagCard("Tag " + String.valueOf(i));
            tagsContainer.getChildren().add(newCard);
        }

    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        login.setText(mUserInfo.getLogin());

        Set<String> artists = new LinkedHashSet<String>();
        Set<String> tags = new LinkedHashSet<String>();

        for (Music music : mAppModel.getLocalCatalog().getMusics()) {
            artists.add(music.getArtist());
            System.out.println("Music " + music.getTitle());
            SongCard newCard = new SongCard(music, this, false);
            songsContainer.getChildren().add(newCard);
        }

        for (String artist : artists) {
            ArtistCard newCard = new ArtistCard(artist, null);
            artistsContainer.getChildren().add(newCard);
        }
    }

    public void handleAddToFriendsClicked(ActionEvent actionEvent) {

    }

    public void handleSeeMoreFriendsClicked(ActionEvent actionEvent) {

    }

    public void handleSeeMoreArtistsClicked(ActionEvent actionEvent) {

    }

    public void handleSeeMoreTagsClicked(ActionEvent actionEvent) {

    }
}

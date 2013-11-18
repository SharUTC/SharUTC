package fr.utc.lo23.sharutc.ui;

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
import java.util.ResourceBundle;

public class PeopleDetailController extends SongSelectorController implements Initializable {

    public Label login;
    public Button addToFriendsButton;
    public FlowPane songsContainer;
    public FlowPane artistsContainer;
    public FlowPane tagsContainer;
    private UserInfo mUserInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < 3; i++) {
            final Music m = new Music();
            m.setTitle("Music " + i);
            SongCard newCard = new SongCard(m, this, false);
            songsContainer.getChildren().add(newCard);
        }
        for (int i = 0; i < 3; i++) {
            ArtistCard newCard = new ArtistCard();
            artistsContainer.getChildren().add(newCard);
        }
        for (int i = 0; i < 3; i++) {
            TagCard newCard = new TagCard("Tag " + String.valueOf(i));
            tagsContainer.getChildren().add(newCard);
        }

    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        login.setText(mUserInfo.getLogin());
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

package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.music.FetchRemoteCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.profile.AddContactToCategoryCommand;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.card.ArtistCard;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import fr.utc.lo23.sharutc.ui.custom.card.TagCard;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class PeopleDetailController extends SongSelectorController implements Initializable, TagCard.ITagCard, CollectionChangeListener {

    private static final Logger log = LoggerFactory
            .getLogger(SongListController.class);

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

    @Inject
    private FetchRemoteCatalogCommand fetchRemoteCatalogCommand;
    @Inject
    private AddContactToCategoryCommand addContactToCategoryCommand;

    private UserInfo mUserInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addToFriendsButton.getStyleClass().add("bgGreen");
        seeMoreArtists.getStyleClass().add("bgRed");
        seeMoreSongs.getStyleClass().add("bgBlue");

        mAppModel.getProfile().getContacts().addPropertyChangeListener(this);
        mAppModel.getRemoteUserCatalog().addPropertyChangeListener(this);

    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;

        Set<String> artists = new LinkedHashSet<String>();
        Set<String> tags = new LinkedHashSet<String>();

        if (userInfo.getPeerId() == mAppModel.getProfile().getUserInfo().getPeerId()) {
            //the current user info
            login.setText("Your profile");

            //hide add to friends
            addToFriendsButton.setVisible(false);

            List<Music> musics = mAppModel.getLocalCatalog().getMusics();

            for (Music music : musics) {
                tags.addAll(music.getTags());
                artists.add(music.getArtist());
                System.out.println("Music " + music.getTitle());
                SongCard newCard = new SongCard(music, this, false);
                songsContainer.getChildren().add(newCard);
            }

            for (String artist : artists) {
                ArtistCard newCard = new ArtistCard(artist, null);
                artistsContainer.getChildren().add(newCard);
            }

            for (String tag : tags) {
                TagCard newCard = new TagCard(tag, this);
                tagsContainer.getChildren().add(newCard);
            }
        } else {
            if (isFriend(mUserInfo)) {
                //hide add to friend button if already in friend list
                addToFriendsButton.setVisible(false);
            }
            login.setText(mUserInfo.getLogin());
            //TODO launch network stuff on new thread
            fetchRemoteCatalogCommand.setPeer(userInfo.toPeer());
            fetchRemoteCatalogCommand.execute();
        }
    }

    /**
     * Check is the user is in the contact list
     *
     * @param userInfo
     * @return
     */
    private boolean isFriend(UserInfo userInfo) {
        boolean isFriendOfMine = false;
        for (Contact contact : mAppModel.getProfile().getContacts().getContacts()) {
            if (contact.getUserInfo().getPeerId().equals(userInfo.getPeerId())) {
                isFriendOfMine = true;
                break;
            }
        }
        return isFriendOfMine;
    }

    public void handleAddToFriendsClicked(ActionEvent actionEvent) {
        addContactToCategoryCommand.setContact(new Contact(mUserInfo));
        addContactToCategoryCommand.setCategory(mAppModel.getProfile().getCategories().findCategoryById(0));
        addContactToCategoryCommand.execute();

    }

    public void handleSeeMoreFriendsClicked(ActionEvent actionEvent) {

    }

    public void handleSeeMoreArtistsClicked(ActionEvent actionEvent) {

    }

    public void handleSeeMoreTagsClicked(ActionEvent actionEvent) {

    }

    @Override
    public void onTagSelected(String tagName) {
        log.debug("tag selected : " + tagName);
    }

    @Override
    public void onMusicDropOnTag(String tagName) {
        log.debug("music dropped on tag : " + tagName);
    }

    @Override
    public void collectionChanged(CollectionEvent ev) {
        final CollectionEvent.Type type = ev.getType();
        final Object item = ev.getItem();
        log.info("collectionChanged : Object " + item.getClass() + " | type : " + type.name());
        if (type.equals(CollectionEvent.Type.ADD)) {
            //ADD EVENT
            if (item instanceof Contact) {
                //user added to friend
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //hide addToFriend button
                        addToFriendsButton.setVisible(false);
                    }
                });
            } else if (item instanceof Music) {
                //user added to friend
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //add song card
                        SongCard newSongCard = new SongCard((Music) item, PeopleDetailController.this, false);
                        songsContainer.getChildren().add(newSongCard);
                        //add related artist  card
                        ArtistCard newArtistCard = new ArtistCard(((Music) item).getArtist(), null);
                        artistsContainer.getChildren().add(newArtistCard);
                        //add tag card
                        for (String tag : ((Music) item).getTags()) {
                            TagCard newCard = new TagCard(tag, PeopleDetailController.this);
                            tagsContainer.getChildren().add(newCard);
                        }
                    }
                });
            }
        }
    }
}

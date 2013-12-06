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

public class PeopleDetailController extends SongSelectorController implements Initializable, TagCard.ITagCard, CollectionChangeListener, ArtistCard.IArtistCard {

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
    private Set<String> mArtistsFound = new LinkedHashSet<String>();
    private Set<String> mTagsFound = new LinkedHashSet<String>();
    private IPeopleDetailController mCallBack;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addToFriendsButton.getStyleClass().add("bgGreen");
        seeMoreArtists.getStyleClass().add("bgRed");
        seeMoreSongs.getStyleClass().add("bgBlue");

        mAppModel.getProfile().getContacts().addPropertyChangeListener(this);
        mAppModel.getRemoteUserCatalog().addPropertyChangeListener(this);

    }

    public void setInterface(IPeopleDetailController i) {
        super.setInterface(i);
        mCallBack = i;
    }
    
    

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;


        if (userInfo.getPeerId() == mAppModel.getProfile().getUserInfo().getPeerId()) {
            //the current user info
            login.setText("Your profile");

            //hide add to friends
            addToFriendsButton.setVisible(false);

            List<Music> musics = mAppModel.getLocalCatalog().getMusics();

            for (Music music : musics) {
                processNewMusic(music, true);
            }

            //show artist and tag
            displayRetrieveData();
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
        if(mCallBack != null) {
            mCallBack.onTagFilterRequested(tagName);
        }
    }

    @Override
    public void onMusicDropOnTag(String tagName) {
        log.debug("music dropped on tag : " + tagName);
    }

    @Override
    public void collectionChanged(CollectionEvent ev) {
        final CollectionEvent.Type type = ev.getType();
        final Object item = ev.getItem();
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
                //retrieve music from network
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //add song card
                        processNewMusic((Music) item, false);
                        displayRetrieveData();
                    }
                });
            }
        }
    }

    /**
     * process music to display data properly
     *
     * @param music
     */
    private void processNewMusic(final Music music, boolean owned) {
        //add tags to the tags list
        mTagsFound.addAll(music.getTags());

        //add music artist to the artist list
        mArtistsFound.add(music.getArtist());

        //display the new music card
        final SongCard newSongCard = new SongCard(music, PeopleDetailController.this, owned);
        songsContainer.getChildren().add(newSongCard);
    }

    /**
     * display retrieve data
     */
    private void displayRetrieveData() {
        artistsContainer.getChildren().clear();
        tagsContainer.getChildren().clear();

        for (String artist : mArtistsFound) {
            ArtistCard newCard = new ArtistCard(artist, this);
            artistsContainer.getChildren().add(newCard);
        }

        for (String tag : mTagsFound) {
            TagCard newCard = new TagCard(tag, this);
            tagsContainer.getChildren().add(newCard);
        }
    }

    @Override
    public void onArtistDetailRequested(final String artistName) {
        log.info("artist info requested " + artistName);
        if(mCallBack != null) {
            mCallBack.onArtistDetailRequested(artistName);
        }
    }
    
    public interface IPeopleDetailController extends ISongListController{
        public void onArtistDetailRequested(final String artistName);
        public void onTagFilterRequested(final String tagName);
    }
}

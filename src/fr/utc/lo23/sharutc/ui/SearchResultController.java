package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.search.MusicSearchCommand;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.userdata.ActivePeerList;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.card.SimpleCard;
import fr.utc.lo23.sharutc.ui.custom.CardList;
import fr.utc.lo23.sharutc.ui.custom.card.AlbumCard;
import fr.utc.lo23.sharutc.ui.custom.card.ArtistCard;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import fr.utc.lo23.sharutc.ui.custom.card.UserCard;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import static fr.utc.lo23.sharutc.util.CollectionEvent.Type.ADD;
import static fr.utc.lo23.sharutc.util.CollectionEvent.Type.CLEAR;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchResultController extends SongSelectorController implements RighpaneInterface, CollectionChangeListener, Initializable, AlbumCard.IAlbumCard, UserCard.IUserCard, ArtistCard.IArtistCard {

    private static final Logger log = LoggerFactory
            .getLogger(SearchResultController.class);
    public VBox gridpane;
    private String mCurrentCriteriaSearch;
    private CardList songList;
    private CardList friendList;
    private CardList artistList;
    private CardList albumList;
    private ISearchResultController mInterface;
    @Inject
    private AppModel mAppModel;
    @Inject
    private MusicSearchCommand mMusicSearchCommand;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //listen for changes on the AppModel
        mAppModel.getSearchResults().addPropertyChangeListener(this);

        songList = new CardList("Songs", "bgBlue");
        friendList = new CardList("Friends", "bgGreen");
        artistList = new CardList("Artists", "bgRed");
        albumList = new CardList("Albums", "bgOrange");

        gridpane.getChildren().add(songList);
        gridpane.getChildren().add(friendList);
        gridpane.getChildren().add(artistList);
        gridpane.getChildren().add(albumList);


        ActivePeerList peers = mAppModel.getActivePeerList();
        //TODO adapte to the last master modification
        HashMap<UserInfo, Date> peerList = peers.getActivePeers();
        for (UserInfo peer : peerList.keySet()) {
            if (peer.getFirstName().contains(mCurrentCriteriaSearch) || peer.getLastName().contains(mCurrentCriteriaSearch)
                    || mCurrentCriteriaSearch.contains(peer.getFirstName()) || mCurrentCriteriaSearch.contains(peer.getLastName())) {
                UserInfo u = new UserInfo();
                u.setPeerId(peer.getPeerId());
                u.setFirstName(peer.getFirstName());
                u.setLastName(peer.getLastName());
            }
        }
    }

    public void searchAll(final String criteriaString) {
        log.debug("search all -> " + criteriaString);
        mCurrentCriteriaSearch = criteriaString;
        searchMusic(mCurrentCriteriaSearch);
    }

    private void searchMusic(final String criteriaString) {
        //execute an asynchronous search
        final Task<Void> searchMusicTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                log.debug("search music -> " + criteriaString);
                SearchCriteria critera = new SearchCriteria(criteriaString);
                mMusicSearchCommand.setSearchCriteria(critera);
                mMusicSearchCommand.execute();
                return null;
            }
        };
        new Thread(searchMusicTask).start();
    }

    public void setInterface(ISearchResultController i) {
        this.mInterface = i;
    }

    public void addChild(SimpleCard card) {
        if (card instanceof UserCard) {
            friendList.addChild(card);
        } else if (card instanceof SongCard) {
            songList.addChild(card);
        } else if (card instanceof ArtistCard) {
            artistList.addChild(card);
        } else if (card instanceof AlbumCard) {
            albumList.addChild(card);
        }
    }

    @Override
    public void onPeopleDetailsRequested(UserInfo userInfo) {
        mInterface.onPeopleDetailRequested(userInfo);
    }

    @Override
    public void onArtistDetailRequested(String artistName) {
        mInterface.onArtistDetailRequested(artistName);
    }

    @Override
    public void onAlbumDetailRequested(String albumName) {
        mInterface.onAlbumDetailRequested(albumName);
    }

    @Override
    public void collectionChanged(CollectionEvent ev) {
        log.debug("collectionChanged -> " + ev.getType().name());
        handleCollectionEventOnUiThread(ev);

    }

    public void handleCollectionEventOnUiThread(final CollectionEvent ev) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                switch (ev.getType()) {
                    case ADD:
                        Music m = ((Music) ev.getItem());
                        log.debug("add music " + m.getTitle() + " " + m.getArtist() + " " + m.getAlbum());
                        if (m.getAlbum().toLowerCase().contains(mCurrentCriteriaSearch)) {
                            log.debug("add music -- album");
                            albumList.addChild(new AlbumCard(m, SearchResultController.this));
                        }
                        if (m.getArtist().toLowerCase().contains(mCurrentCriteriaSearch)) {
                            log.debug("add music -- artist");
                            artistList.addChild(new ArtistCard(m.getArtist(), SearchResultController.this));
                        }
                        if (m.getTitle().toLowerCase().contains(mCurrentCriteriaSearch)) {
                            log.debug("add music -- song");
                            songList.addChild(new SongCard(m, SearchResultController.this, mAppModel.getProfile().getUserInfo().getPeerId() == m.getOwnerPeerId()));
                        }
                        break;
                    case CLEAR:
                        songList.clear();
                        artistList.clear();
                        albumList.clear();
                        friendList.clear();
                        break;

                }
            }
        });
    }

    @Override
    public void onDetach() {
        mAppModel.getSearchResults().removePropertyChangeListener(this);
    }

    public interface ISearchResultController extends SongListController.ISongListController {

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
        public void onArtistDetailRequested(String artistName);

        /**
         * display album details
         *
         * @param music
         */
        public void onAlbumDetailRequested(String albumName);
    }
}

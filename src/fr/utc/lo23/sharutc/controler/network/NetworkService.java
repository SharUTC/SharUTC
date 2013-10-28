package fr.utc.lo23.sharutc.controler.network;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;

/**
 *
 */
public interface NetworkService {

    /**
     *
     */
    public void startListening();

    /**
     *
     */
    public void startHeartbeat();

    /**
     *
     */
    public void stopListening();

    /**
     *
     */
    public void stopHeartbeat();

    /**
     *
     * @param string
     */
    public void handleMessage(String string);

    /**
     *
     * @param peer
     */
    public void sendUnicastGetCatalog(Peer peer);

    /**
     *
     * @param peer
     */
    public void sendUnicastCatalog(Peer peer);

    /**
     *
     */
    public void sendBroadcastGetTagMap();

    /**
     *
     * @param peer
     * @param tagMap
     */
    public void sendUnicastTagMap(Peer peer, Long conversationId, TagMap tagMap);

    /**
     *
     * @param peer the music owner
     * @param music the music to comment
     * @param comment the comment to add
     */
    public void addComment(Peer peer, Music music, String comment);

    /**
     *
     * @param peer the music owner
     * @param music
     * @param comment
     * @param commentIndex
     */
    public void editComment(Peer peer, Music music, String comment, Integer commentIndex);

    /**
     *
     * @param peer the music owner
     * @param music
     * @param commentIndex
     */
    public void removeComment(Peer peer, Music music, Integer commentIndex);

    /**
     *
     * @param peer the music owner
     * @param music
     * @param rating
     */
    public void setScore(Peer peer, Music music, Integer rating);

    /**
     *
     * @param peer the music owner
     * @param music
     */
    public void unsetScore(Peer peer, Music music);

    /**
     *
     * @param criteria
     */
    public void searchRequestBroadcast(SearchCriteria criteria);

    /**
     *
     * @param peer
     * @param catalog
     */
    public void sendMusicSearchResults(Peer peer, Catalog catalog);

    /**
     *
     * @param peer
     * @param music
     */
    public void sendDownloadRequest(Peer peer, Music music);

    /**
     *
     * @param peer
     * @param catalog
     */
    public void sendMusics(Peer peer, Catalog catalog);

    /**
     *
     * @param peer
     * @param musicId
     */
    public void downloadMusicForPlaying(Peer peer, long musicId);

    /**
     *
     * @param peer
     * @param music
     */
    public void sendMusicToPlay(Peer peer, Music music);

    /**
     *
     * @param userInfo
     */
    public void userInfoBroadcast(UserInfo userInfo);

    /**
     *
     */
    public void disconnectionBroadcast();
}

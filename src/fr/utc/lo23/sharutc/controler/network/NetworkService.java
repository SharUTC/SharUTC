package fr.utc.lo23.sharutc.controler.network;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.net.UnknownHostException;

/**
 * Service managing the network part of the p2p application.
 * <p>
 * It automatically discovers new user connecting to the network and add them to
 * the ActivePeerList. It allows other Service, through commands, to send
 * message to any or all connected peers. It also parse incoming message from
 * connected peers and instantiate the right command to handle it.
 */
public interface NetworkService {
    public static int defaultPort = 1337;
    public static String defaultGroup = "239.42.42.42";

    /**
     * Setup the NetworkService with the default parameters.
     */
    public void start();

    /**
     * Setup the NetworkService and start it.
     *
     * @param port the port to bind sockets to
     * @param group the UDP group to join (a class D IP address)
     * @throws java.net.UnknownHostException if the given group is not a valid
     * class D IP address
     */
    public void start(int port, String group) throws UnknownHostException;

    /**
     * Stop the NetworkService and all its components.
     */
    public void stop();

    /**
     * Send a heartbeat message to all the connected peer
     * to verify if a peer is always connected
     */
    public void sendBroadcastHeartbeat();

    /**
     * Request a peer for its music catalog.
     *
     * @param peer a Peer to send the request to
     */
    public void sendUnicastGetCatalog(Peer peer);

    /**
     * Send the local catalog to a peer.
     *
     * @param peer the target peer
     * @param conversationId the current conversation id
     * @param catalog the local catalog to send
     */
    public void sendUnicastCatalog(Peer peer, Long conversationId, Catalog catalog);

    /**
     * Request the tag map of all connected peers.
     */
    public void sendBroadcastGetTagMap();

    /**
     * Send the local tag map to a peer.
     *
     * @param peer receiving peer
     * @param conversationId the current conversation id
     * @param tagMap the local tag map to send
     */
    public void sendUnicastTagMap(Peer peer, Long conversationId, TagMap tagMap);

    /**
     * Add a comment on a music owned by another peer.
     *
     * @param peer the music owner
     * @param music the music to comment
     * @param comment the comment to add
     */
    public void addComment(Peer peer, Music music, String comment);

    /**
     * Edit a comment the user made on another peer's music.
     *
     * @param peer the music owner
     * @param music the commented music
     * @param comment the comment's new text
     * @param commentIndex the comment's identifier
     */
    public void editComment(Peer peer, Music music, String comment, Integer commentIndex);

    /**
     * Remove a comment from another peer's music.
     *
     * @param peer the music owner
     * @param commenter the owner of the comment
     * @param music the commented music
     * @param commentIndex the comment's identifier
     */
    public void removeComment(Peer peer, Peer commenter, Music music, Integer commentIndex);

    /**
     * Rate a music from another peer.
     *
     * @param peer the music owner
     * @param scoringPeer the peer adding the score
     * @param music the music to rate
     * @param rating the rating
     */
    public void setScore(Peer peer, Peer scoringPeer, Music music, Integer rating);

    /**
     * Remove the user's rating from a music of a peer.
     *
     * @param peer the music owner
     * @param music the music to unrate
     */
    public void unsetScore(Peer peer, Music music);

    /**
     * Send a music search request to all connected peers.
     *
     * @param criteria a SearCriteria describing the search request
     */
    public void searchRequestBroadcast(SearchCriteria criteria);

    /**
     * Send back the result of a search to the requesting peer.
     *
     * @param peer the requesting peer
     * @param conversationId the current conversation id
     * @param catalog a Catalog containing the matching musics
     */
    public void sendMusicSearchResults(Peer peer, Long conversationId, Catalog catalog);

    /**
     * Request a list of music.
     * <p>
     * Sends a Catalog containing a list of Music with no file attribute to be
     * downloaded.
     *
     * @param peer the target peer
     * @param catalog a Catalog of all music to download
     */
    public void sendDownloadRequest(Peer peer, Catalog catalog);

    /**
     * Send a complete Catalog to a peer.
     * <p>
     * The Catalog sent contains Music object with a set file attribute.
     *
     * @param peer the requesting peer
     * @param catalog the Catalog to send
     */
    public void sendMusics(Peer peer, Catalog catalog);

    /**
     * Request one music for the player.
     *
     * @param peer the music owner
     * @param musicId a music identifier
     */
    public void downloadMusicForPlaying(Peer peer, long musicId);

    /**
     * Send one Music to be played by the peer.
     *
     * @param peer the peer playing the music
     * @param conversationId the current conversation id
     * @param music the Music to send
     */
    public void sendMusicToPlay(Peer peer, Long conversationId, Music music);

    /**
     * Notify all user of the update to our user info.
     * <p>
     * Multicast to all connected peers the updated user info.
     *
     * @param userInfo the new user's info to broadcast
     */
    public void userInfoBroadcast(UserInfo userInfo);
    /**
     * Notify all user of the client's connection.
     * <p>
     * Multicast to all connected peers the user info.
     *
     * @param userInfo the user's info to broadcast
     */
    public void connectionBroadcast(UserInfo userInfo);

    /**
     * Notify all user of the client's disconnection.
     */
    public void disconnectionBroadcast();

    /**
     * Reply to a connection broadcast notification from another peer.
     *
     * @param peer the peer to reply to
     * @param userInfo the userInfo to include in the reply
     */
    public void sendConnectionResponse(Peer peer, UserInfo userInfo);
}

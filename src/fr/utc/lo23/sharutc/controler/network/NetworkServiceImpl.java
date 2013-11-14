package fr.utc.lo23.sharutc.controler.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
@Singleton
public class NetworkServiceImpl implements NetworkService {

    private static final Logger log = LoggerFactory
            .getLogger(NetworkServiceImpl.class);
    private final AppModel appModel;
    private final MessageParser messageParser;
    private final MessageHandler messageHandler;
    private final HashMap<Long, PeerSocket> mPeers;
    private ListenThread mListenThread;
    private PeerDiscoverySocket mPeerDiscoverySocket;

    @Inject
    public NetworkServiceImpl(AppModel appModel, MessageParser messageParser, MessageHandler messageHandler) {
        this.appModel = appModel;
        this.messageParser = messageParser;
        this.messageHandler = messageHandler;
        mPeers = new HashMap();
        mListenThread = null;
        mPeerDiscoverySocket = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(int port, String group) throws UnknownHostException {
        InetAddress g = InetAddress.getByName(group);
        mListenThread = new ListenThread(port, this, appModel, messageParser, messageHandler);
        mListenThread.start();
        mPeerDiscoverySocket = new PeerDiscoverySocket(port, g, this, appModel, messageParser, messageHandler);
        mPeerDiscoverySocket.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        mListenThread.stop();
        mPeerDiscoverySocket.stop();
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void addPeer(long peerId, PeerSocket peerSocket) {
        log.warn("addPeer - Not supported yet.");
        if (peerId == 0 || peerSocket == null) {
            log.error("[NetworkService - addPeer()] - null object");
        } else {
            this.mPeers.put(peerId, peerSocket);
            log.info("[NetworkService - addPeer()] - peer " + peerId + " had been adding succesfully");
        }
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void removePeer(PeerSocket peerSocket) {
        mPeers.values().remove(peerSocket);
    }

    /**
     * {inheritDoc}
     */
    protected void sendBroadcast(Message message) {
        for (PeerSocket peer : mPeers.values()) {
            peer.send(message);
        }
    }

    /**
     * {inheritDoc}
     */
    protected void sendUnicast(Message message, Peer peer) {
        if (peer != null) {
            this.mPeers.get(peer.getId()).send(message);
        } else {
            log.error("[NetworkService - sendUnicast()] - object Peer is null");
        }
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendUnicastGetCatalog(Peer peer) {
        Message message = messageParser.write(MessageType.MUSIC_GET, new Object[][]{{Message.OWNER_PEER_ID, peer.getId()}, {Message.CONVERSATION_ID, appModel.getCurrentConversationId()}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendUnicastCatalog(Peer peer, Long conversationID, Catalog catalog) {
        Message message = messageParser.write(MessageType.MUSIC_CATALOG, new Object[][]{{Message.CATALOG, catalog}, {Message.OWNER_PEER_ID, peer.getId()}, {Message.CONVERSATION_ID, appModel.getCurrentConversationId()}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendBroadcastGetTagMap() {
        Message message = messageParser.write(MessageType.TAG_GET_MAP, new Object[][]{{Message.CONVERSATION_ID, appModel.getCurrentConversationId()}});
        sendBroadcast(message);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendUnicastTagMap(Peer peer, Long conversationId, TagMap tagMap) {
        Message message = messageParser.write(MessageType.TAG_MAP, new Object[][]{{Message.TAG_MAP, tagMap}, {Message.CONVERSATION_ID, appModel.getCurrentConversationId()}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void addComment(Peer peer, Music music, String comment) {
       Message message = messageParser.write(MessageType.COMMENT_ADD, new Object[][]{{Message.OWNER_PEER, peer}, {Message.AUTHOR_PEER, messageParser.getSource() }, {Message.MUSIC_ID, music}, {Message.COMMENT, comment}});
       sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void editComment(Peer peer, Music music, String comment, Integer commentIndex) {
        Message message = messageParser.write(MessageType.EDIT_COMMENT, new Object[][]{{Message.OWNER_PEER, peer}, {Message.AUTHOR_PEER, messageParser.getSource()},{Message.MUSIC, music}, {Message.COMMENT,comment}});
        sendUnicast(message, peer);
      
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void removeComment(Peer peer, Music music, Integer commentIndex) {
        Message message = messageParser.write(MessageType.COMMENT_REMOVE, new Object[][]{{Message.MUSIC,music},{Message.OWNER_PEER, peer}, {Message.COMMENT_ID, commentIndex}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void setScore(Peer peer, Music music, Integer rating) {
        Message message = messageParser.write(MessageType.SCORE_SET,new Object[][]{{Message.OWNER_PEER, peer}, {Message.MUSIC, music}, {Message.SCORE, rating}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void unsetScore(Peer peer, Music music) {
        log.warn("Not supported yet.");
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void searchRequestBroadcast(SearchCriteria criteria) {
        Message message = messageParser.write(MessageType.MUSIC_SEARCH, new Object[][]{{Message.SEARCH, criteria}, {Message.CONVERSATION_ID, appModel.getCurrentConversationId()}});
        sendBroadcast(message);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendMusicSearchResults(Peer peer, Long conversationID, Catalog catalog) {
        Message message = messageParser.write(MessageType.MUSIC_RESULTS, new Object[][]{{Message.CATALOG, catalog}, {Message.CONVERSATION_ID, appModel.getCurrentConversationId()}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendDownloadRequest(Peer peer, Catalog catalog) {
        Message message = messageParser.write(MessageType.MUSIC_GET, new Object[][]{{Message.CATALOG, catalog}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendMusics(Peer peer, Catalog catalog) {
        Message message = messageParser.write(MessageType.MUSIC_INSTALL, new Object[][]{{Message.CATALOG, catalog}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void downloadMusicForPlaying(Peer peer, long musicId) {
        Message message = messageParser.write(MessageType.MUSIC_GET_TO_PLAY, new Object[][]{{Message.MUSIC_ID, musicId}, {Message.CONVERSATION_ID, appModel.getCurrentConversationId()}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendMusicToPlay(Peer peer, Long conversationID, Music music) {;
        Message message = messageParser.write(MessageType.MUSIC_SEND_TO_PLAY, new Object[][]{{Message.MUSIC, music}, {Message.CONVERSATION_ID, appModel.getCurrentConversationId()}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void userInfoBroadcast(UserInfo userInfo) {
        if (userInfo != null) {
            mPeerDiscoverySocket.send(messageParser.write(MessageType.USER_INFO, new Object[][]{{Message.USER_INFO, userInfo}, {Message.CONVERSATION_ID, appModel.getCurrentConversationId()}}));
        } else {
            log.error("[NetworkService - userInfoBroadCast()] - userInfo is null");
        }
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void disconnectionBroadcast() {
        sendBroadcast(messageParser.write(MessageType.DISCONNECT, null));
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendConnectionResponse(Peer peer, UserInfo userInfo) {
        Message message = messageParser.write(MessageType.CONNECTION_RESPONSE, new Object[][]{{Message.USER_INFO, userInfo}});
        sendUnicast(message, peer);
    }
}

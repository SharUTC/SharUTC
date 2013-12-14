package fr.utc.lo23.sharutc.controler.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateDisconnectionCommand;
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
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 *
 * @see ListenThread
 * @see PeerDiscoverySocket
 */
@Singleton
public class NetworkServiceImpl implements NetworkService {

    private static final Logger log = LoggerFactory
            .getLogger(NetworkServiceImpl.class);
    private final AppModel appModel;
    private final MessageHandler messageHandler;
    private final MessageParser messageParser;
    @Inject
    private IntegrateDisconnectionCommand integrateDisconnectionCommand;
    private ListenThread mListenThread;
    private PeerDiscoverySocket mPeerDiscoverySocket;
    private HeartbeatThread mHeartbeatThread;
    private final HashMap<Long, PeerSocket> mPeers;

    @Inject
    public NetworkServiceImpl(AppModel appModel, MessageHandler messageHandler,
            MessageParser messageParser) {
        this.appModel = appModel;
        this.messageHandler = messageHandler;
        this.messageParser = messageParser;
        this.mListenThread = null;
        this.mPeerDiscoverySocket = null;
        this.mHeartbeatThread = null;
        this.mPeers = new HashMap<Long, PeerSocket>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        try {
            start(NetworkService.defaultPort, NetworkService.defaultGroup);
        } catch (UnknownHostException ex) {
            log.error(ex.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(int port, String group) throws UnknownHostException {
        InetAddress g = InetAddress.getByName(group);
        mListenThread = new ListenThread(port, appModel, messageHandler,
                messageParser, this);
        mListenThread.start();
        mPeerDiscoverySocket = new PeerDiscoverySocket(port, g, appModel,
                messageHandler, messageParser, this);
        mPeerDiscoverySocket.start();
        mHeartbeatThread = new HeartbeatThread(this);
        mHeartbeatThread.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        mListenThread.stop();
        mPeerDiscoverySocket.stop();
        mHeartbeatThread.stop();
        for (PeerSocket peer : mPeers.values()) {
            peer.stop();
        }
    }

    /**
     * Register a new peer and its PeerSocket to the NetworkService.
     *
     * @param peerId the id of the new peer
     * @param peerSocket the PeerSocket object associated to that peer
     */
    public synchronized void addPeer(long peerId, PeerSocket peerSocket) {
        if (peerId == 0 || peerSocket == null) {
            log.error("null object");
        } else {
            this.mPeers.put(peerId, peerSocket);
            log.info("peer " + peerId + " had been added succesfully");
        }
    }

    /**
     * Unregister a peer that is disconnected.
     *
     * @param peerSocket the PeerSocket of the disconnected peer
     */
    public synchronized void removePeer(PeerSocket peerSocket) {
        mPeers.values().remove(peerSocket);
    }

    /**
     * Disconnect from the model a peer which connection died.
     *
     * @param peerSocket the disconnected peerSocket
     */
    public synchronized void disconnectPeer(PeerSocket peerSocket) {
        Long peerId = null;
        for (Entry<Long, PeerSocket> entry : mPeers.entrySet()) {
            if (peerSocket == entry.getValue()) {
                peerId = entry.getKey();
                break;
            }
        }
        if (peerId != null) {
            integrateDisconnectionCommand.setPeerId(peerId);
            integrateDisconnectionCommand.execute();
        } else {
            log.error("Can't find and disconnect peerSocket");
        }
    }

    /**
     * Send a message to all the peers connected to the network.
     *
     * @param message the message to send
     */
    protected synchronized void sendBroadcast(Message message) {
        for (PeerSocket peer : mPeers.values()) {
            peer.send(message);
        }
    }

    /**
     * Send a message to a peer.
     *
     * @param message the message to send
     * @param peer the receiver
     */
    protected synchronized void sendUnicast(Message message, Peer peer) {
        if (peer != null && mPeers.containsKey(peer.getId())) {
            log.debug("before sendUnicast "+message.toString()+" | peer "+peer.getId());
            mPeers.get(peer.getId()).send(message);
            log.debug("after sendUnicast ");
            messageHandler.setPreviousMessageType(message.getType());
        } else {
            log.error("Peer " + (peer != null ? peer.getId() : "null") + " not connected");
        }
    }

    /**
     * Send a message to the multicast group.
     *
     * @param message the message to multicast
     */
    protected void sendMulticast(Message message) {
        mPeerDiscoverySocket.send(message);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendBroadcastHeartbeat() {
        Message message = messageParser.write(MessageType.HEARTBEAT, null);
        sendBroadcast(message);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendUnicastGetCatalog(Peer peer) {
        Message message = messageParser.write(MessageType.MUSIC_GET_CATALOG, new Object[][]{{Message.CONVERSATION_ID, appModel.getNextConversationId()}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendUnicastCatalog(Peer peer, Long conversationId, Catalog catalog) {
        Message message = messageParser.write(MessageType.MUSIC_SEND_CATALOG, new Object[][]{{Message.CATALOG, catalog}, {Message.CONVERSATION_ID, conversationId}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendBroadcastGetTagMap() {
        Message message = messageParser.write(MessageType.TAG_GET_MAP, new Object[][]{{Message.CONVERSATION_ID, appModel.getNextConversationId()}});
        sendBroadcast(message);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendUnicastTagMap(Peer peer, Long conversationId, TagMap tagMap) {
        Message message = messageParser.write(MessageType.TAG_MAP, new Object[][]{{Message.TAG_MAP, tagMap}, {Message.CONVERSATION_ID, conversationId}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void addComment(Peer peer, Music music, String comment) {
        Message message = messageParser.write(MessageType.COMMENT_ADD, new Object[][]{{Message.OWNER_PEER, peer}, {Message.MUSIC_ID, music}, {Message.COMMENT, comment}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void editComment(Peer peer, Music music, String comment, Integer commentIndex) {
        Message message = messageParser.write(MessageType.EDIT_COMMENT, new Object[][]{{Message.OWNER_PEER, peer}, {Message.MUSIC, music}, {Message.COMMENT, comment}});
        sendUnicast(message, peer);

    }

    /**
     * {inheritDoc}
     */
    @Override
    public void removeComment(Peer peer, Peer commenter, Music music, Integer commentIndex) {
        Message message = messageParser.write(MessageType.COMMENT_REMOVE, new Object[][]{{Message.AUTHOR_PEER, commenter}, {Message.MUSIC, music}, {Message.COMMENT_ID, commentIndex}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void setScore(Peer peer, Peer scoringPeer, Music music, Integer rating) {
        Message message = messageParser.write(MessageType.SCORE_SET, new Object[][]{{Message.AUTHOR_PEER, scoringPeer}, {Message.MUSIC, music}, {Message.SCORE, rating}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void unsetScore(Peer peer, Music music) {
        Message message = messageParser.write(MessageType.SCORE_UNSET, new Object[][]{{Message.MUSIC, music}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void searchRequestBroadcast(SearchCriteria criteria) {
        Message message = messageParser.write(MessageType.MUSIC_SEARCH, new Object[][]{{Message.SEARCH, criteria}, {Message.CONVERSATION_ID, appModel.getNextConversationId()}});
        sendBroadcast(message);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendMusicSearchResults(Peer peer, Long conversationId, Catalog catalog) {
        Message message = messageParser.write(MessageType.MUSIC_RESULTS, new Object[][]{{Message.CATALOG, catalog}, {Message.CONVERSATION_ID, conversationId}});
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
        Message message = messageParser.write(MessageType.MUSIC_GET_TO_PLAY, new Object[][]{{Message.MUSIC_ID, musicId}, {Message.CONVERSATION_ID, appModel.getNextConversationId()}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendMusicToPlay(Peer peer, Long conversationId, Music music) {
        Message message = messageParser.write(MessageType.MUSIC_SEND_TO_PLAY, new Object[][]{{Message.MUSIC, music}, {Message.CONVERSATION_ID, conversationId}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void userInfoBroadcast(UserInfo userInfo) {
        if (userInfo != null) {
            UserInfo ui = userInfo.clone();
            ui.setPassword(null);
            sendBroadcast(messageParser.write(MessageType.USER_INFO, new Object[][]{{Message.USER_INFO, ui}}));
        } else {
            log.error("userInfo is null");
        }
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void connectionBroadcast(UserInfo userInfo) {
        if (userInfo != null) {
            UserInfo ui = userInfo.clone();
            ui.setPassword(null);
            sendMulticast(messageParser.write(MessageType.CONNECTION, new Object[][]{{Message.USER_INFO, ui}}));
        } else {
            log.error("userInfo is null");
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
        UserInfo ui = userInfo.clone();
        ui.setPassword(null);
        Message message = messageParser.write(MessageType.CONNECTION_RESPONSE, new Object[][]{{Message.USER_INFO, ui}});
        sendUnicast(message, peer);
    }
}

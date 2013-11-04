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
    private final HashMap<Long, PeerSocket> mPeers;
    private ListenThread mListenThread;
    private PeerDiscoverySocket mPeerDiscoverySocket;

    @Inject
    public NetworkServiceImpl(AppModel appModel, MessageParser messageParser) {
        this.appModel = appModel;
        this.messageParser = messageParser;
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
        mListenThread = new ListenThread(port, this);
        mListenThread.start();
        mPeerDiscoverySocket = new PeerDiscoverySocket(port, g, this);
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
        if(peerId == 0 || peerSocket == null){
            log.error("[NetworkService - addPeer()] - null object");
        } else {
            this.mPeers.put(peerId, peerSocket);
            log.info("[NetworkService - addPeer()] - peer "+peerId+" had been adding succesfully");
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
    private void sendBroadcast(Message message) {
        for (PeerSocket peer: mPeers.values()) {
            peer.send(message);
        }
    }

    /**
     * {inheritDoc}
     */
    private void sendUnicast(Message message, Peer peer) {
        if(peer != null){
            log.error("[NetworkService - sendUnicast()] - object Peer is null");
        } else {
            this.mPeers.get(peer.getId()).send(message);
        }
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendUnicastGetCatalog(Peer peer) {
        log.warn("Not supported yet.");
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendUnicastCatalog(Peer peer, Catalog catalog) {
        log.warn("Not supported yet.");
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
        Message message = messageParser.write(MessageType.TAG_MAP, new Object[][]{{Message.CONVERSATION_ID, conversationId}, {Message.TAG_MAP, tagMap}});
        sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void addComment(Peer peer, Music music, String comment) {
        // Message message = messageParser.write(MessageType.COMMENT_ADD, new Object[][]{{Message.OWNER_PEER_ID, peer.getId()}, {Message.AUTHOR_PEER_ID, getLocalPeerId()}, {Message.MUSIC_ID, music.getId()}, {Message.COMMENT, comment}});
        // sendUnicast(message, peer);
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void editComment(Peer peer, Music music, String comment, Integer commentIndex) {
        log.warn("Not supported yet.");
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void removeComment(Peer peer, Music music, Integer commentIndex) {
        log.warn("Not supported yet.");
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void setScore(Peer peer, Music music, Integer rating) {
        log.warn("Not supported yet.");
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
        log.warn("Not supported yet.");
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendMusicSearchResults(Peer peer, Catalog catalog) {
        log.warn("Not supported yet.");
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
        log.warn("Not supported yet.");
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendMusicToPlay(Peer peer, Music music) {
        log.warn("Not supported yet.");
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void userInfoBroadcast(UserInfo userInfo) {
        if(userInfo != null){
            sendBroadcast(messageParser.write(MessageType.CONNECTION, new Object[][]{{Message.CONVERSATION_ID,appModel.getCurrentConversationId()},{Message.USER_INFO, userInfo}}));
        } else {
            log.equals("[NetworkService - userInfoBroadCast()] - userInfo is null");
        }
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void disconnectionBroadcast() {
        sendBroadcast(messageParser.write(MessageType.DISCONNECT, null));
    }
}

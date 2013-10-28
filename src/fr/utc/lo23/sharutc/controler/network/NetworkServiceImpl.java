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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
@Singleton
public class NetworkServiceImpl implements NetworkService {

    private static final Logger log = LoggerFactory
            .getLogger(NetworkServiceImpl.class);
    @Inject
    private AppModel appModel;
    @Inject
    private MessageParser messageParser;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        log.warn("start - Not supported yet.");
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void addPeer(long peerId, PeerSocket peerSocket) {
        log.warn("addPeer - Not supported yet.");
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void removePeer(PeerSocket peerSocket) {
        log.warn("removePeer - Not supported yet.");
    }

    /**
     *
     */
    private void sendBroadcast(Message message) {
        log.warn("sendBroadcast - Not supported yet.");
    }

    /**
     *
     */
    private void sendUnicast(Message message, Peer peer) {
        log.warn("sendUnicast - Not supported yet.");
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
        log.warn("Not supported yet.");
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void sendMusics(Peer peer, Catalog catalog) {
        log.warn("Not supported yet.");
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
        log.warn("Not supported yet.");
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void disconnectionBroadcast() {
        log.warn("Not supported yet.");
    }
}

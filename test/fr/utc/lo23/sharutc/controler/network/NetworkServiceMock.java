package fr.utc.lo23.sharutc.controler.network;

import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
@Singleton
public class NetworkServiceMock implements NetworkService {
    private static final Logger log = LoggerFactory
            .getLogger(NetworkServiceMock.class);

    @Override
    public void start(int port, String group) {
        log.warn("Not supported yet.");
    }

    @Override
    public void stop() {
        log.warn("Not supported yet.");
    }

    @Override
    public void addPeer(long peerId, PeerSocket peerSocket) {
        log.warn("Not supported yet.");
    }

    @Override
    public void removePeer(PeerSocket peerSocket) {
        log.warn("Not supported yet.");
    }

    @Override
    public void sendUnicastGetCatalog(Peer peer) {
        log.warn("Not supported yet.");
    }

    @Override
    public void sendUnicastCatalog(Peer peer, Catalog catalog) {
        log.warn("Not supported yet.");
    }

    @Override
    public void sendBroadcastGetTagMap() {
        log.warn("Not supported yet.");
    }

    @Override
    public void sendUnicastTagMap(Peer peer, Long conversationId, TagMap tagMap) {
        log.warn("Not supported yet.");
    }

    @Override
    public void addComment(Peer peer, Music music, String comment) {
        log.warn("Not supported yet.");
    }

    @Override
    public void editComment(Peer peer, Music music, String comment, Integer commentIndex) {
        log.warn("Not supported yet.");
    }

    @Override
    public void removeComment(Peer peer, Music music, Integer commentIndex) {
        log.warn("Not supported yet.");
    }

    @Override
    public void setScore(Peer peer, Music music, Integer rating) {
        log.warn("Not supported yet.");
    }

    @Override
    public void unsetScore(Peer peer, Music music) {
        log.warn("Not supported yet.");
    }

    @Override
    public void searchRequestBroadcast(SearchCriteria criteria) {
        log.warn("Not supported yet.");
    }

    @Override
    public void sendMusicSearchResults(Peer peer, Catalog catalog) {
        log.warn("Not supported yet.");
    }

    @Override
    public void sendDownloadRequest(Peer peer, Catalog catalog) {
        log.warn("Not supported yet.");
    }

    @Override
    public void sendMusics(Peer peer, Catalog catalog) {
        log.warn("Not supported yet.");
    }

    @Override
    public void downloadMusicForPlaying(Peer peer, long musicId) {
        log.warn("Not supported yet.");
    }

    @Override
    public void sendMusicToPlay(Peer peer, Music music) {
        log.warn("Not supported yet.");
    }

    @Override
    public void userInfoBroadcast(UserInfo userInfo) {
        log.warn("Not supported yet.");
    }

    @Override
    public void disconnectionBroadcast() {
        log.warn("Not supported yet.");
    }
}

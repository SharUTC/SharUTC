package fr.utc.lo23.sharutc.controler.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.controler.command.music.AddCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteTagMapCommand;
import fr.utc.lo23.sharutc.controler.command.music.SendTagMapCommand;
import static fr.utc.lo23.sharutc.controler.network.MessageType.*;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
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
 *
 */
@Singleton
public class NetworkServiceImpl implements NetworkService {

    private static final Logger log = LoggerFactory
            .getLogger(NetworkServiceImpl.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    @Inject
    private AppModel appModel;
    @Inject
    private MessageParser messageParser;
    @Inject
    private MusicService musicService;
    @Inject
    private UserService userService;
    // all command interfaces used by network service
    @Inject
    private AddCommentCommand addCommentCommand;
    @Inject
    private SendTagMapCommand sendTagMapCommand;
    @Inject
    private IntegrateRemoteTagMapCommand integrateRemoteTagMapCommand;
    // more...
    
    /**
     *
     */
    @Override
    public void startListening() {
        log.warn("startListening - Not supported yet.");
    }

    /**
     *
     */
    @Override
    public void stopListening() {
        log.warn("stopListening - Not supported yet.");
    }

    /**
     *
     */
    @Override
    public void startHeartbeat() {
        log.warn("startHeartbeat - Not supported yet.");
    }

    /**
     *
     */
    @Override
    public void stopHeartbeat() {
        log.warn("stopHeartbeat - Not supported yet.");
    }

    private void sendBroadcast(Message message) {
        log.warn("sendBroadcast - Not supported yet.");
    }

    private void sendUnicast(Message message, Peer peer) {
        log.warn("sendUnicast - Not supported yet.");
    }
    /*
     private Message buildMessage(MessageType messageType, Object object) {
     if (messageType == null) {
     throw new RuntimeException("Missing messageType");
     }
     Message message = null;
     try {
     message = new Message(messageType, object != null ? mapper.writeValueAsString(object) : "", userService.getUID());
     } catch (JsonProcessingException ex) {
     log.error(ex.toString());
     }
     return message;
     }
     */

    @Override
    public void handleMessage(String string) {
        Message incomingMessage = null;
        try {
            incomingMessage = mapper.readValue(string, Message.class);
        } catch (Exception ex) {
            log.error(ex.toString());
        }

        if (incomingMessage != null) {
            try {
                messageParser.read(incomingMessage);
                messageParser.setFromPeerId(getLocalPeerId());
                Command command = null;
                // searching which command to execute following message type
                switch (incomingMessage.getType()) {
                    case MUSIC_GET_ALL:
                        break;
                    case MUSIC_CATALOG:
                        break;
                    case TAG_GET_MAP:
                        // nothing relative to local UI, no need to check conversation ID
                        Peer destinationPeer = messageParser.getSource();
                        sendTagMapCommand.setPeer(destinationPeer);
                        command = sendTagMapCommand;
                        break;
                    case TAG_MAP:
                        // we must check the conversation ID, the user may have left the cloud of tags screen
                        if (isMessageForCurrentConversation(incomingMessage)) {
                            TagMap tagMap = (TagMap) messageParser.getValue(Message.TAG_MAP);
                            integrateRemoteTagMapCommand.setTagMap(tagMap);
                            command = integrateRemoteTagMapCommand;
                        }
                        break;
                    case COMMENT_ADD:
                        //  addCommentCommand.setAuthorPeer(userService.getPeerById((Long) messageReader.getValue(Message.AUTHOR_PEER_ID)));
                        //  addCommentCommand.setMusic(musicService.getPeerById((Integer) messageReader.getValue(Message.MUSIC_ID)));
                        //  addCommentCommand.setOwnerPeer(userService.getPeerById((Long) messageReader.getValue(Message.OWNER_PEER_ID)));
                        //  addCommentCommand.setComment((String) messageReader.getValue(Message.COMMENT));
                        //  command = addCommentCommand;
                        break;
                    case COMMENT_EDIT:
                        break;
                    case COMMENT_REMOVE:
                        break;
                    case SCORE_SET:
                        break;
                    case SCORE_UNSET:
                        break;
                    case MUSIC_SEARCH:
                        break;
                    case MUSIC_RESULTS:
                        break;
                    case MUSIC_GET:
                        break;
                    case MUSIC_INSTALL:
                        break;
                    case MUSIC_GET_TO_PLAY:
                        break;
                    case MUSIC_SEND_TO_PLAY:
                        break;
                    case HEARTBEAT:
                        break;
                    case DISCONNECT:
                        break;
                    default:
                        log.warn("MISSING COMMAND {}", incomingMessage.getType().name());
                        break;
                }
                if (command != null) {
                    //FIXME: some commands have to check if they have to be executed with a conversationId
                    //FIXME: open a new thread and let it run the execute() method
                    command.execute();
                }
            } catch (Exception ex) {
                log.error(ex.toString());
            }
        }
    }

    @Override
    public void sendUnicastGetCatalog(Peer peer) {
        log.warn("Not supported yet.");
    }

    @Override
    public void sendUnicastCatalog(Peer peer) {
        log.warn("Not supported yet.");
    }

    @Override
    public void sendBroadcastGetTagMap() {
        Message message = messageParser.write(MessageType.TAG_GET_MAP, new Object[][]{});
        sendBroadcast(message);
    }

    @Override
    public void sendUnicastTagMap(Peer peer, TagMap tagMap) {
        Message message = messageParser.write(MessageType.TAG_MAP, new Object[][]{{Message.TAG_MAP, tagMap}});
        sendBroadcast(message);
    }

    @Override
    public void addComment(Peer peer, Music music, String comment) {
        Message message = messageParser.write(MessageType.COMMENT_ADD, new Object[][]{{Message.OWNER_PEER_ID, peer.getId()}, {Message.AUTHOR_PEER_ID, getLocalPeerId()}, {Message.MUSIC_ID, music.getId()}, {Message.COMMENT, comment}});
        sendUnicast(message, peer);
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
    public void sendDownloadRequest(Peer peer, Music music) {
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

    private boolean isMessageForCurrentConversation(Message message) {
        return appModel.getCurrentConversationId().equals(message.getConversationId());
    }

    private long getLocalPeerId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

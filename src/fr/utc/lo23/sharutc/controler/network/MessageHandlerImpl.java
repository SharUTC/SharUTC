package fr.utc.lo23.sharutc.controler.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.controler.command.music.AddCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteTagMapCommand;
import fr.utc.lo23.sharutc.controler.command.music.SendCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.SendTagMapCommand;
import fr.utc.lo23.sharutc.controler.command.search.InstallRemoteMusicsCommand;
import fr.utc.lo23.sharutc.controler.command.search.SendMusicsCommand;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
@Singleton
public class MessageHandlerImpl implements MessageHandler {

    private static final Logger log = LoggerFactory
        .getLogger(MessageHandlerImpl.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private final AppModel appModel;
    private final MessageParser messageParser;
    private final MusicService musicService;
    private final UserService userService;
    private Command command = null;

    @Inject
    public MessageHandlerImpl(AppModel appModel, MusicService musicService, UserService userService, MessageParser messageParser) {
        this.appModel = appModel;
        this.userService = userService;
        this.musicService = musicService;
        this.messageParser = messageParser;
    }
    // all command interfaces used by network service
    @Inject
    private SendTagMapCommand sendTagMapCommand;
    @Inject
    private IntegrateRemoteTagMapCommand integrateRemoteTagMapCommand;
    @Inject
    private SendMusicsCommand sendMusicsCommand;
    @Inject
    private InstallRemoteMusicsCommand installRemoteMusicsCommand;
    @Inject
    private SendCatalogCommand sendCatalogCommand;
    @Inject
    private IntegrateRemoteCatalogCommand integrateRemoteCatalogCommand;
    //@Inject
    //private AddCommentCommand addCommentCommand;
    // more...

    /**
     * {inheritDoc}
     */
    @Override
    public void handleMessage(String string) {
        Message incomingMessage = Message.fromJSON(string);
        if (incomingMessage != null) {
            try {
                messageParser.read(incomingMessage);
                messageParser.setFromPeerId(getLocalPeerId());
                // searching which command to execute following message type
                switch (incomingMessage.getType()) {
                    case MUSIC_GET_CATALOG:
                        sendCatalogCommand.setPeer(messageParser.getSource());
                        sendCatalogCommand.setConversationId(incomingMessage.getConversationId());
                        command = sendCatalogCommand;
                        break;
                    case MUSIC_CATALOG:
                        if (isMessageForCurrentConversation(incomingMessage)) {
                          integrateRemoteCatalogCommand.setCatalog((Catalog) messageParser.getValue(Message.CATALOG));
                          command = integrateRemoteCatalogCommand;
                        }
                        break;
                    case TAG_GET_MAP:
                        // nothing relative to local UI, no need to check conversation ID, but we have to forward it
                        sendTagMapCommand.setPeer(messageParser.getSource());
                        sendTagMapCommand.setConversationId(incomingMessage.getConversationId());
                        command = sendTagMapCommand;
                        break;
                    case TAG_MAP:
                        // we must check the conversation ID, the user may have left the cloud of tags screen
                        if (isMessageForCurrentConversation(incomingMessage)) {
                            integrateRemoteTagMapCommand.setTagMap((TagMap) messageParser.getValue(Message.TAG_MAP));
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
                        sendMusicsCommand.setPeer(messageParser.getSource());
                        sendMusicsCommand.setCatalog((Catalog) messageParser.getValue(Message.CATALOG));
                        command = sendMusicsCommand;
                        break;
                    case MUSIC_INSTALL:
                        installRemoteMusicsCommand.setCatalog((Catalog) messageParser.getValue(Message.CATALOG));
                        command = installRemoteMusicsCommand;
                        break;
                    case MUSIC_GET_TO_PLAY:
                        break;
                    case MUSIC_SEND_TO_PLAY:
                        break;
                    case DISCONNECT:
                        break;
                    default:
                        log.warn("MISSING COMMAND {}", incomingMessage.getType().name());
                        break;
                }
                if (command != null) {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            command.execute();
                        }
                    };
                    thread.start();
                }
            } catch (Exception ex) {
                log.error(ex.toString());
            }
        }
    }

    private boolean isMessageForCurrentConversation(Message message) {
        return appModel.getCurrentConversationId().equals(message.getConversationId());
    }

    private long getLocalPeerId() {
        return appModel.getProfile().getUserInfo().getPeerId();
    }
}

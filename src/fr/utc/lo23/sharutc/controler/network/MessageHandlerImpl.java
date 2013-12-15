package fr.utc.lo23.sharutc.controler.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateDisconnectionCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateUserInfoAndReplyCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateUserInfoCommand;
import fr.utc.lo23.sharutc.controler.command.music.*;
import fr.utc.lo23.sharutc.controler.command.player.PlayIncomingMusicCommand;
import fr.utc.lo23.sharutc.controler.command.player.SendMusicToPlayCommand;
import fr.utc.lo23.sharutc.controler.command.search.InstallRemoteMusicsCommand;
import fr.utc.lo23.sharutc.controler.command.search.IntegrateMusicSearchCommand;
import fr.utc.lo23.sharutc.controler.command.search.PerformMusicSearchCommand;
import fr.utc.lo23.sharutc.controler.command.search.SendMusicsCommand;
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
 * {@inheritDoc}
 */
@Singleton
public class MessageHandlerImpl implements MessageHandler {

    private static final Logger log = LoggerFactory
            .getLogger(MessageHandlerImpl.class);
    private final AppModel appModel;
    private final MessageParser messageParser;
    private final MusicService musicService;
    private final UserService userService;
    private Command command = null;
    public MessageType previousMessageType = null; // this attribute is used only for messages using conversationId

    @Inject
    public MessageHandlerImpl(AppModel appModel, MessageParser messageParser,
            MusicService musicService, UserService userService) {
        this.appModel = appModel;
        this.messageParser = messageParser;
        this.musicService = musicService;
        this.userService = userService;
    }
    // all command interfaces used by network service
    @Inject
    private AddCommentCommand addCommentCommand;
    @Inject
    private EditCommentCommand editCommentCommand;
    @Inject
    private InstallRemoteMusicsCommand installRemoteMusicsCommand;
    @Inject
    private IntegrateUserInfoAndReplyCommand integrateUserInfoAndReplyCommand;
    @Inject
    private IntegrateUserInfoCommand integrateUserInfoCommand;
    @Inject
    private IntegrateDisconnectionCommand integrateDisconnectionCommand;
    @Inject
    private IntegrateMusicSearchCommand integrateMusicSearchCommand;
    @Inject
    private IntegrateRemoteCatalogCommand integrateRemoteCatalogCommand;
    @Inject
    private IntegrateRemoteTagMapCommand integrateRemoteTagMapCommand;
    @Inject
    private PerformMusicSearchCommand performMusicSearchCommand;
    @Inject
    private PlayIncomingMusicCommand playIncomingMusicCommand;
    @Inject
    private RemoveCommentCommand removeCommentCommand;
    @Inject
    private SendCatalogCommand sendCatalogCommand;
    @Inject
    private SendMusicsCommand sendMusicsCommand;
    @Inject
    private SendMusicToPlayCommand sendMusicToPlayCommand;
    @Inject
    private SendTagMapCommand sendTagMapCommand;
    @Inject
    private SetScoreCommand setScoreCommand;
    @Inject
    private UnsetScoreCommand unsetScoreCommand;
    // more...

    /**
     * {inheritDoc}
     */
    @Override
    public void handleMessage(String string) {
        command = null;
        log.info("handleMessage ...");
        //log.info("handleMessage : {}", string); // slow down computers when message contains a mp3 file
        final Message incomingMessage = messageParser.fromJSON(string);
        if (incomingMessage != null) {
            try {
                messageParser.read(incomingMessage);
                // searching which command to execute following message type
                if (messageParser.getSource() != null) {
                    log.info("Handling message '{}' from '{}'", incomingMessage.getType().name(), messageParser.getSource().getDisplayName());
                } else {
                    log.info("Handling message '{}' from unknown peer", incomingMessage.getType().name());
                }

                switch (incomingMessage.getType()) {
                    case MUSIC_GET_CATALOG:
                        sendCatalogCommand.setConversationId((Long) messageParser.getValue(Message.CONVERSATION_ID));
                        sendCatalogCommand.setPeer(messageParser.getSource());
                        command = sendCatalogCommand;
                        break;
                    case MUSIC_SEND_CATALOG:
                        if (isMessageForCurrentConversation() || getPreviousMessageType().equals(MessageType.MUSIC_GET_TO_PLAY)) {
                            integrateRemoteCatalogCommand.setPeer(messageParser.getSource());
                            integrateRemoteCatalogCommand.setCatalog((Catalog) messageParser.getValue(Message.CATALOG));
                            command = integrateRemoteCatalogCommand;
                        }
                        break;
                    case TAG_GET_MAP:
                        sendTagMapCommand.setConversationId((Long) messageParser.getValue(Message.CONVERSATION_ID));
                        sendTagMapCommand.setPeer(messageParser.getSource());
                        command = sendTagMapCommand;
                        break;
                    case TAG_MAP:
                        // we must check the conversation ID, the user may have left the cloud of tags screen
                        if (isMessageForCurrentConversation() || getPreviousMessageType().equals(MessageType.MUSIC_GET_TO_PLAY)) {
                            integrateRemoteTagMapCommand.setTagMap((TagMap) messageParser.getValue(Message.TAG_MAP));
                            command = integrateRemoteTagMapCommand;
                        }
                        break;
                    case COMMENT_ADD:
                        addCommentCommand.setAuthorPeer((Peer) messageParser.getValue(Message.AUTHOR_PEER));
                        addCommentCommand.setMusic((Music) messageParser.getValue(Message.MUSIC_ID));
                        addCommentCommand.setOwnerPeer((Peer) messageParser.getValue(Message.OWNER_PEER));
                        addCommentCommand.setComment((String) messageParser.getValue(Message.COMMENT));
                        command = addCommentCommand;
                        break;
                    case EDIT_COMMENT:
                        editCommentCommand.setAuthorPeer((Peer) messageParser.getValue(Message.AUTHOR_PEER));
                        editCommentCommand.setComment((String) messageParser.getValue(Message.COMMENT));
                        editCommentCommand.setCommentId((Integer) messageParser.getValue(Message.COMMENT_ID));
                        editCommentCommand.setMusic((Music) messageParser.getValue(Message.MUSIC));
                        editCommentCommand.setOwnerPeer((Peer) messageParser.getValue(Message.OWNER_PEER));
                        command = editCommentCommand;
                        break;
                    case COMMENT_REMOVE:
                        removeCommentCommand.setCommentId((Integer) messageParser.getValue(Message.COMMENT_ID));
                        removeCommentCommand.setMusic((Music) messageParser.getValue(Message.MUSIC));
                        removeCommentCommand.setPeer((Peer) messageParser.getValue(Message.AUTHOR_PEER));
                        command = removeCommentCommand;
                        break;
                    case SCORE_SET:
                        setScoreCommand.setMusic((Music) messageParser.getValue(Message.MUSIC));
                        setScoreCommand.setPeer((Peer) messageParser.getValue(Message.AUTHOR_PEER));
                        setScoreCommand.setScore((Integer) messageParser.getValue(Message.SCORE));
                        command = setScoreCommand;
                        break;
                    case SCORE_UNSET:
                        unsetScoreCommand.setMusic((Music) messageParser.getValue(Message.MUSIC));
                        unsetScoreCommand.setPeer(messageParser.getSource());
                        command = unsetScoreCommand;
                        break;
                    case MUSIC_SEARCH:
                        performMusicSearchCommand.setConversationId((Long) messageParser.getValue(Message.CONVERSATION_ID));
                        performMusicSearchCommand.setPeer(messageParser.getSource());
                        performMusicSearchCommand.setSearchCriteria((SearchCriteria) messageParser.getValue(Message.SEARCH));
                        command = performMusicSearchCommand;
                        break;
                    case MUSIC_RESULTS:
                        if (isMessageForCurrentConversation() || getPreviousMessageType().equals(MessageType.MUSIC_GET_TO_PLAY)) {
                            integrateMusicSearchCommand.setResultsCatalog((Catalog) messageParser.getValue(Message.CATALOG));
                            command = integrateMusicSearchCommand;
                        }
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
                        sendMusicToPlayCommand.setConversationId((Long) messageParser.getValue(Message.CONVERSATION_ID));
                        sendMusicToPlayCommand.setPeer(messageParser.getSource());
                        sendMusicToPlayCommand.setMusic(appModel.getLocalCatalog().findMusicById((Long) messageParser.getValue(Message.MUSIC_ID)));
                        command = sendMusicToPlayCommand;
                        break;
                    case MUSIC_SEND_TO_PLAY:
                        if (isMessageForCurrentConversation()) {
                            playIncomingMusicCommand.setMusic((Music) messageParser.getValue(Message.MUSIC));
                            command = playIncomingMusicCommand;
                        }
                        break;
                    case USER_INFO:
                        integrateUserInfoCommand.setUserInfo((UserInfo) messageParser.getValue(Message.USER_INFO));
                        command = integrateUserInfoCommand;
                        break;
                    case CONNECTION:
                        integrateUserInfoAndReplyCommand.setUserInfo((UserInfo) messageParser.getValue(Message.USER_INFO));
                        command = integrateUserInfoAndReplyCommand;
                        break;
                    case CONNECTION_RESPONSE:
                        integrateUserInfoCommand.setUserInfo((UserInfo) messageParser.getValue(Message.USER_INFO));
                        command = integrateUserInfoCommand;
                        break;
                    case DISCONNECT:
                        integrateDisconnectionCommand.setPeerId(messageParser.getSource().getId());
                        command = integrateDisconnectionCommand;
                        break;
                    case HEARTBEAT:
                        command = null;
                        break;
                    default:
                        command = null;
                        log.warn("Missing command : {}", incomingMessage.getType().name());
                        break;
                }
                final Command commandToExecute = command;
                if (commandToExecute != null) {
                    Thread thread = new Thread(commandToExecute.getClass().getSimpleName()) {
                        @Override
                        public void run() {
                            log.info("Running new command : {}", commandToExecute.getClass().getName());
                            commandToExecute.execute();
                        }
                    };
                    thread.start();
                }
            } catch (Exception ex) {
                log.error(ex.toString());
            }
            messageParser.resetParser();
        }
    }

    /**
     * check if the current conversation id is equal to the message conversation
     * id
     *
     * @return true if the current conversation id is equal to the message
     * conversation id, else false.
     */
    private boolean isMessageForCurrentConversation() {
        return appModel.getCurrentConversationId().equals(messageParser.getValue(Message.CONVERSATION_ID));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
     public void setPreviousMessageType(MessageType newMessageType) {
        previousMessageType = newMessageType; 
    }
    
     /**
     * {@inheritDoc}
     */
     @Override
     public MessageType getPreviousMessageType() {
        return previousMessageType;
    }
}

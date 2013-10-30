package fr.utc.lo23.sharutc.controler.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class Message {

    private static final Logger log = LoggerFactory
            .getLogger(Message.class);
    // list of constants used in all the messages, describes each value that is 
    // to be set in a message to find it at reading
    /**
     *
     */
    public final static String CONVERSATION_ID = "CONVERSATION_ID";
    /**
     *
     */
    public final static String OWNER_PEER_ID = "OWNER_PEER_ID";
    /**
     *
     */
    public final static String AUTHOR_PEER_ID = "AUTHOR_PEER_ID";
    /**
     *
     */
    public final static String COMMENT = "COMMENT";
    /**
     *
     */
    public final static String MUSIC_ID = "MUSIC";
    /**
     *
     */
    public final static String TAG_MAP = "TAG_MAP";
    /**
     *
     */
    public final static String CATALOG = "CATALOG";
    // more...
    /**
     * MessageType helps to read the content of the message
     */
    private MessageType type;
    /**
     * The content of the message in JSON format
     */
    private String content;
    /**
     * Source of message
     */
    private Long fromPeerId;
    /**
     * used to filter messages coming too late, the view makes this value
     * changes in the app., networkService has only to copy this value here
     * before sending it (not in each message, answer must re-use the value
     * contained in the message)
     */
    private Long conversationId;

    /**
     *
     * @param fromPeerId
     * @param messageType
     * @param content
     */
    public Message(long fromPeerId, MessageType messageType, String content) {
        this.type = messageType;
        this.content = content;
        this.fromPeerId = fromPeerId;
    }

    /**
     *
     * @param fromPeerId
     * @param messageType
     * @param content
     * @param conversationId
     */
    public Message(long fromPeerId, MessageType messageType, String content, Long conversationId) {
        this.type = messageType;
        this.content = content;
        this.fromPeerId = fromPeerId;
        this.conversationId = conversationId;
    }

    /**
     *
     * @return
     */
    public MessageType getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(MessageType type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     *
     * @return
     */
    public Long getFromPeerId() {
        return fromPeerId;
    }

    /**
     *
     * @param fromPeerId
     */
    public void setFromPeerId(Long fromPeerId) {
        this.fromPeerId = fromPeerId;
    }

    /**
     *
     * @return
     */
    public Long getConversationId() {
        return conversationId;
    }

    /**
     *
     * @param conversationId
     */
    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
}

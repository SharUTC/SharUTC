package fr.utc.lo23.sharutc.controler.network;

import java.util.HashMap;
import java.util.Map;

/**
 * List of constants used in all the messages, describes each value that is to
 * be set in a message to find it at reading
 */
public class Message {

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
    public final static String OWNER_PEER = "OWNER_PEER";
    /**
     *
     */
    public final static String AUTHOR_PEER = "AUTHOR_PEER";
    /**
     *
     */
    public final static String COMMENT = "COMMENT";
    /**
     * 
     */
    public final static String COMMENT_ID = "COMMENT_ID";
    /**
     *
     */
    public final static String MUSIC_ID = "MUSIC_ID";
    /**
     *
     */
    public final static String MUSIC = "MUSIC";
    /**
     *
     */
    public final static String TAG_MAP = "TAG_MAP";
    /**
     *
     */
    public final static String CATALOG = "CATALOG";
    /**
     * User info sent in the connection
     */
    public final static String USER_INFO = "USER_INFO";
    /**
     * Search criteria used for all searches
     */
    public final static String SEARCH = "SEARCH";
    /**
     *  a music score (integer)
     */
    public final static String SCORE = "SCORE";
    // more...
    /**
     * MessageType helps to read the content of the message
     */
    private MessageType type;
    /**
     * The content of the message in JSON format
     */
    private Map<String, Object> content;
    /**
     * Source of message
     */
    private Long fromPeerId;

    public Message() {
    }

    /**
     *
     * @param fromPeerId
     * @param messageType
     * @param content
     * @param conversationId
     */
    public Message(long fromPeerId, MessageType messageType, Map<String, Object> content) {
        this.type = messageType;
        this.content = content;
        this.fromPeerId = fromPeerId;
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
    public Map<String, Object> getContent() {
        return content;
    }

    /**
     *
     * @param content
     */
    public void setContent(HashMap<String, Object> content) {
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

    @Override
    public String toString() {
        return "Message{" + "type=" + type + ", content=" + content + ", fromPeerId=" + fromPeerId + "}";
    }
}

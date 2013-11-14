package fr.utc.lo23.sharutc.controler.network;

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
    public final static String COMMENT = "COMMENT";
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
     *
     * @param fromPeerId
     * @param messageType
     * @param content
     * @param conversationId
     */
    public Message(long fromPeerId, MessageType messageType, String content) {
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

    @Override
    public String toString() {
        return "Message{" + "type=" + type + ", content=" + content + ", fromPeerId=" + fromPeerId + "}";
    }
}

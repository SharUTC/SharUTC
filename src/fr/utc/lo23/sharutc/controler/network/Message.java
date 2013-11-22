package fr.utc.lo23.sharutc.controler.network;

import java.util.HashMap;
import java.util.Map;

/**
 * List of constants used in all the messages.
 * <p>
 * It describes each value that can be set in a message to find it at reading.
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

    /**
     * Dummy constructor.
     * <p>
     * This constructor is used by the jackson library when deserializing.
     * It is recommended the other constructor for any other use.
     */
    public Message() {
    }

    /**
     * Create a new message and set its attributes.
     *
     * @see MessageType
     * @param fromPeerId the sender's peerId
     * @param messageType the message type
     * @param content a Map<String, Object> where the String is taken from the
     * static String defined on Message and Object is the associated value.
     */
    public Message(long fromPeerId, MessageType messageType, Map<String, Object> content) {
        this.fromPeerId = fromPeerId;
        this.type = messageType;
        this.content = content;
    }

    /**
     * Return the message's type.
     *
     * @return the type of the message
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Change the message's type.
     *
     * @param type the new type of the message
     */
    public void setType(MessageType type) {
        this.type = type;
    }

    /**
     * Return the content of the message
     *
     * @return the content of the message as a Map<String, Object>.
     */
    public Map<String, Object> getContent() {
        return content;
    }

    /**
     * Change the message's content map.
     *
     * @param content a HashMap<String, Object> with the new content.
     */
    public void setContent(HashMap<String, Object> content) {
        this.content = content;
    }

    /**
     * The peerId of the message's sender.
     *
     * @return a Long containing the sender's peerId.
     */
    public Long getFromPeerId() {
        return fromPeerId;
    }

    /**
     * Change the peerId of the sender.
     *
     * @param fromPeerId the new sender's peerId.
     */
    public void setFromPeerId(Long fromPeerId) {
        this.fromPeerId = fromPeerId;
    }

    /**
     * Give a displayable String representation of the message.
     *
     * @return a String representing the message
     */
    @Override
    public String toString() {
        return "Message{" + "type=" + type + ", content=" + content + ", fromPeerId=" + fromPeerId + "}";
    }
}

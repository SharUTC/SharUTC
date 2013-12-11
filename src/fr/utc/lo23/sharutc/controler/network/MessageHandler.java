package fr.utc.lo23.sharutc.controler.network;

/**
 * The purpose of this class is to read incoming message as Json String and then
 * to execute the command relative to this message.
 */
public interface MessageHandler {
    /**
     * Read and parse a Json String to a Message object, find and run the
     * appropriate command in a new thread.
     *
     * @param string a Json String containing a Message
     */
    public void handleMessage(String string);
    
    /**
     * Sets the previousMessageType attribute with type of latest message (with conversationId) sent
     * @param newMessageType Type of most recent message with conversationId
     */
    public void setPreviousMessageType(MessageType newMessageType);
    
     /**
     * Gets the previousMessageType attribute which contains type of latest message (with conversationId) sent
     * @return Type of most recent message with conversationId
     */
    public MessageType getPreviousMessageType();
}

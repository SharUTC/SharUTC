package fr.utc.lo23.sharutc.controler.network;

import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 * Used to write or read a Message. Used to read a message and ease access to
 * its values. A Message must be read before using any other methods
 */
public interface MessageParser {

    /**
     * Read and parse an incoming message
     *
     * @param incomingMessage the message to read and parse
     */
    public void read(Message incomingMessage);

    /**
     * Write a message by specifying its type and content, includes
     * automatically the peerId of the connected user
     *
     * @param messageType the type of the message to specify which command run
     * after reading
     * @param content the different values of the message with a key
     * @return
     */
    public Message write(MessageType messageType, Object[][] content);

    /**
     * Return the Peer who send the parsed message
     *
     * @return the Peer who send the parsed message
     */
    public Peer getSource();

    /**
     * Return the value associated to the key in the message
     *
     * @param field the key of the value to get
     * @return the value associated to the key in the message, or null if not
     * found (shouldn't happen)
     */
    public Object getValue(String field);

    /**
     *
     * Only for write purpose. Set the source of the message that is
     * uatomatically included when the write method is used
     *
     * @param localPeerId the source of the message that is uatomatically
     * included when the write method is used
     */
    public void setFromPeerId(long localPeerId);

    /**
     * Only for read purpose. return the conversation id included in the lattest
     * parsed message, or null if no conversation id is included
     *
     * @return the conversation id included in the lattest parsed message, or
     * null if no conversation id is included
     */
    public Long getConversationId();
}

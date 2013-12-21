package fr.utc.lo23.sharutc.controler.network;

import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 * Used to write or read a Message.
 * <p>
 * Used to read a message and ease access to its values. A Message must be read
 * before using any other methods
 *
 * @see Message
 */
public interface MessageParser {

    /**
     * Read and parse an incoming message.
     *
     * @param incomingMessage the message to read and parse
     */
    public void read(Message incomingMessage);

    /**
     * Write a message by specifying its type and content.
     * <p>
     * Automatically includes the peerId of the connected user.
     *
     * @param messageType the type of the message to specify which command run
     * after reading
     * @param content the different values of the message with a key
     * @return the message build with given informations
     */
    public Message write(MessageType messageType, Object[][] content);

    /**
     * Return the Peer who send the parsed message.
     *
     * @return the Peer who send the parsed message
     */
    public Peer getSource();

    /**
     * Return the value associated to the key in the message.
     *
     * @param field the key of the value to get
     * @return the value associated to the key in the message, or null if not
     * found (shouldn't happen)
     */
    public Object getValue(String field);

    /**
     * Return a message object from a JSON String.
     *
     * @param json the JSON String to parse
     * @return a Message object from a JSON String
     */
    public Message fromJSON(String json);

    /**
     * Serialize a message to a JSON String.
     *
     * @param message the Message to transform into a JSON String
     * @return the parsed JSON String
     */
    public String toJSON(Message message);

    /**
     * Reset message and content, used after processing a Message.
     */
    public void resetParser();
}

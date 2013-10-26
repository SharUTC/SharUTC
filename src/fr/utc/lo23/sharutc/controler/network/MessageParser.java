package fr.utc.lo23.sharutc.controler.network;

import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 *
 * @author Arnaud
 */
public interface MessageParser {

    /**
     *
     * @return
     */
    public Peer getSource();

    /**
     *
     * @param field
     * @return
     */
    public Object getValue(String field);

    /**
     *
     * @param incomingMessage
     */
    public void read(Message incomingMessage);

    /**
     *
     * @param messageType
     * @param content
     * @return
     */
    public Message write(MessageType messageType, Object[][] content);

    /**
     *
     * @param localPeerId
     */
    public void setFromPeerId(long localPeerId);
}

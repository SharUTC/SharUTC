package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 * Compute local TagMap and send it to required peer
 */
public interface SendTagMapCommand extends Command {

    /**
     * Return the destination Peer
     *
     * @return the destination Peer
     */
    public Peer getPeer();

    /**
     * Set the destination Peer
     *
     * @param peer he destination Peer
     */
    public void setPeer(Peer peer);

    /**
     * Return the Conversation id
     *
     * @return the Conversation id
     */
    public Long getConversationId();

    /**
     * Set the conversation id
     *
     * @param conversationId the conversation id
     */
    public void setConversationId(Long conversationId);
}

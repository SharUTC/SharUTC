package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 *
 * Command for sending user's local catalog to a specific peer
 * 
 */
public interface SendCatalogCommand extends Command {

    /**
     *
     * Gets the peer who will receive the user's local catalog when this command (and those following) are done
     * 
     * @return
     */
    public Peer getPeer();

    /**
     *
     * Sets the peer who will receive the user's local catalog when this command (and those following) are done
     * 
     * @param peer
     */
    public void setPeer(Peer peer);

    /**
     *
     * Gets the current conversation ID
     * 
     * @return
     */
    public Long getConversationId();

    /**
     * 
     * Sets the current conversation ID
     * 
     * @param conversationId
     */
    public void setConversationId(Long conversationId);
}

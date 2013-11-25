package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.command.Command;

/**
 * Command that remove a peer from the connected peer list.
 */
public interface IntegrateDisconnectionCommand extends Command {

    /**
     * Give the peer id of the peer to be removed from the connected peer list
     *
     * @return Peer id of the peer about to be removed from the connected peer list
     */
    public long getPeerId();

    /**
     * Modify the peer id of the peer to be removed from the connected peer list
     *
     * @param peerId
     */
    public void setPeerId(long peerId);
}

package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 *
 */
public interface AddContactCommand extends Command {

    /**
     * Return the destination Peer
     *
     * @return the destination Peer
     */
    public Peer getPeer();

    /**
     * Set the destination Peer
     *
     * @param peer the destination Peer
     */
    public void setPeer(Peer peer);
}

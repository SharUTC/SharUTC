package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 *
 */
public interface DeleteContactCommand extends Command {

    /**
     * Return the Peer
     *
     * @return the Peer
     */
    public Peer getPeer();

    /**
     * Set the destination Peer
     *
     * @param peer
     */
    public void setPeer(Peer peer);
}

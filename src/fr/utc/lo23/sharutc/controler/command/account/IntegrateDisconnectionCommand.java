package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.command.Command;

/**
 *
 */
public interface IntegrateDisconnectionCommand extends Command {

    /**
     *
     * @return
     */
    public long getPeerId();

    /**
     *
     * @param peerId
     */
    public void setPeerId(long peerId);
}

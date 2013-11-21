package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 * Command for asking for a peer's catalog (+ clears current result catalog)
 */
public interface FetchRemoteCatalogCommand extends Command {

    /**
     * 
     * Gets the peer who will be asked for his catalog when the command is executed
     * 
     * @return
     */
    public Peer getPeer();

    /**
     *
     * Sets the peer who will be asked for his catalog when the command is executed
     * 
     * @param peer
     */
    public void setPeer(Peer peer);
}

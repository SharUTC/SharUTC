package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 *
 * Command for integrating a specific catalog to the appModel's remote catalog
 *
 */
public interface IntegrateRemoteCatalogCommand extends Command {

    /**
     *
     * Gets the catalog which will be integrated to the appModel's remote
     * catalog when the command is executed.
     *
     * @return
     */
    public Catalog getCatalog();

    /**
     *
     * Sets the catalog which will be integrated to the appModel's remote
     * catalog when the command is executed.
     *
     * @param catalog
     */
    public void setCatalog(Catalog catalog);

    // TODO: Is peer parameter still useful? If not, delete corresponding attribute and methods.
    /**
     *
     * Gets the peer
     *
     * @return
     */
    public Peer getPeer();

    /**
     *
     * Sets the peer
     *
     * @param peer
     */
    public void setPeer(Peer peer);
}

package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 *
 */
public interface AddContactToCategoryCommand extends Command {

    /**
     * Return the category
     * 
     * @return the category
     */
    public Category getCategory();

    /**
     * Set the category
     * 
     * @param category
     */
    public void setCategory(Category category);

    /**
     * Return the Peer
     *
     * @return the Peer
     */
    public Peer getPeer();

    /**
     * Set the Peer
     *
     * @param peer
     */
    public void setPeer(Peer peer);
}

package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Contact;

/**
 *
 */
public interface AddContactCommand extends Command {

    /**
     * Return the Peer
     *
     * @return the Peer
     */
    public Contact getContact();

    /**
     * Set the Peer
     *
     * @param peer
     */
    public void setContact(Contact contact) ;
}

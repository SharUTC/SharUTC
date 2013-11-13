package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Contact;

/**
 *
 */
public interface AddContactCommand extends Command {

    /**
     * Return the contact
     *
     * @return the contact
     */
    public Contact getContact();

    /**
     * Set the contact
     *
     * @param contact
     */
    public void setContact(Contact contact) ;
}

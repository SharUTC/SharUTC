package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;

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

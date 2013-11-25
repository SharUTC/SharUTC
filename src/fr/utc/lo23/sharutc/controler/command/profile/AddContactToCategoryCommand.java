package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;

/**
 * Command to add a contact to a specified cateory
 * This command manages the changes of categories for a contact, if it is needed
 * (particularly with the category Public : if the contact was previously
 * present in the category Public, we remove the contact from it)
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

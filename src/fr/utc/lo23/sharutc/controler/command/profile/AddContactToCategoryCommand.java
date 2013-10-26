package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;

/**
 *
 */
public interface AddContactToCategoryCommand extends Command {

    /**
     *
     * @return
     */
    public Category getCategory();

    /**
     *
     * @param category
     */
    public void setCategory(Category category);

    /**
     *
     * @return
     */
    public Contact getContact();

    /**
     *
     * @param contact
     */
    public void setContact(Contact contact);
}

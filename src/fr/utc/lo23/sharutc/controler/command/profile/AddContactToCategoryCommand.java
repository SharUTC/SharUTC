package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Category;

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
    public Long getContact();

    /**
     *
     * @param contact
     */
    public void setContact(Long contact);
}

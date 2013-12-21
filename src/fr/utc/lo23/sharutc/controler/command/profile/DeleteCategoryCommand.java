package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Category;

/**
 * Command to delete a categgory (we can't delete the category Public) This
 * command removes the contacts from this category and manages the changes of
 * categories. For instance, if a contact was only in this category, we add it
 * in the category Public.
 */
public interface DeleteCategoryCommand extends Command {

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
}

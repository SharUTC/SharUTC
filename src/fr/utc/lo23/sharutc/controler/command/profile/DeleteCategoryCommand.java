package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.userdata.Category;

/**
 *
 */
public interface DeleteCategoryCommand extends Command {

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
}

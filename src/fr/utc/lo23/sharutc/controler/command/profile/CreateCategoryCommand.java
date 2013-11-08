package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;

/**
 *
 */
public interface CreateCategoryCommand extends Command {

    /**
     * Return the category name
     * 
     * @return
     */
    public String getCategoryName();

    /**
     * Set the category name 
     * 
     * @param categoryName
     */
    public void setCategoryName(String categoryName);
}

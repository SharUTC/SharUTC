package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;

/**
 *
 */
public interface CreateCategoryCommand extends Command {

    /**
     *
     * @return
     */
    public String getCategoryName();

    /**
     *
     * @param categoryName
     */
    public void setCategoryName(String categoryName);
}

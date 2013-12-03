package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;

/**
 * Command to change a categoryName
 */
public interface EditCategoryNameCommand extends Command {

    /**
     * Return the category name
     * 
     * @return the category name
     */
    public String getCategoryName();

    /**
     * Set the category name 
     * 
     * @param categoryName
     */
    public void setCategoryName(String categoryName);
    
    /**
     * Return the category id
     * 
     * @return the category id
     */
    public Integer getCategoryId();

    /**
     * Set the category id 
     * 
     * @param categoryId
     */
    public void setCategoryId (Integer categoryId);
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.command.profile;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.userdata.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * implementation of the DeleteCategoryCommand
 */
public class DeleteCategoryCommandImpl implements DeleteCategoryCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddContactCommandImpl.class);
    private Category mCategory;
    final private UserService mUserService;

     /**
     * Constructor
     * @param userService
     */
    @Inject
    public DeleteCategoryCommandImpl(UserService userService) {
        this.mUserService = userService;
    }
    
     /**
     * {@inheritDoc}
     */
    @Override
    public Category getCategory() {
        return mCategory;
    }

     /**
     * {@inheritDoc}
     */
    @Override
    public void setCategory(Category category) {
        this.mCategory = category;
    }

     /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("DeleteCategoryCommand ...");
        mUserService.deleteCategory(mCategory);
        log.info("DeleteCategoryCommand DONE");
    }
    
}

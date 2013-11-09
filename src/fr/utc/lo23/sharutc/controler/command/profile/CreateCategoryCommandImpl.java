/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.command.profile;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mathilde
 */
public class CreateCategoryCommandImpl implements CreateCategoryCommand {
    
    private static final Logger log = LoggerFactory
            .getLogger(AddContactCommandImpl.class);
    private String mCategoryName;
    final private UserService mUserService;

     /**
     * {@inheritDoc}
     */
    @Inject
    public CreateCategoryCommandImpl(UserService mUserService) {
        this.mUserService = mUserService;
    }
    
     /**
     * {@inheritDoc}
     */
    @Override
    public String getCategoryName() {
        return mCategoryName;
    }
    
     /**
     * {@inheritDoc}
     */
    @Override
    public void setCategoryName(String categoryName) {
       this.mCategoryName = categoryName;
    }

     /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("CreateCategoryCommand ...");
        mUserService.createCategory(mCategoryName);
        log.info("CreateCategoryCommand DONE");
    }
    
}

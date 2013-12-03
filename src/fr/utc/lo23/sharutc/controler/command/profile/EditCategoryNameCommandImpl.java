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
 * implementation of the EditCategoryNameCommand
 */
public class EditCategoryNameCommandImpl implements EditCategoryNameCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddContactCommandImpl.class);
    private String mCategoryName;
    private Integer mCategoryId;
    final private UserService mUserService;
    
    /**
     * Constructor
     *
     * @param userService
     */
    @Inject
    public EditCategoryNameCommandImpl(UserService userService) {
        this.mUserService = userService;
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
    public Integer getCategoryId() {
        return mCategoryId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCategoryId(Integer categoryId) {
        this.mCategoryId = categoryId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("EditCategoryNameCommand ...");
        mUserService.setCategoryName(mCategoryId,mCategoryName);
        mUserService.saveProfileFiles();
        log.info("EditCategoryNameCommand DONE");
    }
    
}
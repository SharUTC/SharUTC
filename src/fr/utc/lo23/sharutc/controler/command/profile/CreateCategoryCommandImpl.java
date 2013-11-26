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
 * implementation of the CreateCategoryCommand
 */
public class CreateCategoryCommandImpl implements CreateCategoryCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddContactCommandImpl.class);
    private String mCategoryName;
    final private UserService mUserService;

    /**
     * Constructor
     *
     * @param userService
     */
    @Inject
    public CreateCategoryCommandImpl(UserService userService) {
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
    public void execute() {
        log.info("CreateCategoryCommand ...");
        mUserService.createCategory(mCategoryName);
        mUserService.saveProfileFiles();
        log.info("CreateCategoryCommand DONE");
    }
}

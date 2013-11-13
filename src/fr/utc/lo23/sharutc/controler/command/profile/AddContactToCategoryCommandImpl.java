/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.command.profile;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * {@inheritDoc}
 */
public class AddContactToCategoryCommandImpl implements AddContactToCategoryCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddContactCommandImpl.class);
    private Contact mContact;
    private Category mCategory;
    final private UserService mUserService;

     /**
     * {@inheritDoc}
     */
    @Inject
    public AddContactToCategoryCommandImpl(UserService mUserService) {
        this.mUserService = mUserService;
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
    public Contact getContact() {
        return mContact;
    }

     /**
     * {@inheritDoc}
     */
    @Override
    public void setContact(Contact contact) {
        this.mContact = contact;
    }

     /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("AddContactToCategoryCommand ...");
        mUserService.addContactToCategory(mContact,mCategory);
        log.info("AddContactToCategoryCommand DONE");
    }
    
}

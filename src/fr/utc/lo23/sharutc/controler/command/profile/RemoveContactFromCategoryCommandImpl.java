/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.command.profile;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mathilde
 */
public class RemoveContactFromCategoryCommandImpl implements RemoveContactFromCategoryCommand {
    
    private static final Logger log = LoggerFactory
            .getLogger(AddContactCommandImpl.class);
    private Peer mPeer;
    private Category mCategory;
    final private UserService mUserService;

     /**
     * {@inheritDoc}
     */
    @Inject
    public RemoveContactFromCategoryCommandImpl(UserService mUserService) {
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
    public Peer getPeer() {
        return mPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPeer(Peer peer) {
        this.mPeer = peer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("RemoveContactFromCategoryCommand ...");
        mUserService.removeContactFromCategory(mPeer,mCategory);
        log.info("RemoveContactFromCategoryCommand DONE");
    }
    
}

package fr.utc.lo23.sharutc.controler.command.profile;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * implementation of the DeleteContactCommand
 */
public class DeleteContactCommandImpl implements DeleteContactCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddContactCommandImpl.class);
    private Contact mContact;
    final private UserService mUserService;

     /**
     * Constructor
     * @param userService
     */
    @Inject
    public DeleteContactCommandImpl(UserService userService) {
        this.mUserService = userService;
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
        log.info("DeleteContactCommand ...");
        mUserService.deleteContact(mContact);
        log.info("DeleteContactCommand DONE");
    }
    
}

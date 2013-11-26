package fr.utc.lo23.sharutc.controler.command.profile;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * implementation of the AddContactCommand
 */
public class AddContactCommandImpl implements AddContactCommand {

    private static final Logger log = LoggerFactory
            .getLogger(AddContactCommandImpl.class);
    private Contact mContact;
    final private AppModel appModel;
    final private UserService mUserService;

    /**
     * Constructor
     *
     * @param appModel
     * @param userService
     */
    @Inject
    public AddContactCommandImpl(AppModel appModel, UserService userService) {
        this.appModel = appModel;
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
        log.info("AddContactCommand ...");
        mUserService.addContact(mContact);
        appModel.getProfile().getKnownPeerList().update(mContact.getUserInfo().toPeer());
        mUserService.saveProfileFiles();
        log.info("AddContactCommand DONE");
    }
}

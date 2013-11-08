package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;

/**
 *
 */
public interface DeleteContactCommand extends Command {

    /**
     *
     * @return
     */
    public Long getContact();

    /**
     *
     * @param contact
     */
    public void setContact(Long contact);
}

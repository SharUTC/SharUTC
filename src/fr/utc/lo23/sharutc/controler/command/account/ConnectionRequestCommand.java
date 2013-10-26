package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.command.Command;

/**
 *
 */
public interface ConnectionRequestCommand extends Command {

    /**
     *
     * @return
     */
    public String getLogin();

    /**
     *
     * @param login
     */
    public void setLogin(String login);

    /**
     *
     * @return
     */
    public String getPassword();

    /**
     *
     * @param password
     */
    public void setPassword(String password);
}

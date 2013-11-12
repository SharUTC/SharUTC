package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.command.Command;

/**
 * TODO : add comments
 */
public interface ConnectionRequestCommand extends Command {

    /**
     * Give the login to use for connection
     * @return a String corresponding to user login
     */
    public String getLogin();

    /**
     * Modify the login to use for connection
     * @param login String for user login
     */
    public void setLogin(String login);

    /**
     * Give the password to use for connection
     * @return a String corresponding to user password
     */
    public String getPassword();

    /**
     * Modify the password to use for connection
     * @param password String for user password
     */
    public void setPassword(String password);
}

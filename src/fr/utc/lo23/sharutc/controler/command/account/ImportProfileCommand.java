package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.command.Command;

/**
 *
 */
public interface ImportProfileCommand extends Command {

    /**
     *
     * @return
     */
    public String getPath();

    /**
     *
     * @param path
     */
    public void setPath(String path);
}

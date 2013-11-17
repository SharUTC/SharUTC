package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.command.Command;

/**
 *
 */
public interface ImportProfileCommand extends Command {

    /**
     * return a string of the path of the zip to import
     * @return 
     */
    public String getPath();

    /**
     *
     * @param path
     */
    public void setPath(String path);
    
    /**
     *
     * @return
     */
    public boolean isForce();

    /**
     *
     * @param force
     */
    public void setForce(boolean force);
}

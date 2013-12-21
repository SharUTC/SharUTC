package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.command.Command;

/**
 * Command to import a local profil
 */
public interface ImportProfileCommand extends Command {

    /**
     * return a string of the path of the zip to import
     *
     * @return
     */
    public String getPath();

    /**
     * change the path of the zip to import
     *
     * @param path
     */
    public void setPath(String path);

    /**
     * return true if the import will be forced if the profile to import already
     * exists otherwise false
     *
     * @return
     */
    public boolean isForce();

    /**
     * change the force value to force or not the import
     *
     * @param force
     */
    public void setForce(boolean force);
}

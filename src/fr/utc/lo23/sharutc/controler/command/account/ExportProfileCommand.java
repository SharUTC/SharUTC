package fr.utc.lo23.sharutc.controler.command.account;

import fr.utc.lo23.sharutc.controler.command.Command;

/**
 * Command to export an entire profile into a zip file
 */
public interface ExportProfileCommand extends Command {

    /**
     * Give the path of the folder to zip and export
     *
     * @return the String representing the source file path
     */
    public String getSrcFile();

    /**
     * Modify the path of the folder to zip and export
     *
     * @param srcFile string of the new source file path
     */
    public void setSrcFile(String srcFile);

    /**
     * Give the path of the writing destination
     *
     * @return the String representing the path of destination folder
     */
    public String getDestFolder();

    /**
     * Modify the path of the writing destination
     *
     * @param destFolder string of the new folder path
     */
    public void setDestFolder(String destFolder);
}

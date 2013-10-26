package fr.utc.lo23.sharutc.controler.service;

/**
 *

 */
public interface FileService {

    /**
     *
     * @param path
     * @param password
     */
    public void importFile(String path, String password);

    /**
     *
     * @param path
     */
    public void writeExportFile(String path);
}

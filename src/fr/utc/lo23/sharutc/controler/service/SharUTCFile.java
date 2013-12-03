package fr.utc.lo23.sharutc.controler.service;

import static fr.utc.lo23.sharutc.controler.service.FileService.FOLDER_MUSICS;
import java.io.File;

public enum SharUTCFile {

    CATALOG(FOLDER_MUSICS + File.separator + "musics.json"),
    RIGHTSLIST("rightslist.json"),
    PROFILE("profile.json");
    private final String filename;

    SharUTCFile(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
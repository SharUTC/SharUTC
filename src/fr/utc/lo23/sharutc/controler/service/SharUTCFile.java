package fr.utc.lo23.sharutc.controler.service;

import java.io.File;

public enum SharUTCFile {

    CATALOG("musics"+File.separator+"musics.json"), RIGHTSLIST("rightslist.json"), PROFILE("profile.json");
    private final String filename;

    SharUTCFile(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
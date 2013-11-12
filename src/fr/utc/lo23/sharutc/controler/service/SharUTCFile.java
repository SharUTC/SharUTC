package fr.utc.lo23.sharutc.controler.service;

public enum SharUTCFile {

    CATALOG("musics.json"), RIGHTSLIST("rightslist.json"), PROFILE("profile.json");
    private final String filename;

    SharUTCFile(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
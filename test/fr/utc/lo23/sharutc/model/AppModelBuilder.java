package fr.utc.lo23.sharutc.model;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.music.AddToLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.AddToLocalCatalogCommandImpl;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.RightsList;
import fr.utc.lo23.sharutc.model.userdata.ActivePeerList;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.Profile;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppModelBuilder {

    private static final Logger log = LoggerFactory
            .getLogger(AppModelBuilder.class);
    private static String TEST_MP3_FOLDER;
    private static final String[] TEST_MP3_FILENAMES = {"Sting & The Police - The Very Best Of Sting & The Police - 17 - Roxanne.mp3"};
    public static final String LOCAL_ACCOUNT_LOGIN = "test login";
    public static final String LOCAL_ACCOUNT_PASSWORD = "pwd";
    public static final String LOCAL_ACCOUNT_FIRSTNAME = "test";
    public static final String LOCAL_ACCOUNT_LASTNAME = "login";
    public static final int LOCAL_ACCOUNT_AGE = 23;
    public static final long LOCAL_ACCOUNT_PEER_ID = 0L;
    private final AppModel appModel;
    private Peer[] activePeers = {new Peer(1L, "Peer Mock (id=1)"), new Peer(2L, "Peer Mock (id=2)"), new Peer(3L, "Peer Mock (id=3)")};
    private AddToLocalCatalogCommand addToLocalCatalogCommand;
    private final MusicService musicService;

    @Inject
    public AppModelBuilder(AppModel appModel, MusicService musicService) {
        this.appModel = appModel;
        this.musicService = musicService;
        try {
            TEST_MP3_FOLDER = new File(".").getCanonicalPath() + "\\test\\mp3\\";
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    public void mockAppModel() {
        log.trace("Mocking AppModel ...");
        mockProfile();
        mockKnownPeerList();
        mockActivePeerList();
        mockCatalog();
        mockRights();
        createFolders();
        log.debug("AppModelMock READY");
    }

    public void clearAppModel() {
        log.trace("Clearing AppModel ...");
        appModel.setProfile(null);
        appModel.setActivePeerList(new ActivePeerList());
        appModel.setLocalCatalog(new Catalog());
        appModel.setRightsList(new RightsList());
        deleteFolders();
        log.debug("AppModelMock CLEARED");
    }

    private void mockKnownPeerList() {
        log.debug("mockKnownPeerList - Not supported yet.");
    }

    private void mockActivePeerList() {
        log.trace("MockingActivePeerList ...");
        for (Peer peer : activePeers) {
            appModel.getActivePeerList().update(peer);
        }
        log.trace("MockingActivePeerList DONE");
    }

    private void mockProfile() {
        log.trace("MockingProfile ...");
        Profile profile = new Profile();
        UserInfo userInfo = new UserInfo();
        userInfo.setPeerId(LOCAL_ACCOUNT_PEER_ID);
        userInfo.setLogin(LOCAL_ACCOUNT_LOGIN);
        userInfo.setPassword(LOCAL_ACCOUNT_PASSWORD);
        userInfo.setFirstName(LOCAL_ACCOUNT_FIRSTNAME);
        userInfo.setLastName(LOCAL_ACCOUNT_LASTNAME);
        userInfo.setAge(LOCAL_ACCOUNT_AGE);
        profile.setUserInfo(userInfo);
        appModel.setProfile(profile);
        log.trace("MockingProfile DONE");
    }

    private void mockCatalog() {
        log.trace("Mocking Catalog ...");
        appModel.setLocalCatalog(new Catalog());

        //  Catalog catalog = new Catalog();
        ArrayList<File> files = new ArrayList<File>();
        for (String mp3File : TEST_MP3_FILENAMES) {
            files.add(new File(TEST_MP3_FOLDER + mp3File));

            /*    try {
             Music music = fileService.readFile();
             music.addTag("ROCK");
             catalog.add(music);
             } catch (Exception ex) {
             ex.printStackTrace();
             log.error(ex.toString());
             }*/
        }
        addToLocalCatalogCommand = new AddToLocalCatalogCommandImpl(musicService);
        addToLocalCatalogCommand.setFiles(files);
        addToLocalCatalogCommand.execute();
        log.trace("Mocking Catalog DONE");
    }

    private void mockRights() {
        log.debug("mockRights - Not supported yet.");
    }

    private void createFolders() {
        log.trace("createFolders ...");
        String path = getSharUTCTestRootFolder() + File.separator + FileService.ROOT_FOLDER_USERS + File.separator + LOCAL_ACCOUNT_LOGIN + File.separator + FileService.FOLDER_MUSICS;
        log.debug("createFolders : {}", path);
        new File(path).mkdirs();
        log.trace("createFolders DONE");
    }

    private void deleteFolders() {
        log.trace("deleteFolders ...");
        String path = getSharUTCTestRootFolder();
        log.debug("deleteFolders : {}", path);
        deleteRecursive(path);
        log.trace("deleteFolders DONE");
    }

    private void deleteRecursive(String path) {
        log.trace("deleteRecursive : {}", path);
        File fileOrDir = new File(path);
        if (fileOrDir.isFile()) {
            fileOrDir.delete();
        } else if (fileOrDir.isDirectory()) {
            for (File file : fileOrDir.listFiles()) {
                try {
                    deleteRecursive(file.getCanonicalPath());
                } catch (IOException ex) {
                    log.error("deleteRecursive failed");
                }
            }
        }
    }

    private String getSharUTCTestRootFolder() {
        String testRootFolder = null;
        try {
            testRootFolder = new File(".").getCanonicalPath() + File.separator + FileService.APP_NAME;
        } catch (IOException ex) {
            log.error("getAccountFolder failed");
        }
        return testRootFolder;
    }
}

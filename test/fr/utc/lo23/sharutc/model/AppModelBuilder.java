package fr.utc.lo23.sharutc.model;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.RightsList;
import fr.utc.lo23.sharutc.model.userdata.ActivePeerList;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.Profile;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppModelBuilder {

    private static final Logger log = LoggerFactory
            .getLogger(AppModelBuilder.class);
    private static String TEST_MP3_FOLDER;
    private static final String[] TEST_MP3_FILENAMES = {"Sting & The Police - The Very Best Of Sting & The Police - 17 - Roxanne.mp3"};
    private final AppModel appModel;
    private final FileService fileService;
    private Peer[] activePeers = {new Peer(1L, "Peer Mock (id=1)"), new Peer(2L, "Peer Mock (id=2)"), new Peer(3L, "Peer Mock (id=3)")};

    @Inject
    public AppModelBuilder(AppModel appModel, FileService fileService) {
        this.appModel = appModel;
        this.fileService = fileService;
        try {
            TEST_MP3_FOLDER = new File(".").getCanonicalPath() + "\\test\\mp3\\";
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    public void mockAppModel() {
        mockProfile();
        mockKnownPeerList();
        mockActivePeerList();
        mockCatalog();
        mockRights();
    }

    public void clearAppModel() {
        appModel.setProfile(null);
        appModel.setActivePeerList(new ActivePeerList());
        appModel.setLocalCatalog(new Catalog());
        appModel.setRightsList(new RightsList());
    }

    private void mockKnownPeerList() {
        log.info("mockKnownPeerList - Not supported yet.");
    }

    private void mockActivePeerList() {
        log.info("MockingActivePeerList ...");
        for (Peer peer : activePeers) {
            appModel.getActivePeerList().update(peer);
        }
        log.info("MockingActivePeerList DONE");
    }

    private void mockProfile() {
        log.info("MockingProfile ...");
        Profile profile = new Profile();
        UserInfo userInfo = new UserInfo();
        userInfo.setPeerId(1l);
        profile.setUserInfo(userInfo);
        appModel.setProfile(profile);
        log.info("MockingProfile DONE (to be completed)");
    }

    private void mockCatalog() {
        log.info("Mocking Catalog ...");
        Catalog catalog = new Catalog();
        for (String mp3File : TEST_MP3_FILENAMES) {
            try {
                Music music = fileService.readFile(new File(TEST_MP3_FOLDER + mp3File));
                music.addTag("ROCK");
                catalog.add(music);
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error(ex.toString());
            }
        }
        appModel.setLocalCatalog(catalog);
        log.info("Mocking Catalog DONE");
    }

    private void mockRights() {
        log.info("mockRights - Not supported yet.");
    }
}

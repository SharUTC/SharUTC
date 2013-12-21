package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.account.ExportProfileCommand;
import fr.utc.lo23.sharutc.controler.command.account.ImportProfileCommand;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.FileService;
import static fr.utc.lo23.sharutc.controler.service.FileService.FOLDER_MUSICS;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.SharUTCFile;
import static fr.utc.lo23.sharutc.controler.service.SharUTCFile.CATALOG;
import static fr.utc.lo23.sharutc.controler.service.SharUTCFile.PROFILE;
import static fr.utc.lo23.sharutc.controler.service.SharUTCFile.RIGHTSLIST;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({ProfileImportExportTestModule.class})
public class ProfileImportExportTest {

    private static final Logger log = LoggerFactory
            .getLogger(ProfileImportExportTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private MusicService musicService;
    @Inject
    private UserService userService;
    @Inject
    private FileService fileService;
    @Inject
    private NetworkService networkService;
    @Inject
    private ExportProfileCommand exportProfileCommand;
    @Inject
    private ImportProfileCommand importProfileCommand;
    private AppModelBuilder appModelBuilder = null;

    @Before
    public void before() {
        log.trace("building appModel");
        if (appModelBuilder == null) {
            appModelBuilder = new AppModelBuilder(appModel, musicService, userService, fileService, networkService);
        }
        appModelBuilder.mockAppModel();

        String userName = "profileTest";
        String usersPath = fileService.getAppFolder() + FileService.ROOT_FOLDER_USERS;

        fileService.deleteFolderRecursively(usersPath);

        new File(usersPath + File.separator + userName).mkdir();
        new File(usersPath + File.separator + userName + File.separator + FOLDER_MUSICS).mkdirs();

        String musicJsonPath = usersPath + File.separator + userName + File.separator + "musics//musics.json";
        String profileJsonPath = usersPath + File.separator + userName + File.separator + "profile.json";
        String rightsJsonPath = usersPath + File.separator + userName + File.separator + "rightslist.json";

        try {
            new File(musicJsonPath).createNewFile();
            new File(profileJsonPath).createNewFile();
            new File(rightsJsonPath).createNewFile();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ProfileImportExportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void after() {
        log.trace("cleaning appModel");
        appModelBuilder.clearAppModel();
    }

    @Test
    public void exportProfileCommand() {
        String userName = "profileTest";
        String usersPath = fileService.getAppFolder() + FileService.ROOT_FOLDER_USERS;

        String dest = fileService.getAppFolder() + userName + ".zip";
        exportProfileCommand.setSrcFile(usersPath + File.separator + userName);
        exportProfileCommand.setDestFolder(dest);
        exportProfileCommand.execute();

        Assert.assertTrue("The zip of the profile has not been created",
                new File(dest).exists());

        //check the structure of the file
        boolean musicJsonExists = false;
        boolean profileJsonExists = false;
        boolean rightsJsonExists = false;

        ZipFile zf;
        try {
            zf = new ZipFile(dest);
            Enumeration entries = zf.entries();

            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                String entryName = ze.getName();
                if (entryName.equals(CATALOG.getFilename())) {
                    musicJsonExists = true;
                } else if (entryName.equals(PROFILE.getFilename())) {
                    profileJsonExists = true;
                } else if (entryName.equals(RIGHTSLIST.getFilename())) {
                    rightsJsonExists = true;
                }
            }

            Assert.assertTrue("music json has not been zipped", musicJsonExists);
            Assert.assertTrue("profile json has not been zipped", profileJsonExists);
            Assert.assertTrue("rightslist json has not been zipped", rightsJsonExists);

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ProfileImportExportTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*
         * Second part of test, import command test, to be sure that the tested file exist (
         */

        //String usersPath = fileService.getAppFolder() + FileService.ROOT_FOLDER_USERS;
        //String userName = "profileTest";
        String zipPath = fileService.getAppFolder() + userName + ".zip";

        if (new File(zipPath).exists()) {
            //test a simple import
            importProfileCommand.setPath(zipPath);
            importProfileCommand.setForce(false);
            importProfileCommand.execute();

            String musicJsonPath = usersPath + File.separator + userName + File.separator + "musics\\musics.json";
            String profileJsonPath = usersPath + File.separator + userName + File.separator + "profile.json";
            String rightsJsonPath = usersPath + File.separator + userName + File.separator + "rightslist.json";

            Assert.assertTrue("musics.json has not been created", new File(musicJsonPath).exists());
            Assert.assertTrue("profile.json has not been created", new File(profileJsonPath).exists());
            Assert.assertTrue("rightslist.json has not been created", new File(rightsJsonPath).exists());


            //test an import on an already existing profile
            String pouetForcePath = usersPath + File.separator + userName + File.separator + "pouetForce.txt";
            String trucPath = usersPath + File.separator + userName + File.separator + "musics\\truc.txt";

            try {
                new File(pouetForcePath).createNewFile();
                new File(trucPath).createNewFile();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ProfileImportExportTest.class.getName()).log(Level.SEVERE, null, ex);
            }

            //   String dest = fileService.getAppFolder() + userName + ".zip";
            exportProfileCommand.setSrcFile(usersPath + File.separator + userName);
            exportProfileCommand.setDestFolder(dest);
            exportProfileCommand.execute();

            importProfileCommand.setForce(true);
            importProfileCommand.execute();

            Assert.assertTrue("musics\\musics.json has not been created", new File(musicJsonPath).exists());
            Assert.assertTrue("profile.json has not been created", new File(profileJsonPath).exists());
            Assert.assertTrue("rights.json has not been created", new File(rightsJsonPath).exists());
            Assert.assertTrue("pouetForce.txt has not been created", new File(pouetForcePath).exists());
            Assert.assertTrue("musics\\truc.txt has not been created", new File(trucPath).exists());
        } else {
            Assert.assertTrue("Zip file doesn't exist", false);
        }
    }
}

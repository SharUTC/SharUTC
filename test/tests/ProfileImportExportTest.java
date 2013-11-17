package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.account.ExportProfileCommand;
import fr.utc.lo23.sharutc.controler.command.account.ImportProfileCommand;
import fr.utc.lo23.sharutc.controler.service.FileService;
import static fr.utc.lo23.sharutc.controler.service.FileService.FOLDER_MUSICS;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Olivier
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
    private ExportProfileCommand exportProfileCommand;
    @Inject
    private ImportProfileCommand importProfileCommand;
    private AppModelBuilder appModelBuilder = null;

    @Before
    public void before() {
        log.trace("building appModel");
        if (appModelBuilder == null) {
            appModelBuilder = new AppModelBuilder(appModel, musicService, userService);
        }
        appModelBuilder.mockAppModel();
        
        String userName = "profileTest";
        String usersPath = fileService.getAppFolder() + FileService.ROOT_FOLDER_USERS;
        
        fileService.deleteFolderRecursively(usersPath);
        
        new File(usersPath + File.separator + userName).mkdir();
        new File(usersPath + File.separator + userName + File.separator + FOLDER_MUSICS).mkdirs();
   
        String musicJsonPath = usersPath + File.separator + userName + File.separator + "musics.json";
        String profileJsonPath = usersPath + File.separator + userName + File.separator + "profile.json";
        String rightsJsonPath = usersPath + File.separator + userName + File.separator + "rights.json";
        String pouetPath = usersPath + File.separator + userName + File.separator + "musics//pouet.txt";
        
        try {
            new File(musicJsonPath).createNewFile();
            new File(profileJsonPath).createNewFile();
            new File(rightsJsonPath).createNewFile();
            new File(pouetPath).createNewFile();
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
        //TODO Test what is inside the zip
    }
    
    @Test
    public void importProfileCommand() {
        String usersPath = fileService.getAppFolder() + FileService.ROOT_FOLDER_USERS;
        String userName = "profileTest";
        String zipPath = fileService.getAppFolder() + userName + ".zip";
        
        if(new File(zipPath).exists()){
            //test a simple import
            importProfileCommand.setPath(zipPath);
            importProfileCommand.setForce(false);
            importProfileCommand.execute();
            
            String musicJsonPath = usersPath + File.separator + userName + File.separator + "musics.json";
            String profileJsonPath = usersPath + File.separator + userName + File.separator + "profile.json";
            String rightsJsonPath = usersPath + File.separator + userName + File.separator + "rights.json";
            String pouetPath = usersPath + File.separator + userName + File.separator + "musics\\pouet.txt";
            
            Assert.assertTrue("musics.json has not been created", new File(musicJsonPath).exists());
            Assert.assertTrue("profile.json has not been created", new File(profileJsonPath).exists());
            Assert.assertTrue("rights.json has not been created", new File(rightsJsonPath).exists());
            Assert.assertTrue("musics\\pouet.txt has not been created", new File(pouetPath).exists());
            
            
            //test an import on an already existing profile
            String pouetForcePath = usersPath + File.separator + userName + File.separator + "pouetForce.txt";
            String trucPath = usersPath + File.separator + userName + File.separator + "musics\\truc.txt";
            
            try {
                new File(pouetForcePath).createNewFile();
                new File(trucPath).createNewFile();
                new File(pouetPath).delete();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ProfileImportExportTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String dest = fileService.getAppFolder() + userName + ".zip";
            exportProfileCommand.setSrcFile(usersPath + File.separator + userName);
            exportProfileCommand.setDestFolder(dest);
            exportProfileCommand.execute();
            
            importProfileCommand.setForce(true);
            importProfileCommand.execute();
            
            Assert.assertTrue("musics.json has not been created", new File(musicJsonPath).exists());
            Assert.assertTrue("profile.json has not been created", new File(profileJsonPath).exists());
            Assert.assertTrue("rights.json has not been created", new File(rightsJsonPath).exists());
            Assert.assertFalse("musics\\pouet.txt has not been deleted", new File(pouetPath).exists());
            Assert.assertTrue("pouetForce.txt has not been created", new File(pouetForcePath).exists());
            Assert.assertTrue("musics\\truc.txt has not been created", new File(trucPath).exists());
        }
        else
            Assert.assertTrue("Zip file doesn't exist", false);
    }
}

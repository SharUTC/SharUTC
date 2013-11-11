package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.music.AddToLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveFromLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.network.NetworkServiceMock;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.io.File;
import java.util.Collection;
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
 */
@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({TagMapTestModule.class})
public class MusicServiceTest {
private static final Logger log = LoggerFactory
            .getLogger(MusicServiceTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private FileService fileService;
    @Inject
    private NetworkServiceMock networkService;
    @Inject
    private MusicService musicService;
    @Inject
    private AddToLocalCatalogCommand addToLocalCatalogCommand;
    @Inject
    private RemoveFromLocalCatalogCommand removeFromLocalCatalogCommand;
    private AppModelBuilder appModelBuilder = null;

    @Before
    public void before() {
        log.trace("building appModel");
        if (appModelBuilder == null) {
            appModelBuilder = new AppModelBuilder(appModel, fileService);
        }
        appModelBuilder.mockAppModel();
    }

    @After
    public void after() {
        log.trace("cleaning appModel");
        appModelBuilder.clearAppModel();
    }

    @Test
    public void addToLocalCatalogCommand() {
        int initialLocalCatalogSize = appModel.getLocalCatalog().size();
        
        Collection<File> filesTestCollection = null;
        File f1 = new File("path_du_fichier_1.mp3");
        File f2 = new File("path_du_fichier_2.mp3");
        File f3 = new File("path_du_fichier_3.mp3");
        filesTestCollection.add(f1);
        filesTestCollection.add(f2);
        filesTestCollection.add(f3);
        addToLocalCatalogCommand.setFiles(filesTestCollection);
        addToLocalCatalogCommand.execute();
        
        int finalLocalCatalogSize = appModel.getLocalCatalog().size();
        
        Assert.assertNotEquals(initialLocalCatalogSize, finalLocalCatalogSize);
    }
    
        @Test
    public void removeFromLocalCatalogCommand() {
        int initialLocalCatalogSize = appModel.getLocalCatalog().size();
        
        Collection<Music> musicsTestCollection = null;
        
        String TEST_MP3_FOLDER = "";
        try {
            TEST_MP3_FOLDER = new File(".").getCanonicalPath() + "\\test\\mp3\\";
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        
        String[] filenames = {"14 - End Credit Score.mp3", "Air - Moon Safari - Sexy Boy.mp3", "Sting & The Police - The Very Best Of Sting & The Police - 17 - Roxanne.mp3"};
        
        for (String mp3File : filenames) {
            try {
                Music music = fileService.readFile(new File(TEST_MP3_FOLDER + mp3File));
                musicsTestCollection.add(music);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(MusicServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        removeFromLocalCatalogCommand.setMusics(musicsTestCollection);
        removeFromLocalCatalogCommand.execute();
        
        int finalLocalCatalogSize = appModel.getLocalCatalog().size(); 
        Assert.assertNotEquals(initialLocalCatalogSize, finalLocalCatalogSize);
    }    
}
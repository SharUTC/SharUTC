package tests;

import ch.qos.logback.classic.util.ContextInitializer;
import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.music.AddToLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveFromLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Comment;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.Score;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
@GuiceJUnitRunner.GuiceModules({MusicServiceTestModule.class})
public class MusicServiceTest {

    private static final Logger log = LoggerFactory
            .getLogger(MusicServiceTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private FileService fileService;
    @Inject
    private MusicService musicService;
    @Inject
    private UserService userService;
    @Inject
    private AddToLocalCatalogCommand addToLocalCatalogCommand;
    @Inject
    private RemoveFromLocalCatalogCommand removeFromLocalCatalogCommand;
    @Inject
    private IntegrateRemoteCatalogCommand integrateRemoteCatalogCommand;
    private AppModelBuilder appModelBuilder = null;

    @Before
    public void before() {
        log.trace("building appModel");
        if (appModelBuilder == null) {
            appModelBuilder = new AppModelBuilder(appModel, musicService, userService);
        }
        appModelBuilder.mockAppModel();
    }

    @After
    public void after() {
        log.trace("cleaning appModel");
        appModelBuilder.clearAppModel();
    }

    // TODO : Add id tests regarding musics added/removed from local catalog. We are currently only testing catalog size to check success of these operations
    @Test
    public void addToLocalCatalogCommand() {

        appModel.getLocalCatalog().clear();
        Collection<File> filesTestCollection = new ArrayList<File>();

        String TEST_MP3_FOLDER = "";

        try {
            TEST_MP3_FOLDER = new File(".").getCanonicalPath() + File.separator + "test" + File.separator + "mp3" + File.separator;
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }

        String[] filenames = {"14 - End Credit Score.mp3", "Air - Moon Safari - Sexy Boy.mp3", "Sting & The Police - The Very Best Of Sting & The Police - 17 - Roxanne.mp3"};

        for (String mp3File : filenames) {
            try {
                File file = new File(TEST_MP3_FOLDER + mp3File);
                filesTestCollection.add(file);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(MusicServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        addToLocalCatalogCommand.setFiles(filesTestCollection);
        addToLocalCatalogCommand.execute();

        int finalLocalCatalogSize = appModel.getLocalCatalog().size();

        out.println(appModel.getLocalCatalog().getMusics().toString());

        Assert.assertEquals("3 music files have been successfully added to Local Catalog", 3, finalLocalCatalogSize);
    }

    @Test
    public void removeFromLocalCatalogCommand() {

        addToLocalCatalogCommand();
        Collection<Music> musicsTestCollection = new ArrayList<Music>();

        String TEST_MP3_FOLDER = "";
        try {
            TEST_MP3_FOLDER = new File(".").getCanonicalPath() + File.separator + "test" + File.separator + "mp3" + File.separator;
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }

        String[] filenames = {"14 - End Credit Score.mp3", "Air - Moon Safari - Sexy Boy.mp3", "Sting & The Police - The Very Best Of Sting & The Police - 17 - Roxanne.mp3"};

        for (String mp3File : filenames) {
            try {
                Music music = fileService.createMusicFromFile(new File(TEST_MP3_FOLDER + mp3File));
                musicsTestCollection.add(music);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(MusicServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        removeFromLocalCatalogCommand.setMusics(musicsTestCollection);
        removeFromLocalCatalogCommand.execute();

        int finalLocalCatalogSize = appModel.getLocalCatalog().size();
        Assert.assertEquals("3 music files have been successfully removed from Local Catalog", 0, finalLocalCatalogSize);
    }
    
    @Test
    public void saveAndLoadUserMusicFile(){
        Catalog catalogTest = appModel.getLocalCatalog();
        log.debug("catalogue chargé" + catalogTest.getMusics().size());
        catalogTest.getMusics().get(0).addComment(new Comment(2, "0 - Stylée", Long.MIN_VALUE, new Date()));
        catalogTest.getMusics().get(2).addComment(new Comment(3, "2 - Boarf", Long.MAX_VALUE, new Date()));
        catalogTest.getMusics().get(1).addScore(new Score(4, Long.MIN_VALUE));
        catalogTest.getMusics().get(2).addScore(new Score(2, Long.MAX_VALUE-1));
        
        musicService.saveUserMusicFile();
        musicService.loadUserMusicFile();
        Catalog catalogToTest = appModel.getLocalCatalog();
        int nbMusic = catalogToTest.getMusics().size();
        Assert.assertEquals("3 music files have been successfully saved and loaded from a user music file", 3, nbMusic);
        
        int nbComments = catalogToTest.getMusics().get(2).getComments().size() + catalogToTest.getMusics().get(0).getComments().size();
        Assert.assertEquals("2 comments have been successfully saved and loaded from a user music file", 2, nbComments);
        
        int nbScores = catalogToTest.getMusics().get(2).getScores().size() + catalogToTest.getMusics().get(1).getScores().size();
        Assert.assertEquals("2 scores have been successfully saved and loaded from a user music file", 2, nbScores);
        
        int scoreTest = catalogToTest.getMusics().get(2).getScore(Long.MAX_VALUE-1).getValue(); 
        Assert.assertEquals("good score value has been successfully saved and loaded from a user music file", 2, scoreTest);
        
        Long peerIdComment = catalogToTest.getMusics().get(0).getComment(Long.MIN_VALUE, 2).getAuthorPeerId();
        Assert.assertEquals("good peerId has been successfully saved and loaded from a user music file", (Long)Long.MIN_VALUE, peerIdComment);
        
    }
    
    @Test
    public void integrateRemoteCatalog(){
        
        int initialCatalogSize = appModel.getRemoteUserCatalog().size();
        
        Catalog testCatalog = new Catalog();
        List<Music> musicsTestCollection = new ArrayList<Music>();

        String TEST_MP3_FOLDER = "";
        try {
            TEST_MP3_FOLDER = new File(".").getCanonicalPath() + File.separator + "test" + File.separator + "mp3" + File.separator;
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }

        String[] filenames = {"14 - End Credit Score.mp3", "Air - Moon Safari - Sexy Boy.mp3", "Sting & The Police - The Very Best Of Sting & The Police - 17 - Roxanne.mp3"};

        for (String mp3File : filenames) {
            try {
                Music music = fileService.createMusicFromFile(new File(TEST_MP3_FOLDER + mp3File));
                musicsTestCollection.add(music);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(MusicServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        testCatalog.addAll(musicsTestCollection);
        
        integrateRemoteCatalogCommand.setCatalog(testCatalog); 
        integrateRemoteCatalogCommand.execute();
        
        Assert.assertEquals("musics from testCatalog have been successfully integrated", initialCatalogSize + 3, appModel.getRemoteUserCatalog().size());
    }
}

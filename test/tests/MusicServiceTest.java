package tests;

import ch.qos.logback.classic.util.ContextInitializer;
import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.music.AddToLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveFromLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.util.ArrayList;
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
            TEST_MP3_FOLDER = new File(".").getCanonicalPath() + "\\test\\mp3\\";
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
            TEST_MP3_FOLDER = new File(".").getCanonicalPath() + "\\test\\mp3\\";
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
}
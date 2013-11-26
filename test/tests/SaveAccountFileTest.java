package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({SaveAccountFileTestModule.class})
public class SaveAccountFileTest {

    private static final Logger log = LoggerFactory
            .getLogger(SaveAccountFileTest.class);
    private AppModelBuilder appModelBuilder = null;
    @Inject
    private AppModel appModel;
    @Inject
    private MusicService musicService;
    @Inject
    private UserService userService;

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

    @Test
    public void writeAccountFileMethods() {
        log.info("readWriteCatalogMethods appModel");
        musicService.saveUserMusicFile();
        musicService.saveUserRightsListFile();
        userService.saveProfileFiles();
        Assert.assertTrue(false); // TODO check content with code (list size, id values, not null and null values)
    }
}

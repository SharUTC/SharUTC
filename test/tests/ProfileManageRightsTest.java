package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.profile.CreateCategoryCommand;
import fr.utc.lo23.sharutc.controler.command.profile.ManageRightsCommand;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.domain.Rights;
import fr.utc.lo23.sharutc.model.userdata.Category;
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
@GuiceJUnitRunner.GuiceModules({ProfileManageRightsTestModule.class})
public class ProfileManageRightsTest {

    private static final Logger log = LoggerFactory
            .getLogger(ProfileManageRightsTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private FileService fileService;
    @Inject
    private NetworkService networkService;
    @Inject
    private MusicService musicService;
    @Inject
    private UserService userService;
    @Inject
    private CreateCategoryCommand createCategoryCommand;
    @Inject
    private ManageRightsCommand manageRightsCommand;
    private AppModelBuilder appModelBuilder = null;

    @Before
    public void before() {
        log.trace("building appModel");
        if (appModelBuilder == null) {
            appModelBuilder = new AppModelBuilder(appModel, musicService, userService, fileService, networkService);
        }
        appModelBuilder.mockAppModel();
    }

    @After
    public void after() {
        log.trace("cleaning appModel");
        appModelBuilder.clearAppModel();
    }

    @Test
    public void setRights() {
        Rights rights = appModel.getRightsList().getByMusicIdAndCategoryId(1L, Category.PUBLIC_CATEGORY_ID);
        Assert.assertTrue(rights.getMayReadInfo());
        Assert.assertTrue(rights.getMayListen());
        Assert.assertTrue(rights.getMayNoteAndComment());
        Category cat = appModel.getProfile().getCategories().findCategoryById(Category.PUBLIC_CATEGORY_ID);
        manageRightsCommand.setCategory(cat);
        manageRightsCommand.setMusic(appModel.getLocalCatalog().findMusicById(1l));
        manageRightsCommand.setMayReadInfo(false);
        manageRightsCommand.setMayListen(false);
        manageRightsCommand.setMayCommentAndScore(true);
        manageRightsCommand.execute();
        Rights rights2 = appModel.getRightsList().getByMusicIdAndCategoryId(1L, Category.PUBLIC_CATEGORY_ID);
        Assert.assertFalse(rights2.getMayReadInfo());
        Assert.assertFalse(rights2.getMayListen());
        Assert.assertTrue(rights2.getMayNoteAndComment());
    }
}

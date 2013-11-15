
package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.account.ExportProfileCommand;
import fr.utc.lo23.sharutc.controler.command.account.ImportProfileCommand;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import org.junit.After;
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
    public void exportProfileCommand() {
        //TODO test
    }
    
    @Test
    public void importProfileCommand() {
        //TODO test
    }
}


package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.account.ConnectionRequestCommand;
import fr.utc.lo23.sharutc.controler.command.account.DisconnectionCommand;
import fr.utc.lo23.sharutc.controler.service.UserService;
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
@GuiceJUnitRunner.GuiceModules({ProfileConnectionDisconnectionTestModule.class})
public class ProfileConnectionDisconnectionTest {
    private static final Logger log = LoggerFactory
            .getLogger(ProfileConnectionDisconnectionTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private UserService userService;
    @Inject
    private ConnectionRequestCommand connectionRequestCommand;
    @Inject
    private DisconnectionCommand disconnectionCommand;

    private AppModelBuilder appModelBuilder = null;
    
    @Before
    public void before() {
        log.trace("building appModel");
        if (appModelBuilder == null) {
            appModelBuilder = new AppModelBuilder(appModel, userService);
        }
        appModelBuilder.mockAppModel();
    }

    @After
    public void after() {
        log.trace("cleaning appModel");
        appModelBuilder.clearAppModel();
    }
    
    @Test
    public void connectionRequestCommand() {
        //TODO test
    }
    
    @Test void disconnectionCommand() {
        //TODO test
    }
}

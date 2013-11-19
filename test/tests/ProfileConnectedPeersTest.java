/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateUserInfoCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateDisconnectionCommand;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import static java.lang.System.out;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexandre
 */
@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({ProfileConnectedPeersTestModule.class})

public class ProfileConnectedPeersTest {
    private static final Logger log = LoggerFactory
            .getLogger(ProfileCategoriesAndContactsTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private UserService userService;
    @Inject
    private MusicService musicService;
    @Inject
    private IntegrateUserInfoCommand integrateConnectionCommand;
    @Inject
    private IntegrateDisconnectionCommand integrateDisconnectionCommand;
    
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
    
    /**
     *
     */
    @Test
    public void addUserCommand() {
        //Add first user
        UserInfo newUserInfo1 = new UserInfo();
        newUserInfo1.setLogin("LocalPeer Mock (id=4)");
        newUserInfo1.setPeerId(4L);
     // FIXME:    
    //    addUserCommand.setContact(newUserInfo1);
    //    addUserCommand.execute();
        
        //Add second user
        UserInfo newUserInfo2 = new UserInfo();
        newUserInfo2.setLogin("LocalPeer Mock (id=5)");
        newUserInfo2.setPeerId(5L);
        
   //     addUserCommand.setContact(newUserInfo2);
    //    addUserCommand.execute();     
        
        int activePeerListSize = appModel.getActivePeerList().getActivePeers().size();
        
        out.println(appModel.getActivePeerList().toString());
        Assert.assertEquals("2 users added to the list failed.", 6, activePeerListSize);
    }
    
    @Test
    public void integrateConnectionCommand(){
        //TODO Test integrateConnectionCommand
        Assert.assertTrue(true);
    }
    
    @Test
    public void integrateDisconnectionCommand(){
        // Removing Peer with ID = 2
        Peer removedPeer1 = new Peer(2L, "Peer Mock (id=2)");
        integrateDisconnectionCommand.setPeerId(removedPeer1.getId());
        integrateDisconnectionCommand.execute();
        
        // Removing Peer with ID = 3
        Peer removedPeer2 = new Peer(3L, "Peer Mock (id=3)");
        integrateDisconnectionCommand.setPeerId(removedPeer2.getId());
        integrateDisconnectionCommand.execute();
        
        int activePeerListSize = appModel.getActivePeerList().getActivePeers().size();
        
        Assert.assertEquals("2 Peers removed form the connected list failed.", 2, activePeerListSize);
    }
}

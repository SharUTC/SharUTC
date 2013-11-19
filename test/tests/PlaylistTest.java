/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.controler.command.player.AddToPlaylistCommand;
import fr.utc.lo23.sharutc.controler.command.player.RemoveFromPlaylistCommand;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.controler.service.PlayerService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelBuilder;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({PlaylistTestModule.class})
public class PlaylistTest {

    private static final Logger log = LoggerFactory
            .getLogger(MusicCommentTest.class);
    @Inject
    private AppModel appModel;
    @Inject
    private MusicService musicService;
    @Inject
    private UserService userService;
    @Inject
    private PlayerService playerService;
    @Inject
    private AddToPlaylistCommand addToPlaylistCommand;
    @Inject
    private RemoveFromPlaylistCommand removeFromPlaylistCommand;
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
    public void addToPlaylist() {
        ArrayList<Music> dummyMusics = new ArrayList<Music>();
        dummyMusics.addAll(appModel.getLocalCatalog().getMusics());       
        
        addToPlaylistCommand.setMusics(dummyMusics);
        addToPlaylistCommand.execute();
        
        Assert.assertNotNull("addToPlaylistCommand failed", playerService.getPlaylist().getMusics());
        Assert.assertEquals("addToPlaylistCommand failed", dummyMusics.size(), playerService.getPlaylist().getMusics().size());
    }

    /**
     *
     */
    @Test
    public void removeFromPlaylist() {
        ArrayList<Music> dummyMusics = new ArrayList<Music>();
        dummyMusics.addAll(appModel.getLocalCatalog().getMusics());
        
        addToPlaylistCommand.setMusics(dummyMusics);
        addToPlaylistCommand.execute();

        removeFromPlaylistCommand.setMusic(dummyMusics.get(0));
        removeFromPlaylistCommand.execute();

        Assert.assertNotNull("removeFromPlaylistCommand failed", playerService.getPlaylist().getMusics());
        
        ArrayList<Music> testMusics = new ArrayList<Music>();
        testMusics.addAll(1, dummyMusics);
        
        Assert.assertEquals("removeFromPlaylistCommand failed", playerService.getPlaylist().getMusics(), testMusics);
    }
}

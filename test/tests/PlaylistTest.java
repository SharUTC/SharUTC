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
    private AddToPlaylistCommand addToPlaylistCommand;
    @Inject
    private RemoveFromPlaylistCommand removeFromPlaylistCommand;
    private AppModelBuilder appModelBuilder = null;

    @Before
    public void before() {
        log.trace("building appModel");
        if (appModelBuilder == null) {
            appModelBuilder = new AppModelBuilder(appModel, musicService);
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
        String dummyMusicName = "Dummy Music ";
        ArrayList<Music> dummyMusics = new ArrayList<Music>();
        for (Integer i = 0; i < 10; i++) {
            Music dummyMusic = new Music();
            dummyMusic.setFileName(dummyMusicName.concat(i.toString()));
            dummyMusic.setFileByte(new Byte[0]);
            dummyMusic.setFileMissing(Boolean.FALSE);
            dummyMusics.add(dummyMusic);
        }
        
        addToPlaylistCommand.setMusics(dummyMusics);
        addToPlaylistCommand.execute();  
        
        Assert.assertNull("addToPlaylistCommand failed", addToPlaylistCommand.getMusics());
        Assert.assertEquals("addToPlaylistCommand failed", addToPlaylistCommand.getMusics(), dummyMusics);
    }

    /**
     *
     */
    @Test
    public void removeFromPlaylist() {
        String dummyMusicName = "Dummy Music ";
        ArrayList<Music> dummyMusics = new ArrayList<Music>();
        for (Integer i = 0; i < 10; i++) {
            Music dummyMusic = new Music();
            dummyMusic.setFileName(dummyMusicName.concat(i.toString()));
            dummyMusic.setFileByte(new Byte[0]);
            dummyMusic.setFileMissing(Boolean.FALSE);
            dummyMusics.add(dummyMusic);
        }

        Music dummyAddedMusic = new Music();
        dummyAddedMusic.setFileName("Dummy Added Music");
        dummyAddedMusic.setFileByte(new Byte[3]);
        dummyAddedMusic.setFileMissing(Boolean.FALSE);
        
        addToPlaylistCommand.setMusics(dummyMusics);
        addToPlaylistCommand.setMusic(dummyAddedMusic);
        addToPlaylistCommand.execute();

        removeFromPlaylistCommand.setMusics(dummyMusics);
        removeFromPlaylistCommand.execute();

        Assert.assertEquals("removeFromPlaylistCommand failed", removeFromPlaylistCommand.getMusics().get(0), dummyAddedMusic);
        Assert.assertNotNull("removeFromPlaylistCommand failed", removeFromPlaylistCommand.getMusics());
    }
}

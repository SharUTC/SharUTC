package fr.utc.lo23.sharutc.controler.service;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.GuiceJUnitRunner;
import fr.utc.lo23.sharutc.GuiceJUnitRunner.GuiceModules;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteTagMapCommand;
import fr.utc.lo23.sharutc.injection.CommandTestModule;
import fr.utc.lo23.sharutc.injection.ModelTestModule;
import fr.utc.lo23.sharutc.injection.ServiceTestModule;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ModelTestModule.class, ServiceTestModule.class, CommandTestModule.class})
public class SampleServiceMockTest {

    @Inject
    private AppModel appModel;
    @Inject
    private MusicService musicService;
    @Inject
    private IntegrateRemoteTagMapCommand integrateRemoteTagMapCommand;

    /**
     *
     */
    @Test
    public void tagMapMerge() {

        // first step, we create a TagMap instance, and work with it,
        // here I'm testing both merge method
        TagMap dummyTagMap = new TagMap();

        TagMap dummyTagMap1 = new TagMap();
        dummyTagMap1.merge("Rock", 5);
        dummyTagMap1.merge("Pop", 3);
        dummyTagMap1.merge("Rock", 5);
        dummyTagMap1.merge("Disco", 2000);

        dummyTagMap1.merge(dummyTagMap);
        // here is the first test, it's advised to always put a short  
        // description of the test to help other people to understand what is 
        // happening, don't hesitate to write a few comments
        Assert.assertEquals("TagMap merge method failed", dummyTagMap1,
                musicService.getLocalTagMap());
    }

    /**
     *
     */
    @Test
    public void tagMapIntegration() {
        // here I'm testing one of the commands of the application,
        // we set its attributes as we would do in the code
        //
        //TagMap dummyTagMap = new TagMap();
        //dummyTagMap.merge("Rock", 5);
        //dummyTagMap.merge("Pop", 3);
        //dummyTagMap.merge("Rock", 5);
        //dummyTagMap.merge("Disco", 2000);
        //
        //integrateRemoteTagMapCommand.setTagMap(dummyTagMap1);
        //
        // then we continue to simulate the application, here it should modify
        // the application model but for this it requires to write a few more 
        // code lines to first mock a complete and usable appModel with a mocked
        // account and catalog, that other developper could also reuse or 
        // complete at the beginning
        //
        //integrateRemoteTagMapCommand.execute();
        //
        //TagMap dummyTagMap1 = new TagMap();
        //dummyTagMap1.merge("Rock", 5);
        //dummyTagMap1.merge("Pop", 3);
        //dummyTagMap1.merge("Rock", 5);
        //dummyTagMap1.merge("Disco", 2000);
        //
        //Assert.assertEquals("IntegrateRemoteTagMapCommand failed", dummyTagMap1,
        //appModel.getNetworkTagMap());
        //
        // here is a second test, the same as the previous but with different
        // instances, it is highly preferable to create a new instance of the
        // TagMap here, don't use the previous instances
        //
        //TagMap dummyTagMap2 = new TagMap();
        //dummyTagMap2.merge("Rock", 10);
        //dummyTagMap2.merge("Pop", 3);
        //dummyTagMap2.merge("Jazz", 5);
        //integrateRemoteTagMapCommand.setTagMap(dummyTagMap2);
        //integrateRemoteTagMapCommand.execute();
        //
        //TagMap dummyTagMap3 = new TagMap();
        //dummyTagMap3.merge("Rock", 20);
        //dummyTagMap3.merge("Pop", 6);
        //dummyTagMap3.merge("Jazz", 5);
        //dummyTagMap.merge("Disco", 2000);
        //Assert.assertEquals("IntegrateRemoteTagMapCommand failed", dummyTagMap3,
        //appModel.getNetworkTagMap());
    }
}

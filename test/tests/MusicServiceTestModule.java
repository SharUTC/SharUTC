package tests;

import com.google.inject.AbstractModule;
import fr.utc.lo23.sharutc.controler.command.music.AddToLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.AddToLocalCatalogCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.RemoveFromLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveFromLocalCatalogCommandImpl;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.FileServiceMock;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.MusicServiceMock;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.controler.service.UserServiceMock;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelMock;

public class MusicServiceTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FileService.class).to(FileServiceMock.class);
        bind(UserService.class).to(UserServiceMock.class);
        bind(AppModel.class).to(AppModelMock.class);
        bind(MusicService.class).to(MusicServiceMock.class);
        bind(AddToLocalCatalogCommand.class).to(AddToLocalCatalogCommandImpl.class);
        bind(RemoveFromLocalCatalogCommand.class).to(RemoveFromLocalCatalogCommandImpl.class);
        requestInjection(this);
    }
}

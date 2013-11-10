package tests;

import com.google.inject.AbstractModule;
import fr.utc.lo23.sharutc.controler.command.music.SetScoreCommand;
import fr.utc.lo23.sharutc.controler.command.music.SetScoreCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.UnsetScoreCommand;
import fr.utc.lo23.sharutc.controler.command.music.UnsetScoreCommandImpl;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.network.NetworkServiceMock;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.FileServiceMock;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.MusicServiceMock;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.controler.service.UserServiceMock;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelMock;

public class MusicScoreTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FileService.class).to(FileServiceMock.class);
        bind(AppModel.class).to(AppModelMock.class);
        bind(UserService.class).to(UserServiceMock.class);
        bind(MusicService.class).to(MusicServiceMock.class);
        bind(NetworkService.class).to(NetworkServiceMock.class);
        bind(SetScoreCommand.class).to(SetScoreCommandImpl.class);
        bind(UnsetScoreCommand.class).to(UnsetScoreCommandImpl.class);
        requestInjection(this);
    }
}

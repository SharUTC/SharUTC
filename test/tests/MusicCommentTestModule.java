package tests;

import com.google.inject.AbstractModule;
import fr.utc.lo23.sharutc.controler.command.music.AddCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.AddCommentCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.EditCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.EditCommentCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.RemoveCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveCommentCommandImpl;
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

public class MusicCommentTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FileService.class).to(FileServiceMock.class);
        bind(AppModel.class).to(AppModelMock.class);
        bind(UserService.class).to(UserServiceMock.class);
        bind(MusicService.class).to(MusicServiceMock.class);
        bind(NetworkService.class).to(NetworkServiceMock.class);
        bind(AddCommentCommand.class).to(AddCommentCommandImpl.class);
        bind(EditCommentCommand.class).to(EditCommentCommandImpl.class);
        bind(RemoveCommentCommand.class).to(RemoveCommentCommandImpl.class);
        requestInjection(this);
    }
}

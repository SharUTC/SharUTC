package fr.utc.lo23.sharutc.injection;

import com.google.inject.AbstractModule;
import fr.utc.lo23.sharutc.controler.network.MessageParser;
import fr.utc.lo23.sharutc.controler.network.MessageParserImpl;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.network.NetworkServiceMock;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.FileServiceMock;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.MusicServiceMock;
import fr.utc.lo23.sharutc.controler.service.PlayerService;
import fr.utc.lo23.sharutc.controler.service.PlayerServiceMock;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.controler.service.UserServiceMock;

/**
 * 
 */
public class ServiceTestModule extends AbstractModule {

    /**
     *
     */
    @Override
    protected void configure() {
        bind(MusicService.class).to(MusicServiceMock.class);
        bind(UserService.class).to(UserServiceMock.class);
        bind(NetworkService.class).to(NetworkServiceMock.class);
        bind(FileService.class).to(FileServiceMock.class);
        bind(PlayerService.class).to(PlayerServiceMock.class);
        bind(MessageParser.class).to(MessageParserImpl.class);
    }
}

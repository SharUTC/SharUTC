package fr.utc.lo23.sharutc.injection;

import com.google.inject.AbstractModule;
import fr.utc.lo23.sharutc.controler.network.MessageHandler;
import fr.utc.lo23.sharutc.controler.network.MessageHandlerImpl;
import fr.utc.lo23.sharutc.controler.network.MessageParser;
import fr.utc.lo23.sharutc.controler.network.MessageParserImpl;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.network.NetworkServiceImpl;
import fr.utc.lo23.sharutc.controler.service.*;

/**
 *
 */
public class ServiceModule extends AbstractModule {

    /**
     *
     */
    @Override
    protected void configure() {
        bind(MusicService.class).to(MusicServiceImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(NetworkService.class).to(NetworkServiceImpl.class);
        bind(FileService.class).to(FileServiceImpl.class);
        bind(PlayerService.class).to(PlayerServiceImpl.class);
        bind(MessageParser.class).to(MessageParserImpl.class);
        bind(MessageHandler.class).to(MessageHandlerImpl.class);
    }
}

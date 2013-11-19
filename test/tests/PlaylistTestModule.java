/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import com.google.inject.AbstractModule;
import fr.utc.lo23.sharutc.controler.command.player.AddToPlaylistCommand;
import fr.utc.lo23.sharutc.controler.command.player.AddToPlaylistCommandImpl;
import fr.utc.lo23.sharutc.controler.command.player.RemoveFromPlaylistCommand;
import fr.utc.lo23.sharutc.controler.command.player.RemoveFromPlaylistCommandImpl;
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
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelMock;

public class PlaylistTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AddToPlaylistCommand.class).to(AddToPlaylistCommandImpl.class);
        bind(RemoveFromPlaylistCommand.class).to(RemoveFromPlaylistCommandImpl.class);
        bind(MessageParser.class).to(MessageParserImpl.class);
        bind(FileService.class).to(FileServiceMock.class);
        bind(AppModel.class).to(AppModelMock.class);
        bind(UserService.class).to(UserServiceMock.class);
        bind(PlayerService.class).to(PlayerServiceMock.class);
        bind(MusicService.class).to(MusicServiceMock.class);
        bind(NetworkService.class).to(NetworkServiceMock.class);
        requestInjection(this);
    }

}

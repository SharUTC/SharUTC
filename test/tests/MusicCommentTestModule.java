package tests;

import com.google.inject.AbstractModule;
import fr.utc.lo23.sharutc.controler.command.account.AccountCreationCommand;
import fr.utc.lo23.sharutc.controler.command.account.AccountCreationCommandImpl;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateBroadcastConnectionCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateBroadcastConnectionCommandImpl;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateConnectionCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateConnectionCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.AddCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.AddCommentCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.AddTagCommand;
import fr.utc.lo23.sharutc.controler.command.music.AddTagCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.AddToLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.AddToLocalCatalogCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.EditCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.EditCommentCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.FetchRemoteCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.FetchRemoteCatalogCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteCatalogCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteTagMapCommand;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteTagMapCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.RemoveCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveCommentCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.RemoveFromLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveFromLocalCatalogCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.RemoveTagCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveTagCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.SendCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.SendCatalogCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.SendTagMapCommand;
import fr.utc.lo23.sharutc.controler.command.music.SendTagMapCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.SetScoreCommand;
import fr.utc.lo23.sharutc.controler.command.music.SetScoreCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.ShowTagMapCommand;
import fr.utc.lo23.sharutc.controler.command.music.ShowTagMapCommandImpl;
import fr.utc.lo23.sharutc.controler.command.music.UnsetScoreCommand;
import fr.utc.lo23.sharutc.controler.command.music.UnsetScoreCommandImpl;
import fr.utc.lo23.sharutc.controler.command.player.PlayIncomingMusicCommand;
import fr.utc.lo23.sharutc.controler.command.player.PlayIncomingMusicCommandImpl;
import fr.utc.lo23.sharutc.controler.command.player.PlayMusicCommand;
import fr.utc.lo23.sharutc.controler.command.player.PlayMusicCommandImpl;
import fr.utc.lo23.sharutc.controler.command.player.SendMusicToPlayCommand;
import fr.utc.lo23.sharutc.controler.command.player.SendMusicToPlayCommandImpl;
import fr.utc.lo23.sharutc.controler.command.profile.AddContactCommand;
import fr.utc.lo23.sharutc.controler.command.profile.AddContactCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.DownloadMusicsCommand;
import fr.utc.lo23.sharutc.controler.command.search.DownloadMusicsCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.InstallRemoteMusicsCommand;
import fr.utc.lo23.sharutc.controler.command.search.InstallRemoteMusicsCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.IntegrateMusicSearchCommand;
import fr.utc.lo23.sharutc.controler.command.search.IntegrateMusicSearchCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.MusicSearchCommand;
import fr.utc.lo23.sharutc.controler.command.search.MusicSearchCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.PerformMusicSearchCommand;
import fr.utc.lo23.sharutc.controler.command.search.PerformMusicSearchCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.SendMusicsCommand;
import fr.utc.lo23.sharutc.controler.command.search.SendMusicsCommandImpl;
import fr.utc.lo23.sharutc.controler.network.MessageHandler;
import fr.utc.lo23.sharutc.controler.network.MessageHandlerImpl;
import fr.utc.lo23.sharutc.controler.network.MessageParser;
import fr.utc.lo23.sharutc.controler.network.MessageParserImpl;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.network.NetworkServiceMock;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.FileServiceMock;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.MusicServiceMock;
import fr.utc.lo23.sharutc.controler.service.PlayerService;
import fr.utc.lo23.sharutc.controler.service.PlayerServiceImpl;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.controler.service.UserServiceMock;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelMock;

public class MusicCommentTestModule extends TestModule {

    @Override
    protected void configure() {
        super.configure();
    }
}
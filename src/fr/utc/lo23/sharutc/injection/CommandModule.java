package fr.utc.lo23.sharutc.injection;

import com.google.inject.AbstractModule;
import fr.utc.lo23.sharutc.controler.command.account.AccountCreationCommand;
import fr.utc.lo23.sharutc.controler.command.account.AccountCreationCommandImpl;
import fr.utc.lo23.sharutc.controler.command.account.ConnectionRequestCommand;
import fr.utc.lo23.sharutc.controler.command.account.ConnectionRequestCommandImpl;
import fr.utc.lo23.sharutc.controler.command.account.DisconnectionCommand;
import fr.utc.lo23.sharutc.controler.command.account.DisconnectionCommandImpl;
import fr.utc.lo23.sharutc.controler.command.account.ExportProfileCommand;
import fr.utc.lo23.sharutc.controler.command.account.ExportProfileCommandImpl;
import fr.utc.lo23.sharutc.controler.command.account.ImportProfileCommand;
import fr.utc.lo23.sharutc.controler.command.account.ImportProfileCommandImpl;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateBroadcastConnectionCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateBroadcastConnectionCommandImpl;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateConnectionCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateConnectionCommandImpl;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateDisconnectionCommand;
import fr.utc.lo23.sharutc.controler.command.account.IntegrateDisconnectionCommandImpl;
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
import fr.utc.lo23.sharutc.controler.command.profile.AddContactToCategoryCommand;
import fr.utc.lo23.sharutc.controler.command.profile.AddContactToCategoryCommandImpl;
import fr.utc.lo23.sharutc.controler.command.profile.AddUserCommand;
import fr.utc.lo23.sharutc.controler.command.profile.AddUserCommandImpl;
import fr.utc.lo23.sharutc.controler.command.profile.CreateCategoryCommand;
import fr.utc.lo23.sharutc.controler.command.profile.CreateCategoryCommandImpl;
import fr.utc.lo23.sharutc.controler.command.profile.DeleteCategoryCommand;
import fr.utc.lo23.sharutc.controler.command.profile.DeleteCategoryCommandImpl;
import fr.utc.lo23.sharutc.controler.command.profile.DeleteContactCommand;
import fr.utc.lo23.sharutc.controler.command.profile.DeleteContactCommandImpl;
import fr.utc.lo23.sharutc.controler.command.profile.RemoveContactFromCategoryCommand;
import fr.utc.lo23.sharutc.controler.command.profile.RemoveContactFromCategoryCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.DownloadMusicsCommand;
import fr.utc.lo23.sharutc.controler.command.search.DownloadMusicsCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.InstallRemoteMusicsCommand;
import fr.utc.lo23.sharutc.controler.command.search.InstallRemoteMusicsCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.IntegrateMusicSearchCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.IntegrateMusicSearchCommand;
import fr.utc.lo23.sharutc.controler.command.search.MusicSearchCommand;
import fr.utc.lo23.sharutc.controler.command.search.MusicSearchCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.PerformMusicSearchCommand;
import fr.utc.lo23.sharutc.controler.command.search.PerformMusicSearchCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.SendMusicsCommand;
import fr.utc.lo23.sharutc.controler.command.search.SendMusicsCommandImpl;

/**
 *
 */
public class CommandModule extends AbstractModule {

    /**
     *
     */
    @Override
    protected void configure() {
        //account
        bind(AccountCreationCommand.class).to(AccountCreationCommandImpl.class);
        bind(ConnectionRequestCommand.class).to(ConnectionRequestCommandImpl.class);
        bind(DisconnectionCommand.class).to(DisconnectionCommandImpl.class);
        bind(ExportProfileCommand.class).to(ExportProfileCommandImpl.class);
        bind(ImportProfileCommand.class).to(ImportProfileCommandImpl.class);
        // more...

        //music
        bind(AddCommentCommand.class).to(AddCommentCommandImpl.class);
        bind(EditCommentCommand.class).to(EditCommentCommandImpl.class);
        bind(RemoveCommentCommand.class).to(RemoveCommentCommandImpl.class);

        bind(AddTagCommand.class).to(AddTagCommandImpl.class);
        bind(RemoveTagCommand.class).to(RemoveTagCommandImpl.class);

        bind(ShowTagMapCommand.class).to(ShowTagMapCommandImpl.class);
        bind(SendTagMapCommand.class).to(SendTagMapCommandImpl.class);
        bind(IntegrateRemoteTagMapCommand.class).to(IntegrateRemoteTagMapCommandImpl.class);

        bind(AddToLocalCatalogCommand.class).to(AddToLocalCatalogCommandImpl.class);
        bind(RemoveFromLocalCatalogCommand.class).to(RemoveFromLocalCatalogCommandImpl.class);

        bind(FetchRemoteCatalogCommand.class).to(FetchRemoteCatalogCommandImpl.class);
        bind(IntegrateRemoteCatalogCommand.class).to(IntegrateRemoteCatalogCommandImpl.class);
        bind(SendCatalogCommand.class).to(SendCatalogCommandImpl.class);

        bind(SetScoreCommand.class).to(SetScoreCommandImpl.class);
        bind(UnsetScoreCommand.class).to(UnsetScoreCommandImpl.class);
        // more...

        //player
        bind(PlayMusicCommand.class).to(PlayMusicCommandImpl.class);
        bind(SendMusicToPlayCommand.class).to(SendMusicToPlayCommandImpl.class);
        bind(PlayIncomingMusicCommand.class).to(PlayIncomingMusicCommandImpl.class);
        // more...

        //profile
        bind(AddContactCommand.class).to(AddContactCommandImpl.class);
        bind(AddContactToCategoryCommand.class).to(AddContactToCategoryCommandImpl.class);
        bind(AddUserCommand.class).to(AddUserCommandImpl.class);
        bind(CreateCategoryCommand.class).to(CreateCategoryCommandImpl.class);
        bind(DeleteCategoryCommand.class).to(DeleteCategoryCommandImpl.class);
        bind(DeleteContactCommand.class).to(DeleteContactCommandImpl.class);
        bind(RemoveContactFromCategoryCommand.class).to(RemoveContactFromCategoryCommandImpl.class);
        // more...

        //search
        bind(MusicSearchCommand.class).to(MusicSearchCommandImpl.class);
        bind(PerformMusicSearchCommand.class).to(PerformMusicSearchCommandImpl.class);
        bind(IntegrateMusicSearchCommand.class).to(IntegrateMusicSearchCommandImpl.class);

        bind(DownloadMusicsCommand.class).to(DownloadMusicsCommandImpl.class);
        bind(SendMusicsCommand.class).to(SendMusicsCommandImpl.class);
        bind(InstallRemoteMusicsCommand.class).to(InstallRemoteMusicsCommandImpl.class);

        //network ??
        bind(IntegrateBroadcastConnectionCommand.class).to(IntegrateBroadcastConnectionCommandImpl.class);
        bind(IntegrateConnectionCommand.class).to(IntegrateConnectionCommandImpl.class);
        bind(IntegrateDisconnectionCommand.class).to(IntegrateDisconnectionCommandImpl.class);

    }
}

package fr.utc.lo23.sharutc.injection;

import com.google.inject.AbstractModule;
import fr.utc.lo23.sharutc.controler.command.account.*;
import fr.utc.lo23.sharutc.controler.command.music.*;
import fr.utc.lo23.sharutc.controler.command.player.*;
import fr.utc.lo23.sharutc.controler.command.profile.*;
import fr.utc.lo23.sharutc.controler.command.search.*;

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
        bind(EditUserInfoCommand.class).to(EditUserInfoCommandImpl.class);
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

        bind(AddMusicToCategoryCommand.class).to(AddMusicToCategoryCommandImpl.class);
        // more...

        //player
        bind(PlayMusicCommand.class).to(PlayMusicCommandImpl.class);
        bind(SendMusicToPlayCommand.class).to(SendMusicToPlayCommandImpl.class);
        bind(PlayIncomingMusicCommand.class).to(PlayIncomingMusicCommandImpl.class);
        bind(AddToPlaylistCommand.class).to(AddToPlaylistCommandImpl.class);
        bind(RemoveFromPlaylistCommand.class).to(RemoveFromPlaylistCommandImpl.class);

        //profile
        bind(AddContactCommand.class).to(AddContactCommandImpl.class);
        bind(AddContactToCategoryCommand.class).to(AddContactToCategoryCommandImpl.class);
        bind(CreateCategoryCommand.class).to(CreateCategoryCommandImpl.class);
        bind(DeleteCategoryCommand.class).to(DeleteCategoryCommandImpl.class);
        bind(DeleteContactCommand.class).to(DeleteContactCommandImpl.class);
        bind(EditCategoryNameCommand.class).to(EditCategoryNameCommandImpl.class);
        bind(RemoveContactFromCategoryCommand.class).to(RemoveContactFromCategoryCommandImpl.class);
        bind(ManageRightsCommand.class).to(ManageRightsCommandImpl.class);
        // more...

        //search
        bind(MusicSearchCommand.class).to(MusicSearchCommandImpl.class);
        bind(PerformMusicSearchCommand.class).to(PerformMusicSearchCommandImpl.class);
        bind(IntegrateMusicSearchCommand.class).to(IntegrateMusicSearchCommandImpl.class);

        bind(DownloadMusicsCommand.class).to(DownloadMusicsCommandImpl.class);
        bind(SendMusicsCommand.class).to(SendMusicsCommandImpl.class);
        bind(InstallRemoteMusicsCommand.class).to(InstallRemoteMusicsCommandImpl.class);

        //network ??
        bind(IntegrateUserInfoAndReplyCommand.class).to(IntegrateUserInfoAndReplyCommandImpl.class);
        bind(IntegrateUserInfoCommand.class).to(IntegrateUserInfoCommandImpl.class);
        bind(IntegrateDisconnectionCommand.class).to(IntegrateDisconnectionCommandImpl.class);

    }
}

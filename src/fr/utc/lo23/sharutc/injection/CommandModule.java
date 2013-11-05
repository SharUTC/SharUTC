package fr.utc.lo23.sharutc.injection;

import com.google.inject.AbstractModule;
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
import fr.utc.lo23.sharutc.controler.command.search.InstallRemoteMusicsCommand;
import fr.utc.lo23.sharutc.controler.command.search.InstallRemoteMusicsCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.IntegrateMusicSearchCommandImpl;
import fr.utc.lo23.sharutc.controler.command.search.IntegrateMusicSearchCommand;
import fr.utc.lo23.sharutc.controler.command.search.MusicSearchCommand;
import fr.utc.lo23.sharutc.controler.command.search.MusicSearchCommandImpl;
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
        bind(AddCommentCommand.class).to(AddCommentCommandImpl.class);
        bind(AddTagCommand.class).to(AddTagCommandImpl.class);
        bind(AddToLocalCatalogCommand.class).to(AddToLocalCatalogCommandImpl.class);
        bind(EditCommentCommand.class).to(EditCommentCommandImpl.class);
        bind(FetchRemoteCatalogCommand.class).to(FetchRemoteCatalogCommandImpl.class);
        bind(IntegrateRemoteCatalogCommand.class).to(IntegrateRemoteCatalogCommandImpl.class);
        bind(IntegrateRemoteTagMapCommand.class).to(IntegrateRemoteTagMapCommandImpl.class);
        bind(RemoveCommentCommand.class).to(RemoveCommentCommandImpl.class);
        bind(RemoveFromLocalCatalogCommand.class).to(RemoveFromLocalCatalogCommandImpl.class);
        bind(RemoveTagCommand.class).to(RemoveTagCommandImpl.class);
        bind(SendCatalogCommand.class).to(SendCatalogCommandImpl.class);
        bind(SendTagMapCommand.class).to(SendTagMapCommandImpl.class);
        bind(SetScoreCommand.class).to(SetScoreCommandImpl.class);
        bind(ShowTagMapCommand.class).to(ShowTagMapCommandImpl.class);
        bind(UnsetScoreCommand.class).to(UnsetScoreCommandImpl.class);
        bind(IntegrateMusicSearchCommand.class).to(IntegrateMusicSearchCommandImpl.class);
        bind(InstallRemoteMusicsCommand.class).to(InstallRemoteMusicsCommandImpl.class);
        bind(SendMusicsCommand.class).to(SendMusicsCommandImpl.class);
        bind(MusicSearchCommand.class).to(MusicSearchCommandImpl.class);
        bind(IntegrateConnectionCommand.class).to(IntegrateConnectionCommandImpl.class);
        // more...
    }
}

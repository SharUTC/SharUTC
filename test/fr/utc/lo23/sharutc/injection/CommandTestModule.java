package fr.utc.lo23.sharutc.injection;

import com.google.inject.AbstractModule;
import fr.utc.lo23.sharutc.controler.command.music.AddCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.AddCommentCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.AddTagCommand;
import fr.utc.lo23.sharutc.controler.command.music.AddTagCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.AddToLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.AddToLocalCatalogCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.EditCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.EditCommentCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.FetchRemoteCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.FetchRemoteCatalogCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteCatalogCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteTagMapCommand;
import fr.utc.lo23.sharutc.controler.command.music.IntegrateRemoteTagMapCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.RemoveCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveCommentCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.RemoveFromLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveFromLocalCatalogCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.RemoveTagCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveTagCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.SendCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.SendCatalogCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.SendTagMapCommand;
import fr.utc.lo23.sharutc.controler.command.music.SendTagMapCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.SetScoreCommand;
import fr.utc.lo23.sharutc.controler.command.music.SetScoreCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.ShowTagMapCommand;
import fr.utc.lo23.sharutc.controler.command.music.ShowTagMapCommandMock;
import fr.utc.lo23.sharutc.controler.command.music.UnsetScoreCommand;
import fr.utc.lo23.sharutc.controler.command.music.UnsetScoreCommandMock;

/**
 *
 */
public class CommandTestModule extends AbstractModule {

    /**
     *
     */
    @Override
    protected void configure() {
        bind(AddCommentCommand.class).to(AddCommentCommandMock.class);
        bind(AddTagCommand.class).to(AddTagCommandMock.class);
        bind(AddToLocalCatalogCommand.class).to(AddToLocalCatalogCommandMock.class);
        bind(EditCommentCommand.class).to(EditCommentCommandMock.class);
        bind(FetchRemoteCatalogCommand.class).to(FetchRemoteCatalogCommandMock.class);
        bind(IntegrateRemoteCatalogCommand.class).to(IntegrateRemoteCatalogCommandMock.class);
        bind(IntegrateRemoteTagMapCommand.class).to(IntegrateRemoteTagMapCommandMock.class);
        bind(RemoveCommentCommand.class).to(RemoveCommentCommandMock.class);
        bind(RemoveFromLocalCatalogCommand.class).to(RemoveFromLocalCatalogCommandMock.class);
        bind(RemoveTagCommand.class).to(RemoveTagCommandMock.class);
        bind(SendCatalogCommand.class).to(SendCatalogCommandMock.class);
        bind(SendTagMapCommand.class).to(SendTagMapCommandMock.class);
        bind(SetScoreCommand.class).to(SetScoreCommandMock.class);
        bind(ShowTagMapCommand.class).to(ShowTagMapCommandMock.class);
        bind(UnsetScoreCommand.class).to(UnsetScoreCommandMock.class);
        // more...
    }
}
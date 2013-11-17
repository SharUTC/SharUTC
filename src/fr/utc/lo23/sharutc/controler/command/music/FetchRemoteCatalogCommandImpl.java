package fr.utc.lo23.sharutc.controler.command.music;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
public class FetchRemoteCatalogCommandImpl implements FetchRemoteCatalogCommand {

    private static final Logger log = LoggerFactory
            .getLogger(FetchRemoteCatalogCommandImpl.class);
    private final NetworkService networkService;
    private final AppModel appModel;
    private Peer mPeer;

    /**
     * {@inheritDoc}
     */
    @Inject
    public FetchRemoteCatalogCommandImpl(NetworkService networkService, AppModel appModel) {
        this.networkService = networkService;
        this.appModel = appModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Peer getPeer() {
        return mPeer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPeer(Peer peer) {
        this.mPeer = peer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.info("FetchRemoteCatalogCommand ...");

        //removing old values, updates ui
        appModel.getRemoteUserCatalog().clear();

        // send request
        networkService.sendUnicastGetCatalog(mPeer);
        log.info("FetchRemoteCatalogCommand DONE");
    }
}

package fr.utc.lo23.sharutc.controler.network;

import fr.utc.lo23.sharutc.model.AppModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;
/**
 * This class is a server socket. It waits ans listens to new connexions.
 * It also accepts the new connexions.
 * For each new connexion, it creates a new peer.
 *
 * @author Arselle
 */
public class ListenThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(PeerDiscoverySocket.class);

    private final AppModel appModel;
    private final NetworkService networkService;
    private final MessageHandler messageHandler;
    private final MessageParser messageParser;

    private Thread mThread;
    private final int mPort;
    private boolean mThreadShouldStop = false;

    /**
     *
     * @param p the binding port of the socket
     * @param ns the network service
     * @param appModel the model of the global application
     */
    public ListenThread(int port, AppModel appModel, MessageHandler
            messageHandler, MessageParser messageParser, NetworkService
            networkService) {
        this.mPort = port;
        this.appModel = appModel;
        this.messageHandler = messageHandler;
        this.messageParser = messageParser;
        this.networkService = networkService;
    }

    /**
     * Starts the listenThread
     */
    public void start() {
        mThread = new Thread(this);
        mThread.start();
    }

    /**
     * Stops the listenThread
     */
    public void stop() {
        mThreadShouldStop = true;
    }

    /**
     * Traitement executed by the listenThread
     */
    @Override
    public void run() {
        long peerID = appModel.getProfile().getUserInfo().getPeerId();
        try {
            ServerSocket socketServer = new ServerSocket(mPort);
            System.out.println("Lancement du serveur");

            while (socketServer.isBound()) {
                Socket socketClient = socketServer.accept();
                PeerSocket ps = new PeerSocket(socketClient, peerID, messageHandler, messageParser, networkService);
                ps.start();
            }
        } catch (IOException e) {
            log.error(e.toString());
        }
    }
}

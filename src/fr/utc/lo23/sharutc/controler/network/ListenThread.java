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
    private static final Logger slog = LoggerFactory.getLogger(PeerDiscoverySocket.class);
    private final int mPort;
    private final NetworkService mNetworkService;
    private Thread thread;
    private boolean threadShouldStop = false;
    private final AppModel mAppModel;
    
    /**
     *
     * @param p the binding port of the socket
     * @param ns the network service
     * @param appModel the model of the global application
     */
    public ListenThread(int p, NetworkService ns, AppModel appModel) {
        this.mPort = p;
        mNetworkService = ns;
        mAppModel = appModel;
    }

    /**
     * Starts the listenThread
     */
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Stops the listenThread
     */
    public void stop() {
        threadShouldStop = true;
    }

    /**
     * Traitement executed by the listenThread
     */
    @Override
    public void run() {
        long peerID = mAppModel.getProfile().getUserInfo().getPeerId();
        try {
            ServerSocket socketServeur = new ServerSocket(mPort);
            System.out.println("Lancement du serveur");
            
            while (socketServeur.isBound()) {
                Socket socketClient = socketServeur.accept();
                PeerSocket ps = new PeerSocket(socketClient,mNetworkService,peerID);
                ps.start();
            }
            
        } catch (Exception e) {
          e.printStackTrace();
        }
    }
  }

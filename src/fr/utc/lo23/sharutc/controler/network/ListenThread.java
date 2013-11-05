package fr.utc.lo23.sharutc.controler.network;

import fr.utc.lo23.sharutc.model.AppModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;
/**
 *
 */
public class ListenThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(PeerDiscoverySocket.class);
    private final int port;//final static int port;
    final NetworkService networkService;
    private Thread thread;
    private boolean threadShouldStop = false;
    private final AppModel mAppModel;
    
    /**
     *
     */
    public ListenThread(int p, NetworkService ns, AppModel appModel) {
        this.port = p;
        networkService = ns;
        mAppModel = appModel;
    }

    /**
     *
     */
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     *
     */
    public void stop() {
        threadShouldStop = true;
    }

    /**
     *
     */
    @Override
    public void run() {
        long peerID = mAppModel.getProfile().getUserInfo().getPeerId();
        try {
            ServerSocket socketServeur = new ServerSocket(port);
            System.out.println("Lancement du serveur");
            
            while (socketServeur.isBound()) {
                Socket socketClient = socketServeur.accept();
                PeerSocket ps = new PeerSocket(socketClient,networkService,peerID);
                ps.start();
            }
            
        } catch (Exception e) {
          e.printStackTrace();
        }
    }
  }

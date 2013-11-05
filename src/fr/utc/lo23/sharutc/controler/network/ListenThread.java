package fr.utc.lo23.sharutc.controler.network;

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
    private final NetworkService networkService;
    
    /**
     *
     */
    public ListenThread(int p, NetworkService ns) {
        this.port = p;
        this.networkService = ns;
    }

    /**
     *
     */
    public void start() {
        this.start();
    }

    /**
     *
     */
    public void stop() {
        this.stop();
    }

    /**
     *
     */
    @Override
    public void run() {
        long peerID = 20;   //valeur arbitraire pour l'instant A CHANGER
        try {
            ServerSocket socketServeur = new ServerSocket(port);
            System.out.println("Lancement du serveur");
            
            while (socketServeur.isBound()) {
                Socket socketClient = socketServeur.accept();
                PeerSocket ps = new PeerSocket(socketClient,this.networkService,peerID);
                networkService.addPeer(peerID, ps);
                socketClient.close();
            }
            
        } catch (Exception e) {
          e.printStackTrace();
        }
    }
  }

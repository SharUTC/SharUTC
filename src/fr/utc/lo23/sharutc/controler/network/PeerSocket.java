package fr.utc.lo23.sharutc.controler.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeerSocket implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(PeerDiscoverySocket.class);
    private final MessageParser messageParser;
    private Thread thread;
    private boolean threadShouldStop = false;
    private final Socket mSocket;
    private BufferedReader mBr;
    private PrintWriter mPw;
    private final NetworkService mNs;
    private final MessageHandler messageHandler;
    private ObjectInputStream mObjectInputStream;
    private ObjectOutputStream mObjectOutputStream;

    /**
     * Construct a PeerSocket
     *
     * @param sock
     * @param ns
     * @param peerId
     * @param messageParser
     * @param messageHandler
     */
    public PeerSocket(Socket sock, NetworkService ns, Long peerId, MessageParser messageParser, MessageHandler messageHandler) {
        log.info("* new TCP connection from " + sock.getInetAddress() + " *");
        this.mSocket = sock;
        this.mNs = ns;
        this.messageParser = messageParser;
        this.messageHandler = messageHandler;
        // add this new PeerSocket to the PeerSocket list
        addMe(peerId);
    }

    /**
     * Start the thread
     */
    public void start() {
        // start thread
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        } else {
            log.warn("Can't start PeerSocket: already running.");
        }
    }

    /**
     * Stop the thread
     */
    public void stop() {
        threadShouldStop = true;
    }

    /**
     * Add this new PeerSocket to the PeerSocket list
     *
     * @param peerId
     */
    public void addMe(Long peerId) {
        this.mNs.addPeer(peerId, this);
    }

    /**
     * Send a general information message - string
     *
     * @param msg
     */
    public void send(String msg) {
        mPw.print(msg);
        mPw.flush();
    }

    /**
     * Send a general information message - message
     *
     * @param msg
     */
    public void send(Message msg) {
        try {
                mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
            } catch (IOException e) {
                log.error(e.toString());
            }
        String json = messageParser.toJSON(msg);
        try {
                mObjectOutputStream.writeObject(json);
            } catch (IOException e) {
                log.error(e.toString());
            }
    }

    /**
     * Peer listening and writing received messages
     */
    @Override
    public void run() {
        // TODO en gros là le run log juste les messages qu'il reçoit,
        // il faudrait qu'il lise les objets messages, instancie la bonne Commande pour le traiter,
        // set tous les paramètres de cette commandes avec les valeurs contenues dans le message et lance a commande dans un nouveau thread
        while (!threadShouldStop) {
            try {
                mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());
            } catch (IOException e) {
                log.error(e.toString());
            }
            String msg = null;
            try {
                msg = (String) mObjectInputStream.readObject();
            } catch (IOException ex) {
                log.error(ex.toString());
            } catch(ClassNotFoundException e) {
                log.error(e.toString());
            }
            messageHandler.handleMessage(msg);
        }
        // close everything
        try {
            mSocket.close();
        } catch (IOException ex) {
            log.error(ex.toString());
        }
    }
}

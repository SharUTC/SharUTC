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

    private final MessageHandler messageHandler;
    private final MessageParser messageParser;
    private final NetworkService networkService;

    private final Socket mSocket;
    private BufferedReader mBr;
    private PrintWriter mPw;
    private ObjectInputStream mObjectInputStream;
    private ObjectOutputStream mObjectOutputStream;
    private Thread mThread;
    private boolean mThreadShouldStop = false;

    /**
     * Construct a PeerSocket.
     *
     * @param socket a Socket connected to the peer
     * @param peerId the peerId of the new peer
     * @param messageHandler the injected message handler
     * @param messageParser the injected message parser
     * @param networkService the instance of NetworkService
     */
    public PeerSocket(Socket socket, Long peerId, MessageHandler messageHandler,
            MessageParser messageParser, NetworkService networkService) {
        log.info("* new TCP connection from " + socket.getInetAddress() + " *");
        this.mSocket = socket;
        this.messageHandler = messageHandler;
        this.messageParser = messageParser;
        this.networkService = networkService;

        // add this new PeerSocket to the PeerSocket list
        addMe(peerId);
    }

    /**
     * Start the thread.
     */
    public void start() {
        // start thread
        if (mThread == null) {
            mThread = new Thread(this);
            mThread.start();
        } else {
            log.warn("Can't start PeerSocket: already running.");
        }
    }

    /**
     * Stop the thread.
     */
    public void stop() {
        mThreadShouldStop = true;
    }

    /**
     * Add this new PeerSocket to the PeerSocket list
     *
     * @param peerId
     */
    public void addMe(Long peerId) {
        networkService.addPeer(peerId, this);
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
     * Send a message to the peer.
     *
     * @param msg a Message to send
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
     * Listen to incoming message from the peer and handles them.
     * <p>
     * The message receive are given to messageHandler to instanciate the right
     * command to treat them.
     *
     * @see MessageHandler
     */
    @Override
    public void run() {
        // TODO en gros là le run log juste les messages qu'il reçoit,
        // il faudrait qu'il lise les objets messages, instancie la bonne Commande pour le traiter,
        // set tous les paramètres de cette commandes avec les valeurs contenues dans le message et lance a commande dans un nouveau thread
        while (!mThreadShouldStop) {
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

package fr.utc.lo23.sharutc.controler.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeerSocket implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(PeerDiscoverySocket.class);

    private final MessageHandler messageHandler;
    private final MessageParser messageParser;
    private final NetworkServiceImpl networkService;

    private boolean mFailureHandled = false;
    private final Socket mSocket;
    private Thread mThread;
    private volatile boolean mThreadShouldStop = false;
    private ObjectInputStream mIn;
    private ObjectOutputStream mOut;

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
        this.networkService = (NetworkServiceImpl) networkService;

        try {
            mOut = new ObjectOutputStream(mSocket.getOutputStream());
            mIn = new ObjectInputStream(mSocket.getInputStream());
        } catch (IOException e) {
            log.error(e.toString());
        }

        // add this new PeerSocket to the PeerSocket list
        this.networkService.addPeer(peerId, this);
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
    public synchronized void stop() {
        mThreadShouldStop = true;
        try {
            mIn.close();
            mOut.close();
        } catch (IOException ex) {
            log.error(ex.toString());
        }
    }

    /**
     * Send a message to the peer.
     *
     * @param msg a Message to send
     */
    public synchronized void send(Message msg) {
        String json = messageParser.toJSON(msg);
        try {
            mOut.writeObject(json);
        } catch (IOException ex) {
            handleSocketFailure(ex);
        }
    }

    /**
     * Any clean-up action following a socket failure.
     */
    private synchronized void handleSocketFailure(IOException ex) {
        if (!mThreadShouldStop && !mFailureHandled) {
            log.info("Peer socket failure: " + ex.getMessage());
            networkService.disconnectPeer(this);
        }
        mFailureHandled = true;
    }

    /**
     * Listen to incoming message from the peer and handles them.
     * <p>
     * The messages received are given to messageHandler to instantiate the
     * right command to treat them.
     *
     * @see MessageHandler
     */
    @Override
    public void run() {
        while (!mThreadShouldStop) {
            String msg = null;
            try {
                msg = (String) mIn.readObject();
            } catch (ClassNotFoundException ex) {
                log.error(ex.toString());
            } catch (IOException ex) {
                handleSocketFailure(ex);
                break;
            }
            messageHandler.handleMessage(msg);
        }
        if (mSocket.isConnected()) {
            try {
                mSocket.close();
            } catch (IOException ex) {
                log.error(ex.toString());
            }
        }
        networkService.removePeer(this);
    }
}

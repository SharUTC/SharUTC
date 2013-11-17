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
    private final NetworkService networkService;

    private final Socket mSocket;
    private Thread mThread;
    private boolean mThreadShouldStop = false;
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
        this.networkService = networkService;

        try {
            mOut = new ObjectOutputStream(mSocket.getOutputStream());
            mIn = new ObjectInputStream(mSocket.getInputStream());
        } catch (IOException e) {
            log.error(e.toString());
        }

        // add this new PeerSocket to the PeerSocket list
        networkService.addPeer(peerId, this);
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
     * Send a message to the peer.
     *
     * @param msg a Message to send
     */
    public void send(Message msg) {
        String json = messageParser.toJSON(msg);
        try {
            mOut.writeObject(json);
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
        while (!mThreadShouldStop) {
            String msg = null;
            try {
                msg = (String) mIn.readObject();
            } catch (Exception ex) {
                log.error(ex.toString());
            }
            messageHandler.handleMessage(msg);
        }
        try {
            mSocket.close();
        } catch (IOException ex) {
            log.error(ex.toString());
        }
    }
}

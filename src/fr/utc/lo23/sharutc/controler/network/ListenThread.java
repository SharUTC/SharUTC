package fr.utc.lo23.sharutc.controler.network;

import fr.utc.lo23.sharutc.model.AppModel;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listen to new TCP connection and instantiate new PeerSocket to handle them.
 * <p>
 * This class bind a ServerSocket to the given port and listen on it.
 * Each new TCP connection is handled separately in a new instance of PeerSocket
 * running in a separate thread.
 *
 * @see PeerSocket
 */
public class ListenThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(PeerDiscoverySocket.class);

    private final AppModel appModel;
    private final NetworkService networkService;
    private final MessageHandler messageHandler;
    private final MessageParser messageParser;

    private final int mPort;
    private ServerSocket mServerSocket;
    private Thread mThread;
    private volatile boolean mThreadShouldStop = false;

    /**
     * Set the different service provider we'll need and the port to listen.
     *
     * @param port the port to bind the listening socket to
     * @param appModel the model of the application
     * @param messageHandler the injected MessageHandler instance
     * @param messageParser the injected MessageParser instance
     * @param networkService the injected NetworkService instance
     */
    public ListenThread(int port, AppModel appModel, MessageHandler
            messageHandler, MessageParser messageParser, NetworkService
            networkService) {
        this.mPort = port;
        this.appModel = appModel;
        this.messageHandler = messageHandler;
        this.messageParser = messageParser;
        this.networkService = networkService;

        this.mThreadShouldStop = false;
        this.mThread = null;
    }

    /**
     * Starts the listenThread.
     */
    public void start() {
        if (mThread == null) {
            mThread = new Thread(this);
            mThread.start();
        } else {
            log.warn("Can't start ListenThread: already running.");
        }
    }

    /**
     * Stops the listenThread.
     */
    public synchronized void stop() {
        mThreadShouldStop = true;
        try {
            mServerSocket.close();
        } catch (IOException ex) {
            log.error(ex.toString());
        }
    }

    private long handleNewConnection(Socket socket) {
        Long peerId = null;
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            String json = null;
            try {
                json = (String) in.readObject();
            } catch (ClassNotFoundException ex) {
                log.error(ex.toString());
            }
            Message msg= messageParser.fromJSON(json);
            if (msg.getType() == MessageType.CONNECTION_RESPONSE) {
                if (msg.getFromPeerId() == null) {
                    log.error("Received connection response with peerId = null !");
                }
                peerId = msg.getFromPeerId();
                messageHandler.handleMessage(json);
            } else {
                log.error("First message on Socket must be CONNECTION_RESPONSE");
            }
        } catch (IOException ex) {
            log.error(ex.toString());
        }
        return peerId;
    }

    /**
     * Main listening loop.
     * <p>
     * Loop on the ServerSocket accept() method and instantiate a new PeerSocket
     * for each new connection.
     * The newly created PeerSocket is then started in a new thread with
     * start().
     *
     * @see PeerSocket
     */
    @Override
    public void run() {
        try {
            mServerSocket = new ServerSocket(mPort);
            log.info("Started listening on port " + mPort);

            while (mServerSocket.isBound()) {
                Socket clientSocket = mServerSocket.accept();
                long peerId = handleNewConnection(clientSocket);
                PeerSocket ps = new PeerSocket(clientSocket, peerId, messageHandler, messageParser, networkService);
                ps.start();
            }
            if (!mServerSocket.isClosed()) {
                mServerSocket.close();
            }
        } catch (IOException ex) {
            if (!mThreadShouldStop) {
                log.error(ex.toString());
            }
        }
    }
}

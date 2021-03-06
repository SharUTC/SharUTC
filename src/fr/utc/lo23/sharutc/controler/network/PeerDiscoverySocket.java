package fr.utc.lo23.sharutc.controler.network;

import fr.utc.lo23.sharutc.model.AppModel;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listen to incoming UDP multicast and provide methods to send some.
 */
public class PeerDiscoverySocket implements Runnable {

    private static final Logger log = LoggerFactory
            .getLogger(PeerDiscoverySocket.class);
    private final AppModel appModel;
    private final MessageHandler messageHandler;
    private final MessageParser messageParser;
    private final NetworkService networkService;
    private final InetAddress mGroup;
    private final int mPort;
    private MulticastSocket mSocket;
    private Thread mThread;
    private volatile boolean mThreadShouldStop = false;

    /**
     * Construct a PeerDiscoverySocket.
     *
     * @param port the port to use for UDP multicast
     * @param group the Class D address of the multicast group to join
     * @param appModel the model of the application
     * @param messageHandler the injected message handler
     * @param messageParser the injected message parser
     * @param networkService the instance of the networkService
     */
    public PeerDiscoverySocket(int port, InetAddress group, AppModel appModel,
            MessageHandler messageHandler, MessageParser messageParser,
            NetworkService networkService) {
        this.mPort = port;
        this.mGroup = group;
        this.appModel = appModel;
        this.messageHandler = messageHandler;
        this.messageParser = messageParser;
        this.networkService = networkService;

        this.mThreadShouldStop = false;
        this.mThread = null;
        try {
            mSocket = new MulticastSocket(mPort);
            mSocket.joinGroup(mGroup);
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    /**
     * Start the listening thread.
     */
    public void start() {
        if (mThread == null) {
            mThread = new Thread(this, "PeerDiscoverySocket");
            mThread.start();
        } else {
            log.warn("Can't start PeerDiscoverySocket: already running.");
        }
    }

    /**
     * Stop the listening thread.
     */
    public synchronized void stop() {
        mThreadShouldStop = true;
        mSocket.close();
    }

    /**
     * Send a message to the multicast group.
     *
     * @param msg a Message to send
     */
    public synchronized void send(Message msg) {
        byte[] bytes = messageParser.toJSON(msg).getBytes();
        DatagramPacket p = new DatagramPacket(bytes, bytes.length, mGroup, mPort);
        try {
            mSocket.send(p);
        } catch (IOException e) {
            if (!mThreadShouldStop) {
                log.error(e.toString());
            }
        }
    }

    /**
     * Create a new PeerSocket and start it.
     *
     * @see PeerSocket
     * @param p the Hello UDP packet
     * @param msg the Hello Message
     */
    private void addPeer(DatagramPacket p, Message msg) {
        try {
            Socket socket = new Socket(p.getAddress(), p.getPort());
            Long peerId = msg.getFromPeerId();
            PeerSocket peerSocket = new PeerSocket(socket, peerId, messageHandler, messageParser, networkService);
            peerSocket.start();
        } catch (IOException ex) {
            log.error(ex.toString());
        }
    }

    /**
     * Listen to multicast packet and create new PeerSocket when receiving Hello
     * message.
     * <p>
     * When receiving a Hello message, this creates a new PeerSocket, start it
     * and use it to send a reply.
     */
    @Override
    public void run() {
        while (!mThreadShouldStop) {
            final int SIZE = 1000;
            byte[] buf = new byte[SIZE];
            DatagramPacket p = new DatagramPacket(buf, buf.length);
            try {
                mSocket.receive(p);
                // print information about the sender
                log.info("<broadcast from " + p.getAddress().toString() + " : " + p.getPort() + " >");
                log.debug("Got packet " + Arrays.toString(p.getData()));
                String json = new String(p.getData());
                Message msgReceived = messageParser.fromJSON(json);
                if (msgReceived.getFromPeerId().equals(appModel.getProfile().getUserInfo().getPeerId())) {
                    continue;
                }
                if (msgReceived.getType() == MessageType.CONNECTION) {
                    if (msgReceived.getFromPeerId() != null) {
                        log.info("Message received from peerId " + msgReceived.getFromPeerId() + ", message type = " + msgReceived.getType());
                    } else {
                        log.error("Received message with peerId = null !");
                    }
                    addPeer(p, msgReceived);
                    messageHandler.handleMessage(json);
                } else {
                    log.warn("Message type must be CONNECTION !");
                }
            } catch (IOException e) {
                if (!mThreadShouldStop) {
                    log.error(e.toString());
                }
            }
        }
        if (!mSocket.isClosed()) {
            mSocket.close();
        }
    }
}

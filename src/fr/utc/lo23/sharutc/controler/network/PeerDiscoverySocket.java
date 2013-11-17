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
    private boolean mThreadShouldStop = false;

    /**
     * Construct a PeerDiscoverySocket
     *
     * @param port
     * @param group
     * @param ns
     * @param appModel
     * @param messageParser
     * @param messageHandler
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
     * Start the thread
     */
    public void start() {
        if (mThread == null) {
            mThread = new Thread(this);
            mThread.start();
        } else {
            log.warn("Can't start PeerDiscoverySocket: already running.");
        }
    }

    /**
     * Stop the thread
     */
    public void stop() {
        mThreadShouldStop = true;
    }

    /**
     * Send a general information message
     *
     * @param msg
     */
    public void send(String msg) {
        DatagramPacket p = new DatagramPacket(msg.getBytes(), msg.length(), mGroup, mPort);
        try {
            mSocket.send(p);
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    /**
     * Send a connection informative message
     *
     * @param msg
     */
    public void send(Message msg) {
        byte[] bytes = messageParser.toJSON(msg).getBytes();
        DatagramPacket p = new DatagramPacket(bytes, bytes.length, mGroup, mPort);
        try {
            mSocket.send(p);
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    /**
     * Add a new peer (with its peerId) to the peerSocket existing list and start peerSocket thread
     *
     * @param p
     * @param msg
     */
    public void addPeer(DatagramPacket p, Message msg) {
        PeerSocket peerSocket = null;
        try {
            Socket socket = new Socket(p.getAddress(), p.getPort());
            // obtain sender peerId
            Long peerId = msg.getFromPeerId();
            // add new peer
            peerSocket = new PeerSocket(socket, peerId, messageHandler, messageParser, networkService);
            peerSocket.start();
        } catch (IOException ex) {
            log.error(ex.toString());
        }
    }

    /**
     * Thread which receives a UDP packet, adds a new peer, and send personal information in order to establish the connection with the new peer
     */
    @Override
    public void run() {
        while (!mThreadShouldStop) {
            final int SIZE = 1000;
            byte[] buf = new byte[SIZE];
            DatagramPacket p = new DatagramPacket(buf, buf.length);
            Message msgReceived = null;
            try {
                // receiving UDP packet
                mSocket.receive(p);
                // print information about the sender
                log.info("<broadcast from " + p.getAddress().toString() + " : " + p.getPort() + " >");
                log.info("Got packet " + Arrays.toString(p.getData()));
                // get json string
                String json = new String(p.getData());
                // get message object
                msgReceived = messageParser.fromJSON(json);
                if (msgReceived.getFromPeerId() == appModel.getProfile().getUserInfo().getPeerId()) {
                    continue;
                }
                // CONNECTION type required
                if (msgReceived.getType() == MessageType.CONNECTION) {
                    // print more info
                    if (msgReceived.getFromPeerId() != null) {
                        log.info("Message received from peerId " + msgReceived.getFromPeerId() + ", message type = " + msgReceived.getType());
                    } else {
                        log.error("Received message with peerId = null !");
                    }
                    // add a new
                    addPeer(p, msgReceived);
                    // handle message
                    messageHandler.handleMessage(json);
                } else {
                    log.warn("Message type must be CONNECTION !");
                }
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
        // close everything
        mSocket.close();
    }
}

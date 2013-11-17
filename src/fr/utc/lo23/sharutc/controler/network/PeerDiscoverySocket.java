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

    /**
     *
     */
    private final AppModel appModel;
    /**
     *
     */
    private final MessageParser messageParser;
    /**
     *
     */
    private final MessageHandler messageHandler;
    /**
     *
     */
    private static final Logger log = LoggerFactory.getLogger(PeerDiscoverySocket.class);
    /**
     *
     */
    private Thread thread;
    /**
     *
     */
    private boolean threadShouldStop = false;
    /**
     *
     */
    private int mPort;
    /**
     *
     */
    private MulticastSocket mSocket;
    /**
     *
     */
    private InetAddress mGroup;
    /**
     *
     */
    private final NetworkService mNs;

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
    public PeerDiscoverySocket(int port, InetAddress group, NetworkService ns, AppModel appModel, MessageParser messageParser, MessageHandler messageHandler) {
        // preparation
        this.mPort = port;
        this.mGroup = group;
        this.mNs = ns;
        this.threadShouldStop = false;
        this.thread = null;
        this.appModel = appModel;
        try {
            mSocket = new MulticastSocket(mPort);
            mSocket.joinGroup(mGroup);
        } catch (IOException e) {
            log.error(e.toString());
        }
        this.messageParser = messageParser;
        this.messageHandler = messageHandler;
    }

    /**
     * Start the thread
     */
    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        } else {
            log.warn("Can't start PeerDiscoverySocket: already running.");
        }
    }

    /**
     * Stop the thread
     */
    public void stop() {
        threadShouldStop = true;
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
            peerSocket = new PeerSocket(socket, mNs, peerId, messageParser, messageHandler);
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
        while (!threadShouldStop) {
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tudor Luchiancenco <tudorluchy@gmail.com>
 */
public class PeerDiscoverySocket implements Runnable {

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
    private NetworkService mNs;

    /**
     * 
     * @param port
     * @param group
     * @param ns 
     */
    public PeerDiscoverySocket(int port, InetAddress group, NetworkService ns) {
        // preparation
        this.mPort = port;
        this.mGroup = group;
        this.mNs = ns;
        this.threadShouldStop = false;
        try {
            mSocket = new MulticastSocket(mPort);
            mSocket.joinGroup(mGroup);
        } catch (IOException e) {
            log.error(e.toString());
        }
        // start thread
        thread = new Thread(this);
        startThread();
    }
    
    /**
     * Start the thread
     */
    public void startThread() {
        thread.start();
    }

    /**
     * Stop the thread
     */
    public void stopThread() {   
        threadShouldStop = true;
    }
    
    /**
     * Send a general information message
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
     * @param msg 
     * @throws java.io.IOException 
     */
    public void sendConnectionBroadcast(String msg) throws IOException {
        // TODO - recuperer peerId
        Message msgToSend = new Message(11, MessageType.CONNECTION, msg);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(msgToSend);
        oos.flush();
        byte[] buf= baos.toByteArray();
        DatagramPacket p = new DatagramPacket(buf, buf.length, mGroup, mPort);
        try {
            mSocket.send(p);
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    /**
     * Add a new peer (with its peerId) to the peerSocket existing list
     * @param p
     * @param msg
     * @return 
     * @throws java.io.IOException 
     */
    public PeerSocket addPeer(DatagramPacket p, Message msg) throws IOException {
        Socket socket = new Socket(p.getAddress(), p.getPort());
        // obtain sender peerId
        Long peerId = msg.getFromPeerId();
        // add new peer
        PeerSocket peerSocket = new PeerSocket(socket, mNs, peerId);
        return peerSocket;
    }

    /**
     * Send personal information in order to establish the connection with the
     * new peer
     * @param pSocket
     */
    public void sendPersonalInformationToPeer(PeerSocket pSocket) {
        // TODO - peerId
        Message msgInfo = new Message(11, MessageType.CONNECTION_RESPONSE, "I send you my personal information (peerId = "+ 11 +")");
        pSocket.send(msgInfo);
    }

    /**
     * Thread which receives a UDP packet, adds a new peer, and send personal
     * information in order to establish the connection with the new peer
     */
    @Override
    public void run() {
        while (!threadShouldStop) {
            byte[] buf = new byte[1000];
            DatagramPacket p = new DatagramPacket(buf, buf.length);
            ByteArrayInputStream baos = null;
            ObjectInputStream oos = null;
            Message msgReceived = null;
            try {
                // receiving UDP packet
                mSocket.receive(p);
                // print information about the sender
                System.out.println("<broadcast from " + p.getAddress().toString() + " : " + p.getPort() + " >");
                System.out.println("Got packet " + Arrays.toString(p.getData()));
                baos = new ByteArrayInputStream(p.getData());
                // get message object
                oos = new ObjectInputStream(baos);
                try {
                    msgReceived = (Message)oos.readObject();
                } catch (ClassNotFoundException ex) {
                    log.error(ex.toString());
                }
                // print more info
                if (msgReceived.getFromPeerId() != null) {
                    System.out.println("Message received from peerId " + msgReceived.getFromPeerId() + ", message type = " + msgReceived.getType());
                } else {
                    log.error("Received message with peerId = null !");
                }
                // add a new peer 
                PeerSocket newPeer = addPeer(p, msgReceived);
                // send personal information to the new peer
                sendPersonalInformationToPeer(newPeer);
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
    }
}

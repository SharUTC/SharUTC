package fr.utc.lo23.sharutc.controler.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tudor Luchiancenco <tudorluchy@gmail.com>
 */
public class PeerSocket implements Runnable {

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
    private Socket mSocket;
    /**
     *
     */
    private BufferedReader mBr;
    /**
     *
     */
    private PrintWriter mPw;
    /**
     *
     */
    private NetworkService mNs;

    /**
     * Construct a PeerSocket
     *
     * @param sock
     * @param ns
     * @param peerId
     */
    public PeerSocket(Socket sock, NetworkService ns, Long peerId) {
        log.info("* new TCP connection from " + sock.getInetAddress() + " *");
        this.mSocket = sock;
        this.mNs = ns;
        try {
            mPw = new PrintWriter(sock.getOutputStream());
        } catch (IOException e) {
            log.error(e.toString());
        }
        // add this new PeerSocket to the PeerSocket list 
        addMe(peerId);
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
        mPw.print(msg);
        mPw.flush();
    }

    /**
     * Peer listening and writing received messages
     */
    @Override
    public void run() {
        while (!threadShouldStop) {
            try {
                mBr = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            } catch (IOException e) {
                log.error(e.toString());
            }
            String msg = null;
            try {
                while ((msg = mBr.readLine()) != null) {
                    log.info(msg);
                }
            } catch (IOException ex) {
                log.error(ex.toString());
            }
//            log.info("* TCP disconnect from " + mSocket.getInetAddress() + " *");
//            mNs.removePeer(this);
        }
        // close everything
        try {
            mBr.close();
        } catch (IOException ex) {
            log.error(ex.toString());
        }
        mPw.close();
        try {
            mSocket.close();
        } catch (IOException ex) {
            log.error(ex.toString());
        }
    }
}

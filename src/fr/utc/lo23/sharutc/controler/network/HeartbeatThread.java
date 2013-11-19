/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 
 */
public class HeartbeatThread implements Runnable {

    private static final Logger log = LoggerFactory
            .getLogger(NetworkServiceImpl.class);
    private final NetworkService mNetworkService;
    private Thread mThread;
    private boolean mThreadShouldStop = false;
    
    public HeartbeatThread(NetworkService nws){
        this.mNetworkService = nws;
        this.mThreadShouldStop = false;
        this.mThread = null;
    }
    
    /**
     * Start the thread.
     */
    public void start() {
        if (mThread == null) {
            mThread = new Thread(this);
            mThread.start();
        } else {
            log.warn("Can't start HeartbeatThread: already running.");
        }
    }

    /**
     * Stop the thread.
     */
    public void stop() {
        mThreadShouldStop = true;
    }
    
    /**
     * Testing TCP connection with known and connected peers
     */
    @Override
    public void run() {
        while (!mThreadShouldStop) {
            // TODO tudor : il faudrait en quelque sorte récuperer le catch si jamais pb d'émission (public void send(Message msg) de PEERSOCKET)
            // il faut donc faire ici TRY .... CATCH et dans le CATCH on doit supprimer le peer défaillant
            mNetworkService.sendBroadcastHeartbeat();
            log.warn("[HeartBeatThread - run()] TODO");
            try {
                // every 5 seconds we test the TCP connections
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                log.error(ex.getMessage());
            }
        }
    }
}

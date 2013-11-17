/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.controler.network;

import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Audrey
 */
public class HeartbeatThread implements Runnable {

    private static final Logger log = LoggerFactory
            .getLogger(NetworkServiceImpl.class);
    private final NetworkService mSetworkService;
    
    public HeartbeatThread(NetworkService nws){
        this.mSetworkService = nws;
    }
    @Override
    public void run() {
        // TODO
        mSetworkService.sendBroadcastHeartbeat();
        log.warn("[HeartBeatThread - run()] TODO");
        
    }
    
}

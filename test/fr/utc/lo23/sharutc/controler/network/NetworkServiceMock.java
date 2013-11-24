package fr.utc.lo23.sharutc.controler.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
@Singleton
public class NetworkServiceMock extends NetworkServiceImpl implements NetworkService {

    private static final Logger log = LoggerFactory
            .getLogger(NetworkServiceMock.class);
    private Message sendMessage = null;
    private Peer peer = null;

    @Inject
    public NetworkServiceMock(AppModel appModel, MessageParser messageParser, MessageHandler messageHandler) {
        super(appModel, messageHandler, messageParser);
    }

    @Override
    public void start(int port, String group) throws UnknownHostException {
        log.info("Starting NetworkServiceMock");
    }

    @Override
    public void stop() {
        log.info("Stopping NetworkServiceMock");
    }

    @Override
    protected void sendBroadcast(Message message) {
        this.sendMessage = message;
    }

    @Override
    protected void sendUnicast(Message message, Peer peer) {
        this.sendMessage = message;
        this.peer = peer;
    }

    @Override
    protected void sendMulticast(Message message) {
        this.sendMessage = message;
    }

    public Message getSentMessage() {
        return sendMessage;
    }

    public void setSentMessage(Message sendMessage) {
        this.sendMessage = sendMessage;
    }

    public Peer getPeer() {
        return peer;
    }
}
package fr.utc.lo23.sharutc.controler.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
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
    private final List<Message> messages = new LinkedList<Message>();
    private final List<Peer> peers = new LinkedList<Peer>();

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
        messages.add(message);
        peers.add(null);
    }

    @Override
    protected void sendUnicast(Message message, Peer peer) {
        messages.add(message);
        peers.add(peer);
    }

    @Override
    protected void sendMulticast(Message message) {
        messages.add(message);
        peers.add(null);
    }

    public int size() {
        return messages.size();
    }

    public Message getSentMessage(int i) {
        return messages.get(i);
    }

    public Message getSentMessage() {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.get(messages.size() - 1);
    }

    public Peer getPeer(int i) {
        return peers.get(i);
    }

    public Peer getPeer() {
        if (peers.isEmpty()) {
            return null;
        }
        return peers.get(peers.size() - 1);
    }

    public void clear() {
        messages.clear();
        peers.clear();
    }
}
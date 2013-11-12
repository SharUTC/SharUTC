package fr.utc.lo23.sharutc.controler.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.util.HashMap;
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
        super(appModel, messageParser, messageHandler);
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

    public Message getSendMessage() {
        return sendMessage;
    }

    public Peer getPeer() {
        return peer;
    }
}
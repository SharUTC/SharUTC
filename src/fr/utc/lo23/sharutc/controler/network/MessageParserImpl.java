package fr.utc.lo23.sharutc.controler.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Arnaud
 */
public class MessageParserImpl implements MessageParser {

    private static final Logger log = LoggerFactory
            .getLogger(MessageParserImpl.class);
    private static ObjectMapper mapper = new ObjectMapper();
    @Inject
    private AppModel appModel;
    private Message message;
    private HashMap<String, Object> messageContent = null;
    private long fromPeerId;

    /**
     *
     * @param fromPeerId
     */
    @Override
    public void setFromPeerId(long fromPeerId) {
        this.fromPeerId = fromPeerId;
    }

    /**
     *
     * @param message
     */
    @Override
    public void read(Message message) {
        this.message = message;
        Map<String, Object> parsedContent = null;
        try {
            parsedContent = mapper.readValue(message.getContent(), Map.class);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        if (parsedContent != null) {
            messageContent = new HashMap<String, Object>();
            messageContent.putAll(parsedContent);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Peer getSource() {
        Peer destinationPeer = appModel.getActivePeerList().getByPeerId(message.getFromPeerId());
        if (destinationPeer == null) {
            log.error("Missing arg : no destination");
        }
        return destinationPeer;
    }

    /**
     *
     * @param field
     * @return
     */
    @Override
    public Object getValue(String field) {
        if (message == null || messageContent == null) {
            log.warn("You must initialize the parser with a Message first");
        }
        Object object = messageContent.get(field);
        if (object == null) {
            log.error("Field \"{}\" not found in message", field);
        }
        return object;
    }

    /**
     *
     * @param messageType
     * @param content
     * @return
     */
    @Override
    public Message write(MessageType messageType, Object[][] content) {
        if (fromPeerId == 0) {
            log.error("Missing fromPeerId");
        }
        if (messageType == null) {
            log.error("Missing messageType");
        }
        Message newMessage = null;
        try {
            newMessage = new Message(fromPeerId, messageType, content != null ? mapper.writeValueAsString(content) : "");
        } catch (JsonProcessingException ex) {
            log.error(ex.toString());
        }
        return newMessage;
    }
}

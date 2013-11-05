package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.model.userdata.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class SendTagMapCommandMock implements SendTagMapCommand {

    private static final Logger log = LoggerFactory
            .getLogger(SendTagMapCommandMock.class);

    /**
     *
     */
    @Override
    public void execute() {
        log.warn("Not supported yet.");
    }

    /**
     *
     * @return
     */
    @Override
    public Peer getPeer() {
        log.warn("Not supported yet.");
        return null;
    }

    /**
     *
     * @param peer
     */
    @Override
    public void setPeer(Peer peer) {
        log.warn("Not supported yet.");
    }

    /**
     *
     * @return
     */
    @Override
    public Long getConversationId() {
        log.warn("Not supported yet.");
        return null;
    }

    /**
     *
     * @param conversationId
     */
    @Override
    public void setConversationId(Long conversationId) {
        log.warn("Not supported yet.");
    }
}

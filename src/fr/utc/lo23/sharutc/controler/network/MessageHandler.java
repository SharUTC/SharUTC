package fr.utc.lo23.sharutc.controler.network;

/**
 * The purpose of this class is to read incoming message as Json String and then
 * to execute the command relative to this message.
 */
public interface MessageHandler {
    /**
     * Read and parse a Json String to a Message object, find and run the
     * appropriate command in a new thread.
     *
     * @param string a Json String containing a Message
     */
    public void handleMessage(String string);
}

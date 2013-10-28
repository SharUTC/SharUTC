package fr.utc.lo23.sharutc.controler.network;

/**
 * list of message types available in SharUTC
 */
public enum MessageType {

    /**
     *
     */
    MUSIC_GET_ALL,
    /**
     *
     */
    MUSIC_CATALOG,
    /**
     * => sendTagMapCommand
     */
    TAG_GET_MAP,
    /**
     * => integrateRemoteTagMapCommand
     */
    TAG_MAP,
    /**
     *
     */
    COMMENT_ADD,
    /**
     *
     */
    COMMENT_EDIT,
    /**
     *
     */
    COMMENT_REMOVE,
    /**
     *
     */
    SCORE_SET,
    /**
     *
     */
    SCORE_UNSET,
    /**
     *
     */
    MUSIC_SEARCH,
    /**
     *
     */
    MUSIC_RESULTS,
    /**
     *
     */
    MUSIC_GET,
    /**
     *
     */
    MUSIC_INSTALL,
    /**
     *
     */
    MUSIC_GET_TO_PLAY,
    /**
     *
     */
    MUSIC_SEND_TO_PLAY,
    /**
     *
     */
    HEARTBEAT,
    /**
     *
     */
    DISCONNECT
}

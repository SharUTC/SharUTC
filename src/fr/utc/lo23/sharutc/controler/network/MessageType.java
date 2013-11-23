package fr.utc.lo23.sharutc.controler.network;

/**
 * list of message types available in SharUTC.
 */
public enum MessageType {

    /**
     * Send message to get a catalog from a peer.
     */
    MUSIC_GET_CATALOG,
    /**
     * Send message with the own catalog.
     */
    MUSIC_CATALOG,
    /**
     * Send message to get tag map from a peer.
     */
    TAG_GET_MAP,
    /**
     * Send message containing own tag map.
     */
    TAG_MAP,
    /**
     * Add a comment on a music.
     */
    COMMENT_ADD,
    /**
     * Edit a music comment.
     */
    COMMENT_EDIT,
    /**
     * Remove a music comment.
     */
    COMMENT_REMOVE,
    /**
     * Set a music score.
     */
    SCORE_SET,
    /**
     * Unset a music score.
     */
    SCORE_UNSET,
    /**
     * Send a message to search a music.
     */
    MUSIC_SEARCH,
    /**
     * Send a message return a list of music corresponding to a search.
     */
    MUSIC_RESULTS,
    /**
     * Send message to get a music.
     */
    MUSIC_GET,
    /**
     * Put a music in the local library.
     */
    MUSIC_INSTALL,
    /**
     * Get a music to play.
     */
    MUSIC_GET_TO_PLAY,
    /**
     * Send a music to a peer.
     */
    MUSIC_SEND_TO_PLAY,
    /**
     * Send a disconnection message to the network.
     */
    DISCONNECT,
    /**
     * Send a connection message to the network.
     */
    CONNECTION,
    /**
     * Send a connection response to a peer just connected.
     */
    CONNECTION_RESPONSE,
    /**
     * Send updated info of a user.
     */
    USER_INFO,
    /**
     * Edit a music comments.
     */
    EDIT_COMMENT,
    /**
     * Send a heartbeat message.
     */
    HEARTBEAT
}

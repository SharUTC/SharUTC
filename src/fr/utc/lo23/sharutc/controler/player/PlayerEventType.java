package fr.utc.lo23.sharutc.controler.player;

public enum PlayerEventType {

    /**
     * When a music is started or continued after a pause
     */
    STARTED,
    /**
     * When a music is paused
     */
    PAUSED,
    /**
     * When a music reaches its end
     */
    STOPPED;

    PlayerEventType() {
    }
}

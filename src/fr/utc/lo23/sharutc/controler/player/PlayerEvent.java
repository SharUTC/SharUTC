package fr.utc.lo23.sharutc.controler.player;

/**
 * Used by the player of the application to inform the PlayerService of current
 * action in the Mp3Player
 */
public class PlayerEvent {

    public Mp3Player source;
    public PlayerEventType eventType;
    public int frameIndex;

    /**
     *
     * @param source the Mp3Player playing the current music
     * @param eventType type of reading event
     * @param frameIndex the current frame index when the event occured
     */
    public PlayerEvent(
            Mp3Player source,
            PlayerEventType eventType,
            int frameIndex) {
        this.source = source;
        this.eventType = eventType;
        this.frameIndex = frameIndex;
    }
}

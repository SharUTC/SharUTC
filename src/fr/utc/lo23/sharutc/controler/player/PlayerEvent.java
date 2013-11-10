package fr.utc.lo23.sharutc.controler.player;

public class PlayerEvent {

    public Mp3Player source;
    public PlayerEventType eventType;
    public int frameIndex;

    public PlayerEvent(
            Mp3Player source,
            PlayerEventType eventType,
            int frameIndex) {
        this.source = source;
        this.eventType = eventType;
        this.frameIndex = frameIndex;
    }
}

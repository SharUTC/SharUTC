package fr.utc.lo23.sharutc.controler.player;

public class PlayerEventType {

    public static final PlayerEventType STARTED = new PlayerEventType("Started");
    public static final PlayerEventType PAUSED = new PlayerEventType("Paused");
    public static final PlayerEventType STOPPED = new PlayerEventType("Stopped");
    public String name;

    public PlayerEventType(String name) {
        this.name = name;
    }
}

package fr.utc.lo23.sharutc.controler.command.player;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;

/**
 * Used to play remote music only when they arrive from network
 */
public interface PlayIncomingMusicCommand extends Command {

    /**
     *
     * @return
     */
    public Music getMusic();

    /**
     *
     * @param music
     */
    public void setMusic(Music music);
}

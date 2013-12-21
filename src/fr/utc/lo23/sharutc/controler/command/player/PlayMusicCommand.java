package fr.utc.lo23.sharutc.controler.command.player;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;

/**
 * Used when user clic a music to play it all alone. Used for local and remote
 * music, clear playlist and play instantly if music is local, else send a
 * specific request to get file and play it then
 */
public interface PlayMusicCommand extends Command {

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

package fr.utc.lo23.sharutc.controler.command.player;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Peer;

/**
 *
 */
public interface SendMusicToPlayCommand extends Command {

    /**
     *
     * @return
     */
    public Peer getPeer();

    /**
     *
     * @param peer
     */
    public void setPeer(Peer peer);

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

package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.domain.Music;

public class PlayListMusic {

    public Music music;
    private boolean played;

    public PlayListMusic(Music music) {
        this.music = music;
        this.played = false;
    }

    public void setPlaying(boolean b) {
        played = b;
    }

    public boolean isPlaying() {
        return played;
    }
}

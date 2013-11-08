package fr.utc.lo23.sharutc.controler.player;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.net.URL;
import javazoom.jl.decoder.JavaLayerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaybackListenerImpl implements Runnable, PlaybackListener {

    private static final Logger log = LoggerFactory
            .getLogger(PlaybackListenerImpl.class);
    private String filePath;
    private Mp3Player player;
    private Thread playerThread;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final FileService fileService;
    private Music music;

    @Override
    public Music getMusic() {
        return music;
    }

    @Inject
    public PlaybackListenerImpl(PropertyChangeListener pcl, FileService fileService, String filePath) {
        this.fileService = fileService;
        this.filePath = filePath;
        this.propertyChangeSupport.addPropertyChangeListener(pcl);
    }
    // IRunnable members

    @Override
    public void run() {
        try {
            boolean musicHasEnd = !player.resume();
            if (musicHasEnd) {
                log.info("Music has end");
                propertyChangeSupport.firePropertyChange(Property.MUSIC_END.name(), null, null);
            }
        } catch (JavaLayerException ex) {
            log.error(ex.toString());
        }
    }

    public void play() {
        if (player == null) {
            playerInitialize();
        }

        playerThread = new Thread(this, "AudioPlayerThread");
        playerThread.start();
    }

    public void pause() {
        if (player != null && playerThread != null) {
            player.pause();

            playerThread.stop();
            playerThread = null;
        }
    }

    public void pauseToggle() {
        if (player == null || player != null && player.isPaused) {
            play();
        } else {
            pause();
        }
    }

    private void playerInitialize() {
        try {
            String urlAsString = "file:///" + filePath;
            music = fileService.readFile(new File(filePath));
            player = new Mp3Player(new URL(urlAsString), this);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    // PlaybackListener members
    @Override
    public void playbackStarted(PlayerEvent playerEvent) {
    }

    @Override
    public void playbackFinished(PlayerEvent playerEvent) {
    }

    @Override
    public void newFrameIndex(int frameIndexCurrent) {
        Long currentTimeMs = 0L;
        if (music != null) {
            currentTimeMs = new Long((long) (music.getTrackLength() * (float) frameIndexCurrent / music.getFrames()));
        }
        propertyChangeSupport.firePropertyChange(Property.CURRENT_TIME.name(), null, currentTimeMs);
    }

    /**
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public enum Property {

        CURRENT_TIME, MUSIC_END
    }
}
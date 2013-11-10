package fr.utc.lo23.sharutc.controler.player;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.PlayerService;
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
    public static final float MUTE_GAIN = -80F;
    private String filePath;
    private Mp3Player player;
    private Thread playerThread;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private Music music;
    private final FileService fileService;
    private final PlayerService playerService;

    @Override
    public Music getMusic() {
        return music;
    }

    @Inject
    public PlaybackListenerImpl(PlayerService playerService, PropertyChangeListener pcl, FileService fileService, String filePath) {
        this.playerService = playerService;
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

    @Override
    public void play() {
        if (player == null) {
            playerInitialize();
        }
        if (playerThread != null) {
            playerThread.stop();
        }
        playerThread = new Thread(this, "AudioPlayerThread");
        playerThread.start();
    }

    @Override
    public void pause() {
        if (player != null && playerThread != null) {
            player.pause();

            playerThread.stop();
            playerThread = null;
        }
    }

    @Override
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
            setCurrentTime(playerService.getCurrentTimeSec() != null ? playerService.getCurrentTimeSec() : 0L);
            if (playerService.isMute()) {
                player.setGain(MUTE_GAIN);
            } else {
                double gain = 10 * Math.log10((double) playerService.getVolume() / 100);
                player.setGain((float) gain);
            }
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    @Override
    public void setCurrentTime(long mCurrentTimeSec) {
        log.debug("Setting current Frame : ...");
        if (player != null && music != null) {
            int indexCurrentFrame = (int) (mCurrentTimeSec * music.getFrames() / music.getTrackLength());
            if (playerThread != null && !player.isPaused) {
                pause();
                player.setCurrentFrame(indexCurrentFrame);
                play();
            } else {
                player.setCurrentFrame(indexCurrentFrame);
            }
        } else {
            log.warn("Setting current Frame : player is null");
        }
        log.debug("Setting current Frame : DONE");
    }

    @Override
    public void setVolume(int volume) {
        log.debug("Setting volume (current value = {})", player.getMasterGainControl().getValue());
        if (player != null && player.getMasterGainControl() != null) {
            double gain = 10 * Math.log10((double) volume / 100);
            player.getMasterGainControl().setValue((float) gain);
        } else {
        }
    }

    @Override
    public void setMute(boolean mute) {
        if (player != null && player.getMasterGainControl() != null) {
            if (mute) {
                player.getMasterGainControl().setValue((float) MUTE_GAIN);
            } else {
                double gain = 10 * Math.log10((double) playerService.getVolume() / 100);
                player.getMasterGainControl().setValue((float) gain);
            }
        } else {
        }
    }

    private boolean isPlayerPlaying() {
        return player != null && playerThread != null && !player.isPaused;
    }

    @Override
    public void playbackStarted(PlayerEvent playerEvent) {
    }

    @Override
    public void playbackFinished(PlayerEvent playerEvent) {
    }

    @Override
    public void newFrameIndex(int frameIndexCurrent) {
        Long currentTimeSec = 0L;
        if (music != null) {
            currentTimeSec = new Long((long) (music.getTrackLength() * (float) frameIndexCurrent / music.getFrames()));
        }
        propertyChangeSupport.firePropertyChange(Property.CURRENT_TIME.name(), null, currentTimeSec);
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
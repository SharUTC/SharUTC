package fr.utc.lo23.sharutc.controler.player;

import com.google.inject.Inject;
import static fr.utc.lo23.sharutc.controler.player.PlaybackListenerImpl.MUTE_GAIN;
import fr.utc.lo23.sharutc.controler.service.FileService;
import fr.utc.lo23.sharutc.controler.service.PlayerService;
import fr.utc.lo23.sharutc.model.domain.Music;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
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

    /**
     * Create a new instance of the PlaybackListener, one instance is needed by music file
     */
    @Inject
    public PlaybackListenerImpl(PlayerService playerService, PropertyChangeListener pcl, FileService fileService, String filePath) {
        this.playerService = playerService;
        this.fileService = fileService;
        this.filePath = filePath;
        this.propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    /**
     * Run the reading thread. Reading doesn't directly return, thread is
     * waiting for its loop end to send MUSIC_END update
     */
    @Override
    public void run() {
        try {
            boolean musicHasEnd = !player.resume();
            if (musicHasEnd) {
                log.info("Music has end");
                propertyChangeSupport.firePropertyChange(PlaybackListenerImpl.Property.MUSIC_END.name(), null, null);
            }
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void play() {
        log.info("__play");
        if (player == null) {
            playerInitializeWithPlayerServiceTimeAndVolume();
            playerThread = new Thread(this, "AudioPlayerThread");
            playerThread.start();
        } else {
            player.unpause();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
        log.info("__pause");
        if (player != null && playerThread != null) {
            player.pause();
        }
    }

    @Override
    public void stop() {
        log.info("__stop");
        if (player != null && playerThread != null) {
            player.stop();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pauseToggle() {
        if (player == null || player != null && player.paused) {
            play();
        } else {
            pause();
        }
    }

    private void playerInitializeWithPlayerServiceTimeAndVolume() {
        try {
            String urlAsString = "file:///" + filePath;
            music = fileService.fakeMusicFromFile(new File(filePath));
            player = new Mp3Player(new URL(urlAsString), this);
            setCurrentTime(playerService.getCurrentTimeSec() != null ? playerService.getCurrentTimeSec() : 0L);
            if (playerService.isMute()) {
                player.setGain(MUTE_GAIN);
            } else {
                double gain = volumeToGain(playerService.getVolume());
                player.setGain((float) gain);
            }
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Music getMusic() {
        return music;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentTime(long timeSec) {
        log.debug("Setting current Frame : ...");
        if (player != null && music != null) {
            int indexCurrentFrame = timeToFrame(timeSec);
            player.changeCurrentFrame(indexCurrentFrame);
        } else {
            log.warn("Setting current Frame : player is null");
        }
        log.debug("Setting current Frame : DONE");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVolume(int volume) {
        log.debug("Setting volume (current value = {})", player.getMasterGainControl().getValue());
        if (player != null && player.getMasterGainControl() != null) {
            double gain = volumeToGain(volume);
            player.getMasterGainControl().setValue((float) gain);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMute(boolean mute) {
        if (player != null && player.getMasterGainControl() != null) {
            if (mute) {
                player.getMasterGainControl().setValue((float) MUTE_GAIN);
            } else {
                double gain = volumeToGain(playerService.getVolume());
                player.getMasterGainControl().setValue((float) gain);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playbackEvent(PlayerEvent playerEvent) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentFrameIndex(int currentFrameIndex) {
        Long currentTimeSec = 0L;
        if (music != null) {
            currentTimeSec = new Long((long) (music.getTrackLength() * (float) currentFrameIndex / music.getFrames()));
        }
        propertyChangeSupport.firePropertyChange(PlaybackListenerImpl.Property.CURRENT_TIME.name(), null, currentTimeSec);
    }

    /**
     * {@inheritDoc}
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private double volumeToGain(int volume) {
        return 10 * Math.log10((double) volume / 100);
    }

    private int timeToFrame(long time) {
        int frames = 0;
        if (music != null && music.getFrames() != null && music.getTrackLength() != null) {
            if (time > music.getTrackLength()) {
                time = music.getTrackLength();
            }
            frames = (int) (time * music.getFrames() / music.getTrackLength());
        }
        return frames;
    }

    /**
     * {@inheritDoc}
     */
    public enum Property {

        /**
         * {@inheritDoc}
         */
        CURRENT_TIME,
        /**
         * {@inheritDoc}
         */
        MUSIC_END
    }
}
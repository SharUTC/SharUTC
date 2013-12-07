package fr.utc.lo23.sharutc.controler.player;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import javax.sound.sampled.FloatControl;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.JavaSoundAudioDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This class is loosely based on javazoom.jl.player.AdvancedPlayer.
public class Mp3Player {

    private static final Logger log = LoggerFactory
            .getLogger(Mp3Player.class);
    public static final int CORRECTION_FACTOR_FRAMES = 0; //52
    private URL urlToStreamFrom;
    private Bitstream bitstream;
    private Decoder decoder;
    private AudioDevice audioDevice;
    //  private boolean isClosed = false;
    //   private boolean isComplete = false;
    private final PlaybackListener listener;
    private int frameIndexCurrent = 0;
    public boolean paused = false;
    private Float gain = null;
    private boolean stillFramesToRead = true;
    private Integer newCurrentFrameIndex;
    private boolean turnDownThread;
    private boolean terminated;

    public Mp3Player(URL urlToStreamFrom, final PlaybackListener listener) throws Exception {
        this.urlToStreamFrom = urlToStreamFrom;
        this.listener = listener;
    }

    public boolean resume() throws Exception {
        return play(frameIndexCurrent);
    }

    public boolean play() throws Exception {
        return play(0);
    }

    public boolean play(int frameIndexStart) throws Exception {
        return play(frameIndexStart, listener.getMusic().getFrames().intValue(), CORRECTION_FACTOR_FRAMES);
    }

    public boolean play(int frameIndexStart, int frameIndexFinal, int correctionFactorInFrames) throws Exception {
        terminated = false;
        bitstream = new Bitstream(urlToStreamFrom.openStream());
        audioDevice = FactoryRegistry.systemRegistry().createAudioDevice();
        decoder = new Decoder();
        audioDevice.open(decoder);

        while (/*!isComplete && */stillFramesToRead
                && frameIndexCurrent < frameIndexStart - correctionFactorInFrames) {
            stillFramesToRead = skipFrame();
            updateCurrentFrameIndex(false);
        }

        listener.playbackEvent(new PlayerEvent(this, PlayerEventType.STARTED, audioDevice.getPosition()));

        if (frameIndexFinal < 0) {
            frameIndexFinal = Integer.MAX_VALUE;
        }

        while (!turnDownThread && stillFramesToRead
                && frameIndexCurrent < frameIndexFinal) {
            if (paused) {
                try {
                    Thread.sleep(10);
                } catch (Exception ex) {
                    log.error(ex.toString());
                }
            } else {
                if (newCurrentFrameIndex != null) {
                    reload(newCurrentFrameIndex.intValue(), correctionFactorInFrames);
                    newCurrentFrameIndex = null;
                }
                stillFramesToRead = decodeFrame();
                if (gain != null) {
                    getMasterGainControl().setValue(gain);
                    gain = null;
                }
                updateCurrentFrameIndex(true);
            }
        }

        // last frame played, ensure all data flushed to the audio device.
        if (audioDevice != null) {
            audioDevice.flush();

            synchronized (this) {
                /*  isComplete = (isClosed == false);*/
                closeAudioDeviceAndBitStream();
            }

            // report to listener
            if (listener != null) {
                if (!turnDownThread/* && isComplete*/) {
                    listener.playbackEvent(
                            new PlayerEvent(
                            this,
                            PlayerEventType.STOPPED, audioDevice != null
                            ? audioDevice.getPosition() : 0));
                } else {
                    listener.playbackEvent(new PlayerEvent(this, PlayerEventType.PAUSED, frameToTime(frameIndexCurrent)));
                }

            }
        }
        if (turnDownThread) {
            stillFramesToRead = true;
        }
        terminated = true;
        log.info("End of AudioPlayerThread");
        return stillFramesToRead;
    }

    private void reload(int frameIndexStart, int correctionFactorInFrames) {
        log.info("reloading bitstream...");
        try {
            closeAudioDeviceAndBitStream();
            try {
                bitstream = new Bitstream(
                        urlToStreamFrom.openStream());
            } catch (IOException ex) {
                log.error("failed to create bitsream");
            }
            frameIndexCurrent = 0;
            audioDevice = FactoryRegistry.systemRegistry().createAudioDevice();
            decoder = new Decoder();
            audioDevice.open(decoder);

            while (/*!isComplete && */stillFramesToRead == true
                    && frameIndexCurrent < frameIndexStart - correctionFactorInFrames) {
                stillFramesToRead = skipFrame();
                updateCurrentFrameIndex(false);
            }
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        log.info("reloading bitstream DONE");

    }

    /**
     * Bloque le thread en cours
     */
    public void pause() {
        paused = true;
        listener.playbackEvent(new PlayerEvent(this, PlayerEventType.PAUSED, audioDevice.getPosition()));
    }

    /**
     * reprend la lecture, player est en pause dans un while
     */
    public void unpause() {
        paused = false;
        listener.playbackEvent(new PlayerEvent(this, PlayerEventType.STARTED, audioDevice.getPosition()));
    }

    /**
     * met fin au thread en cours avant la fin de la musique
     */
    public void stop() {
        turnDownThread = true;
        while (!terminated) {
            try {
                log.debug("Waiting for player to Stop");
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                log.info("Error while waiting for player to Stop");
            }
        }
    }

    public synchronized void closeAudioDeviceAndBitStream() {
        if (audioDevice != null) {
            audioDevice.close();
            audioDevice = null;
        }
        if (bitstream != null) {
            try {
                bitstream.close();
                bitstream = null;
            } catch (Exception ex) {
                log.error(ex.toString());
            }
        }
        //  isClosed = true;
    }

    private boolean decodeFrame() throws JavaLayerException {
        boolean continueDecoding = true;
        try {
            if (audioDevice != null) {
                Header header = bitstream.readFrame();
                if (header != null) {
                    // sample buffer set when decoder constructed
                    SampleBuffer output = (SampleBuffer) decoder.decodeFrame(
                            header, bitstream);
                    synchronized (this) {
                        if (audioDevice != null) {
                            audioDevice.write(
                                    output.getBuffer(),
                                    0,
                                    output.getBufferLength());
                        }
                    }
                    bitstream.closeFrame();
                }
            }
        } catch (RuntimeException ex) {
            log.trace("End of music ({})", ex.toString());
            continueDecoding = false;
        }
        return continueDecoding;
    }

    private boolean skipFrame() throws JavaLayerException {
        boolean skipped = false;
        Header header = bitstream.readFrame();
        if (header != null) {
            bitstream.closeFrame();
            skipped = true;
        }
        return skipped;
    }

    private void updateCurrentFrameIndex(boolean propagateToUI) {
        frameIndexCurrent++;
        if (propagateToUI) {
            listener.setCurrentFrameIndex(frameIndexCurrent);
        }
    }

    public FloatControl getMasterGainControl() {
        log.debug("getMasterGainControl");
        FloatControl floatControl = null;
        if (audioDevice != null && audioDevice instanceof JavaSoundAudioDevice) {
            JavaSoundAudioDevice javaSoundAudioDevice = (JavaSoundAudioDevice) audioDevice;
            floatControl = javaSoundAudioDevice.getMasterGainControl();
        }
        return floatControl;
    }

    public void changeCurrentFrame(int newCurrentFrameIndex) {
        this.newCurrentFrameIndex = new Integer(newCurrentFrameIndex);
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    private int frameToTime(int frame) {
        int time = 0;
        if (listener != null && listener.getMusic() != null && listener.getMusic().getTrackLength() != null && listener.getMusic().getFrames() != null) {
            time = (int) ((frame * (double) listener.getMusic().getTrackLength()) / listener.getMusic().getFrames());
        }
        return time;
    }
}
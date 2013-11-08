package fr.utc.lo23.sharutc.controler.player;

import java.io.IOException;
import java.net.URL;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
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
    private boolean isClosed = false;
    private boolean isComplete = false;
    private PlaybackListener listener;
    private int frameIndexCurrent;
    public boolean isPaused;

    public Mp3Player(URL urlToStreamFrom, PlaybackListener listener) throws JavaLayerException {
        this.urlToStreamFrom = urlToStreamFrom;
        this.listener = listener;
    }

    public void pause() {
        isPaused = true;
        close();
    }

    public boolean resume() throws JavaLayerException {
        return play(frameIndexCurrent);
    }

    public boolean play() throws JavaLayerException {
        return play(0);
    }

    public boolean play(int frameIndexStart) throws JavaLayerException {
        return play(frameIndexStart, listener.getMusic().getFrames().intValue(), CORRECTION_FACTOR_FRAMES);
    }

    public boolean play(int frameIndexStart, int frameIndexFinal, int correctionFactorInFrames) throws JavaLayerException {
        try {
            bitstream = new Bitstream(
                    urlToStreamFrom.openStream());
        } catch (IOException ex) {
            log.error("failed to create bitsream");
        }

        audioDevice = FactoryRegistry.systemRegistry().createAudioDevice();
        decoder = new Decoder();
        audioDevice.open(decoder);

        boolean shouldContinueReadingFrames = true;

        isPaused = false;
        frameIndexCurrent = 0;

        while (!isComplete && shouldContinueReadingFrames == true
                && frameIndexCurrent < frameIndexStart - correctionFactorInFrames) {
            shouldContinueReadingFrames = skipFrame();
            updateCurrentFrameIndex();
        }

        if (listener != null) {
            listener.playbackStarted(
                    new PlayerEvent(this, PlayerEventType.STARTED, audioDevice.getPosition()));
        }

        if (frameIndexFinal < 0) {
            frameIndexFinal = Integer.MAX_VALUE;
        }

        while (shouldContinueReadingFrames == true
                && frameIndexCurrent < frameIndexFinal) {
            if (isPaused == true) {
                shouldContinueReadingFrames = false;
                try {
                    Thread.sleep(1);
                } catch (Exception ex) {
                    log.error(ex.toString());
                }
            } else {
                shouldContinueReadingFrames = decodeFrame();
                updateCurrentFrameIndex();
            }
        }

        // last frame, ensure all data flushed to the audio device.
        if (audioDevice != null) {
            audioDevice.flush();

            synchronized (this) {
                isComplete = (isClosed == false);
                close();
            }

            // report to listener
            if (listener != null) {
                listener.playbackFinished(
                        new PlayerEvent(
                        this,
                        PlayerEventType.STOPPED, audioDevice != null
                        ? audioDevice.getPosition() : 0));
            }
        }
        return shouldContinueReadingFrames;
    }

    public void stop() {
        listener.playbackFinished(
                new PlayerEvent(
                this,
                PlayerEventType.STOPPED,
                audioDevice.getPosition()));
        close();
    }

    public synchronized void close() {
        if (audioDevice != null) {
            isClosed = true;

            audioDevice.close();

            audioDevice = null;

            try {
                bitstream.close();
            } catch (Exception ex) {
                log.error(ex.toString());
            }
        }
    }

    protected boolean decodeFrame() throws JavaLayerException {
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

    protected boolean skipFrame() throws JavaLayerException {
        boolean returnValue = false;

        Header header = bitstream.readFrame();

        if (header != null) {
            bitstream.closeFrame();
            returnValue = true;
        }

        return returnValue;
    }

    private void updateCurrentFrameIndex() {
        frameIndexCurrent++;
        listener.newFrameIndex(frameIndexCurrent);
    }
}
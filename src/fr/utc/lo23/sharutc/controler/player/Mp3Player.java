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

/**
 * Open a stream from a mp3 file and play its content until the end, player can
 * be started, paused and stopped. Audio gain and current time may also be
 * modified with this class, current time changes are checked between every
 * frame read, gain uses a small tweak on JLayer librairy to give access to gain
 * control
 */
public class Mp3Player {

    private static final Logger log = LoggerFactory
            .getLogger(Mp3Player.class);
    public static final int CORRECTION_FACTOR_FRAMES = 0; //52
    private URL urlToStreamFrom;
    private Bitstream bitstream;
    private Decoder decoder;
    private AudioDevice audioDevice;
    private final PlaybackListener listener;
    private int frameIndexCurrent = 0;
    public boolean paused = false;
    private Float gain = null;
    private boolean stillFramesToRead = true;
    private Integer newCurrentFrameIndex;
    private boolean turnDownThread;
    private boolean terminated;

    /**
     * Create a new instance of the Mp3Player, use listener's methods to manage
     * events (may be send without user action at the end of the music)
     *
     * @param urlToStreamFrom the complete file location on the system
     * @param listener the listener of events such as STARTED, PAUSED and
     * STOPPED
     *
     * @throws Exception might occur if the mp3 file is corrupted
     */
    public Mp3Player(URL urlToStreamFrom, final PlaybackListener listener) throws Exception {
        this.urlToStreamFrom = urlToStreamFrom;
        this.listener = listener;
    }

    /**
     * Play the music from the current frame Index to its end, send STARTED and
     * PAUSED event, use returned value to listen to thread's end (which occurs
     * on error and at the end of the music). Default value for
     * frameIndexCurrent is 0
     *
     * @return false when then music stops by itself, true if the music is
     * paused or an error happens
     * @throws Exception might occur if the mp3 file is corrupted
     */
    public boolean resume() throws Exception {
        return play(frameIndexCurrent);
    }

    /**
     * Play the music from its beginning to its end, send STARTED and PAUSED
     * event, use returned value to listen to thread's end (which occurs on
     * error and at the end of the music)
     *
     * @return false when then music stops by itself, true if the music is
     * paused or an error happens
     * @throws Exception might occur if the mp3 file is corrupted
     */
    public boolean play() throws Exception {
        return play(0);
    }

    /**
     * Play the music from frameIndexStart to its end, send STARTED and PAUSED
     * event, use returned value to listen to thread's end (which occurs on
     * error and at the end of the music)
     *
     * @param frameIndexStart the first frame to read
     * @return false when then music stops by itself, true if the music is
     * paused or an error happens
     * @throws Exception might occur if the mp3 file is corrupted
     */
    public boolean play(int frameIndexStart) throws Exception {
        return play(frameIndexStart, listener.getMusic().getFrames().intValue(), CORRECTION_FACTOR_FRAMES);
    }

    /**
     * Play the music from frameIndexStart to frameIndexFinal, send STARTED and
     * PAUSED event, use returned value to listen to thread's end (which occurs
     * on error and at the end of the music)
     *
     * @param frameIndexStart the first frame to read
     * @param frameIndexFinal the last frame to read
     * @param correctionFactorInFrames if some delay appears (NOT USED)
     * @return false when then music stops by itself, true if the music is
     * paused or an error happens
     * @throws Exception might occur if the mp3 file is corrupted
     */
    public boolean play(int frameIndexStart, int frameIndexFinal, int correctionFactorInFrames) throws Exception {
        terminated = false;
        bitstream = new Bitstream(urlToStreamFrom.openStream());
        try {
            audioDevice = FactoryRegistry.systemRegistry().createAudioDevice();
            decoder = new Decoder();
            audioDevice.open(decoder);
        } catch (Exception ex) {
            log.warn("Cannot create AudioDevice");
        }
        while (stillFramesToRead
                && frameIndexCurrent < frameIndexStart - correctionFactorInFrames) {
            stillFramesToRead = skipFrame();
            updateCurrentFrameIndex(false);
        }

        listener.playbackEvent(new PlayerEvent(this, PlayerEventType.STARTED, audioDevice != null ? audioDevice.getPosition() : 0));

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
                    if (getMasterGainControl() != null) {
                        getMasterGainControl().setValue(gain);
                    }
                    gain = null;
                }
                updateCurrentFrameIndex(true);
            }
        }

        // last frame played, ensure all data flushed to the audio device.
        if (audioDevice != null) {
            audioDevice.flush();

            synchronized (this) {
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

    /**
     * When the next frame index to read is changed, music has to be read from
     * the beginning because it uses a Bitstream to progress in the file
     *
     * @param frameIndexStart the new position of the music during the while
     * loop
     * @param correctionFactorInFrames if some delay appears (NOT USED)
     */
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
            try {
                audioDevice = FactoryRegistry.systemRegistry().createAudioDevice();
                decoder = new Decoder();
                audioDevice.open(decoder);
            } catch (Exception ex) {
                log.warn("Cannot create AudioDevice");
            }
            while (stillFramesToRead == true && frameIndexCurrent < frameIndexStart - correctionFactorInFrames) {
                stillFramesToRead = skipFrame();
                updateCurrentFrameIndex(false);
            }
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        log.info("reloading bitstream DONE");

    }

    /**
     * Pauses the thread to pause the music reading, send PAUSED event
     */
    public void pause() {
        paused = true;
        listener.playbackEvent(new PlayerEvent(this, PlayerEventType.PAUSED, audioDevice != null ? audioDevice.getPosition() : 0));
    }

    /**
     * Wakes up the thread to continue playing the music, send STARTED event
     */
    public void unpause() {
        paused = false;
        listener.playbackEvent(new PlayerEvent(this, PlayerEventType.STARTED, audioDevice != null ? audioDevice.getPosition() : 0));
    }

    /**
     * Stops the thread, and wait for its complete end before returning to avoid
     * duplication of this thread
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

    /**
     * Try to close the audio device and the bitstream from mp3 file
     */
    private void closeAudioDeviceAndBitStream() {
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
    }

    /**
     * Read a frame from the music and write it in audio device's buffer to play
     * a sound
     *
     * @return true if there is no error at reading the frame, if not so the
     * reading loop has to be stopped
     *
     * @throws JavaLayerException
     */
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

    /**
     * When the music is paused or is started directly at an other frame index
     * than 0, since the reading must start from the beginning of the file we
     * have to read its content until the right frame without writing it in the
     * audio buffer
     *
     * @return true to confirm that a frame was read and closed without trouble
     * @throws JavaLayerException might occur if the mp3 file is corrupted or if
     * the bitstream is going to far
     */
    private boolean skipFrame() throws JavaLayerException {
        boolean skipped = false;
        Header header = bitstream.readFrame();
        if (header != null) {
            bitstream.closeFrame();
            skipped = true;
        }
        return skipped;
    }

    private void updateCurrentFrameIndex(boolean informListener) {
        frameIndexCurrent++;
        if (informListener) {
            listener.setCurrentFrameIndex(frameIndexCurrent);
        }
    }

    /**
     * Allows the user to control the audio gain. Uses a modification of JLayer
     * librairy to give access to a private member from one of its class
     *
     * @return the floatControl on which a gain in dB is settable
     */
    public FloatControl getMasterGainControl() {
        log.debug("getMasterGainControl");
        FloatControl floatControl = null;
        if (audioDevice != null && audioDevice instanceof JavaSoundAudioDevice) {
            JavaSoundAudioDevice javaSoundAudioDevice = (JavaSoundAudioDevice) audioDevice;
            floatControl = javaSoundAudioDevice.getMasterGainControl();
        }
        return floatControl;
    }

    /**
     * Set the next position of frame to read, lead to restart reading at the
     * beginning of the file
     *
     * @param newCurrentFrameIndex if < 0 or too big, music will played until
     * its end
     */
    public void changeCurrentFrame(int newCurrentFrameIndex) {
        this.newCurrentFrameIndex = new Integer(newCurrentFrameIndex);
    }

    /**
     * Set the audio gain, the value is read during the while loop if not null
     *
     * @param gain the gain in dB (from -30dB to 0dB)
     */
    public void setGain(float gain) {
        this.gain = gain;
    }

    /**
     * Convert frame index into a integer representing the current time position
     * in the music
     *
     * @param frame the frame index to convert to seconds
     * @return the time equivalent value of this frame index, or 0 if no music
     * is played or a value (track length in seconds or total frames of the
     * file) is missing
     */
    private int frameToTime(int frame) {
        int time = 0;
        if (listener != null && listener.getMusic() != null && listener.getMusic().getTrackLength() != null && listener.getMusic().getFrames() != null) {
            time = (int) ((frame * (double) listener.getMusic().getTrackLength()) / listener.getMusic().getFrames());
        }
        return time;
    }
}
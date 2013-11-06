package fr.utc.lo23.sharutc.controler.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class PlayerLib {

    private static int pausedOnFrame = 0;

    public static void main(String[] args) {
        try {
            AdvancedPlayer player = new AdvancedPlayer(new FileInputStream(new File(new File(".").getCanonicalPath() + "\\test\\mp3\\Sting & The Police - The Very Best Of Sting & The Police - 17 - Roxanne.mp3")));
            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackStarted(PlaybackEvent event) {
                    super.playbackStarted(event);
                }

                @Override
                public void playbackFinished(PlaybackEvent event) {
                    super.playbackFinished(event);
                    pausedOnFrame = event.getFrame();
                }
            });
            if (pausedOnFrame != 0) {
                player.play(pausedOnFrame);
            } else {
                player.play();
            }
            /*PlayerLib t = new PlayerLib();
             try {
             t.play(new File(".").getCanonicalPath() + "\\test\\mp3\\Sting & The Police - The Very Best Of Sting & The Police - 17 - Roxanne.mp3");
             } catch (IOException ex) {
             ex.printStackTrace();
             }*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void play(String filename) {
        try {
            File file = new File(filename);
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);

            AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
            // Play now
            rawplay(decodedFormat, din);
            in.close();
        } catch (Exception e) {
            //Handle exception.
        }
    }

    private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException {
        byte[] data = new byte[4096];
        SourceDataLine line = getLine(targetFormat);
        if (line != null) {
            // Start
            line.start();
            int nBytesRead = 0;
            while (nBytesRead != -1) {
                nBytesRead = din.read(data, 0, data.length);
                if (nBytesRead != -1) {
                    line.write(data, 0, nBytesRead);
                }
            }
            // Stop
            line.drain();
            line.stop();
            line.close();
            din.close();
        }
    }

    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }
}

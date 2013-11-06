package fr.utc.lo23.sharutc.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 */
public class PlayerController implements Initializable {

    //TODO remove once we get a real song
    private static final int SONG_TIME_IN_SECONDS = 206;
    private static final Logger log = LoggerFactory
            .getLogger(PlayerController.class);
    public Slider playerTimeSlider;
    public ProgressBar playerProgressBar;
    public Label playerCurrentTime;
    public Label playerMaxTime;
    private int mCurrentTimeInSeconds;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO remove once we get a real song
        playerMaxTime.setText(timeInSecondsToString(SONG_TIME_IN_SECONDS));
        updateCurrentSongTime(0.7);

        playerTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                log.info("sliderValueChanged: " + String.valueOf(t1));
                updateCurrentSongTime((Double) t1);
            }
        });
    }

    private void updateCurrentSongTime(double percent) {
        mCurrentTimeInSeconds = (int)(SONG_TIME_IN_SECONDS  * percent);
        playerCurrentTime.setText(timeInSecondsToString(mCurrentTimeInSeconds));
        playerProgressBar.setProgress(percent);
    }

    private String timeInSecondsToString(int timeInSeconds) {
        final int minutes = timeInSeconds / 60;
        return String.format("%02d:%02d", minutes, timeInSeconds - minutes * 60);
    }
}

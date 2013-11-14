package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.RatingStar;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
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
    public RatingStar ratingStar1;
    public RatingStar ratingStar2;
    public RatingStar ratingStar3;
    public RatingStar ratingStar4;
    public RatingStar ratingStar5;
    private int mCurrentTimeInSeconds;
    private RatingStar[] mRatingStars;
    //TODO Remove once we get a real rating
    private int mSongRating = 3;

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

        mRatingStars = new RatingStar[]{
                ratingStar1,
                ratingStar2,
                ratingStar3,
                ratingStar4,
                ratingStar5
        };

        displayCurrentRating();

    }

    /**
     * Player part
     *
     * @param music music to add
     */
    public void addSong(Music music) {
        log.info("new music added to the player " + music.getFileName());
    }

    private void updateCurrentSongTime(double percent) {
        mCurrentTimeInSeconds = (int) (SONG_TIME_IN_SECONDS * percent);
        playerCurrentTime.setText(timeInSecondsToString(mCurrentTimeInSeconds));
        playerProgressBar.setProgress(percent);
    }

    private String timeInSecondsToString(int timeInSeconds) {
        final int minutes = timeInSeconds / 60;
        return String.format("%02d:%02d", minutes, timeInSeconds - minutes * 60);
    }

    //******************
    //*  Rating system
    //******************

    private void displayCurrentRating() {
        fillRatingStar(mSongRating);
    }

    private void fillRatingStar(int rate) {
        for (int i = 0; i < 5; i++) {
            if (i < rate) {
                mRatingStars[i].fill(true);
            } else {
                mRatingStars[i].fill(false);
            }
        }
    }

    public void handleMouseEnteredRatingStar(MouseEvent mouseEvent) {
        final Object source = mouseEvent.getSource();
        int previewRate = 1;
        if (source == ratingStar2) {
            previewRate = 2;
        } else if (source == ratingStar3) {
            previewRate = 3;
        } else if (source == ratingStar4) {
            previewRate = 4;
        } else if (source == ratingStar5) {
            previewRate = 5;
        }

        fillRatingStar(previewRate);
    }

    public void handleMouseExitedRatingStar(MouseEvent mouseEvent) {
        displayCurrentRating();
    }

    public void handleMouseClickedRatingStar(MouseEvent mouseEvent) {
        final Object source = mouseEvent.getSource();
        if (source == ratingStar5) {
            mSongRating = 5;
        } else if (source == ratingStar4) {
            mSongRating = 4;
        } else if (source == ratingStar3) {
            mSongRating = 3;
        } else if (source == ratingStar2) {
            mSongRating = 2;
        } else if (source == ratingStar1) {
            if (mSongRating == 1) {
                mSongRating = 0;
            } else {
                mSongRating = 1;
            }

        }
    }
}

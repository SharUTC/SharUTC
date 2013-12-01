package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.music.SetScoreCommand;
import fr.utc.lo23.sharutc.controler.service.PlayerService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.Score;
import fr.utc.lo23.sharutc.ui.custom.RatingStar;
import fr.utc.lo23.sharutc.ui.custom.SliderScrollHandler;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;

/**
 * FXML Controller class
 */
public class PlayerController implements Initializable, PropertyChangeListener {

    //Images for the speaker
    private static final ImageView IC_SPEAKER = new ImageView("/fr/utc/lo23/sharutc/ui/drawable/ic_speaker.png");
    private static final ImageView IC_SPEAKER_MUTED = new ImageView("/fr/utc/lo23/sharutc/ui/drawable/ic_speaker_muted.png");
    private static final ImageView BT_PAUSE = new ImageView("/fr/utc/lo23/sharutc/ui/drawable/pause_button_small.png");
    private static final ImageView BT_PLAY = new ImageView("/fr/utc/lo23/sharutc/ui/drawable/play_button_small.png");
    //TODO remove once we get a real song
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
    public Slider speakerSlider;
    public Polygon speakerLevel;
    public Button speakerButton;
    public Button buttonPlay;
    public Label currentMusicTitle;
    public Label currentMusicAlbum;
    public Label currentMusicArtist;
    private int mCurrentTimeInSeconds;
    private RatingStar[] mRatingStars;
    @Inject
    private PlayerService mPlayerService;
    @Inject
    private SetScoreCommand mSetScoreCommand;
    @Inject
    private AppModel mAppModel;
    private Music mCurrentMusic;
    private double mCurrentPercent = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mPlayerService.addPropertyChangeListener(this);
        playerTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                log.info("Player Time Slider Value Changed: " + String.valueOf(t1));
                if (mCurrentMusic != null) {

                    if (mCurrentPercent == t1.doubleValue()) {
                        return;
                    }
                    mPlayerService.setCurrentTimeSec(t1.longValue() * mPlayerService.getTotalTimeSec());
                }
            }
        });
        playerTimeSlider.setOnScroll(new SliderScrollHandler());

        speakerSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {

                log.info("Speaker Slider Value Change: " + String.valueOf(newValue));
                mPlayerService.setVolume((int) (newValue.doubleValue() * 100));

            }
        });
        speakerSlider.setOnScroll(new SliderScrollHandler());
        speakerSlider.setValue(mPlayerService.getVolume() * 0.01f);

        mRatingStars = new RatingStar[]{
            ratingStar1,
            ratingStar2,
            ratingStar3,
            ratingStar4,
            ratingStar5
        };
        displayCurrentRating();

    }

    public void onDetach() {
        mPlayerService.removePropertyChangeListener(this);
    }

    /**
     * Player part
     *
     * @param music music to add
     */
    public void addSong(Music music) {
        log.info("new music added to the player " + music.getFileName());
    }

    public void onCurrentMusicUpdate(Music music) {
        mCurrentMusic = music;
        if (mCurrentMusic != null) {

            playerMaxTime.setText(timeInSecondsToString(mPlayerService.getTotalTimeSec().intValue()));
            currentMusicTitle.setText(mCurrentMusic.getTitle());
            currentMusicAlbum.setText(mCurrentMusic.getAlbum());
            currentMusicArtist.setText(mCurrentMusic.getArtist());
            displayCurrentRating();
        } else {
            playerMaxTime.setText(timeInSecondsToString(0));
        }
    }

    private void updateSpeakerLevel(double oldValue, double newValue) {
        speakerLevel.getPoints().clear();
        speakerLevel.getPoints().addAll(new Double[]{0.0, 20.0, 25 + 60.0 * newValue, 20.0, 25 + 60.0 * newValue, 15.0 * (1 - newValue)});
        if (newValue == 0.0) {
            mPlayerService.setMute(true);
        } else if (oldValue == 0.0) {
            mPlayerService.setMute(false);
        }
    }

    private void updateCurrentSongTime(double percent) {
        mCurrentPercent = percent;
        if (mCurrentMusic != null) {
            mCurrentTimeInSeconds = (int) (mPlayerService.getTotalTimeSec() * percent);
        } else {
            mCurrentTimeInSeconds = 0;
        }
        playerCurrentTime.setText(timeInSecondsToString(mCurrentTimeInSeconds));
        playerProgressBar.setProgress(percent);
    }

    private String timeInSecondsToString(int timeInSeconds) {
        final int minutes = timeInSeconds / 60;
        return String.format("%02d:%02d", minutes, timeInSeconds - minutes * 60);
    }

    public void handlePrevAction(ActionEvent actionEvent) {
        mPlayerService.playerPrevious();
    }

    public void handleNextAction(ActionEvent actionEvent) {
        mPlayerService.playerNext();
    }

    public void handlePlayAction(ActionEvent actionEvent) {
        if (mPlayerService.isPause()) {
            mPlayerService.playerPlay();
        } else {
            mPlayerService.playerPause();
        }

    }

    public void handleSpeakerAction(ActionEvent actionEvent) {
        mPlayerService.setMute(!mPlayerService.isMute());
    }

    //******************
    //*  Rating system
    //******************
    private void displayCurrentRating() {
        fillRatingStar(getCurrentSongScore());
    }

    private int getCurrentSongScore() {
        int currentSongValue = 0;
        if (mCurrentMusic != null) {
            final Score currentScore = mCurrentMusic.getScore(mAppModel.getProfile().getUserInfo().toPeer());
            if (currentScore != null) {
                currentSongValue = currentScore.getValue();
            }
        }
        return currentSongValue;
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
        if (mCurrentMusic != null) {
            final Object source = mouseEvent.getSource();
            int newCandidateRate = 0;
            final int currentSongScore = getCurrentSongScore();
            if (source == ratingStar5) {
                newCandidateRate = 5;
            } else if (source == ratingStar4) {
                newCandidateRate = 4;
            } else if (source == ratingStar3) {
                newCandidateRate = 3;
            } else if (source == ratingStar2) {
                newCandidateRate = 2;
            } else if (source == ratingStar1) {
                if (currentSongScore != 1) {
                    newCandidateRate = 1;
                }
            }

            if (newCandidateRate != currentSongScore) {
                log.debug("new rate : " + String.valueOf(newCandidateRate));
                mSetScoreCommand.setMusic(mCurrentMusic);
                mSetScoreCommand.setScore(newCandidateRate);
                mSetScoreCommand.setPeer(mAppModel.getProfile().getUserInfo().toPeer());
                mSetScoreCommand.execute();
            }
        }

    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        //Warning - most of the changes on the PlayerService properties
        //are not fired from the UI thread but from the AudioPlayerThread
        //therefore you can't update the UI directly.
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                handlePropertyChangeFromAudiPlayerThread(evt);
            }
        });
    }

    private void handlePropertyChangeFromAudiPlayerThread(final PropertyChangeEvent evt) {
        final String propertyName = evt.getPropertyName();
        if (propertyName.equals(PlayerService.Property.CURRENT_MUSIC.name())) {
            Music m = (Music) evt.getNewValue();
            onCurrentMusicUpdate(m);
        } else if (propertyName.equals(PlayerService.Property.CURRENT_TIME.name())) {
            updateCurrentSongTime(((Long) evt.getNewValue()).floatValue() / mPlayerService.getTotalTimeSec().floatValue());
            playerTimeSlider.valueProperty().setValue(mCurrentPercent);
        } else if (propertyName.equals(PlayerService.Property.MUTE.name())) {
            if ((Boolean) evt.getNewValue()) {
                speakerButton.setGraphic(IC_SPEAKER_MUTED);
            } else {
                speakerButton.setGraphic(IC_SPEAKER);
            }
        } else if (propertyName.equals(PlayerService.Property.PAUSE.name())) {
            if ((Boolean) evt.getNewValue()) {
                buttonPlay.setGraphic(BT_PAUSE);
            } else {
                buttonPlay.setGraphic(BT_PLAY);
            }
        } else if (propertyName.equals(PlayerService.Property.VOLUME.name())) {
            updateSpeakerLevel((Integer) evt.getOldValue() * 0.01f, (Integer) evt.getNewValue() * 0.01f);
        }
    }
}

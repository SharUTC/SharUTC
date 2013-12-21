package fr.utc.lo23.sharutc.ui.util;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Toast {

    private static final int SPACING = 10;
    private static final int TOAST_IN_DURATION_MILLI = 500;
    private static final int TOAST_OUT_DURATION_MILLI = 500;
    private static final int TOAST_DURATION_MILLI = 1000;
    private ParallelTransition mToastInTransition;
    private ParallelTransition mToastOutTransition;
    private Pane mToastBox;
    private Label mToastText;
    private static final Logger log = LoggerFactory.getLogger(Toast.class);

    public Toast(Pane toastBox, Label toastText) {
        mToastBox = toastBox;
        mToastText = toastText;
        initAnimation();
    }

    public void make(String message) {
        if (mToastBox == null || mToastText == null) {
            log.error("Should initialize Toast first, see Toast.init");
        } else {
            mToastText.setText(message);
            if (mToastInTransition.getStatus().equals(Animation.Status.RUNNING)) {
                mToastInTransition.stop();
            }
            if (mToastOutTransition.getStatus().equals(Animation.Status.RUNNING)) {
                mToastOutTransition.stop();
            }
            mToastInTransition.play();
        }
    }

    private void initAnimation() {
        //init ToastOut animation
        FadeTransition fadeOut = new FadeTransition(Duration.millis(TOAST_OUT_DURATION_MILLI), mToastBox);
        fadeOut.setFromValue(1.0f);
        fadeOut.setToValue(0.0f);
        TranslateTransition translateOut =
                new TranslateTransition(Duration.millis(TOAST_OUT_DURATION_MILLI), mToastBox);
        translateOut.setFromX(mToastBox.getBoundsInLocal().getMaxX() + SPACING);
        translateOut.setToX(-mToastBox.getBoundsInParent().getWidth());
        mToastOutTransition = new ParallelTransition();
        mToastOutTransition.getChildren().addAll(
                fadeOut//TODO ,translateOut
                );
        mToastOutTransition.setCycleCount(1);
        mToastOutTransition.setDelay(Duration.millis(TOAST_DURATION_MILLI));

        //init ToastIn animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(TOAST_IN_DURATION_MILLI), mToastBox);
        fadeIn.setFromValue(0.0f);
        fadeIn.setToValue(1.0f);
        TranslateTransition translateIn =
                new TranslateTransition(Duration.millis(TOAST_IN_DURATION_MILLI), mToastBox);
        translateIn.setFromX(-mToastBox.getWidth());
        translateIn.setToX(mToastBox.getTranslateX() + SPACING);
        mToastInTransition = new ParallelTransition();
        mToastInTransition.getChildren().addAll(
                fadeIn//TODO ,translateIn
                );
        mToastInTransition.setCycleCount(1);
        mToastInTransition.setAutoReverse(true);
        mToastInTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mToastOutTransition.play();
            }
        });
    }
}

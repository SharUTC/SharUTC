package fr.utc.lo23.sharutc.ui.custom;


import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.ScrollEvent;

import java.util.Timer;
import java.util.TimerTask;

public class HorizontalScrollHandler implements EventHandler<Event> {

    /**
     * speed of the auto scroll
     */
    private static final double SPEED = 1.0 / 200;

    /**
     * area for scroll detection in percent
     */
    private static final double SCROLL_BOUNDARY_IN_PERCENT = 10;

    /**
     * refresh delay, also change the speed when onDragOver detected
     */
    private static final int TIMER_REFRESH_IN_MILLI = 20;

    /**
     * mouse scroll direction
     */
    private static final int DIRECTION = -1;

    private ScrollPane mScrollPane;
    private int mDirection;
    private Timer mTimer;

    public HorizontalScrollHandler(ScrollPane s) {
        super();
        mScrollPane = s;
        mScrollPane.setOnDragOver(this);
        mScrollPane.setOnDragExited(this);
        mScrollPane.setOnScroll(this);
        mDirection = 0;

    }

    @Override
    public void handle(Event event) {
        if (event.getEventType().equals(DragEvent.DRAG_OVER)) {

            final DragEvent dragEvent = (DragEvent) event;
            double mLeftBoundary = mScrollPane.getWidth() / SCROLL_BOUNDARY_IN_PERCENT;
            double mRightBoundry = mScrollPane.getWidth() - mLeftBoundary;
            final double eventX = dragEvent.getX();

            if (eventX > mRightBoundry) {
                mDirection = 1;
                startTimer();
            } else if (eventX < mLeftBoundary) {
                mDirection = -1;
                startTimer();
            } else {
                stopTimer();
            }
        } else if (event.getEventType().equals(ScrollEvent.SCROLL)) {
            final ScrollEvent scrollEvent = (ScrollEvent) event;
            mScrollPane.setHvalue(mScrollPane.getHvalue() + scrollEvent.getDeltaY() * DIRECTION * SPEED);
        } else if (event.getEventType().equals(DragEvent.DRAG_EXITED)) {
            stopTimer();
        }


    }

    /**
     * start the ticker if it's not running
     */
    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mScrollPane.setHvalue(mScrollPane.getHvalue() + SPEED * mDirection);
                }
            }, 0, TIMER_REFRESH_IN_MILLI);
        }
    }

    /**
     * stop the ticker if it's running
     */
    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

}

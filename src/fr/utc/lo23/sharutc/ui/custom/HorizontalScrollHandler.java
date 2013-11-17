package fr.utc.lo23.sharutc.ui.custom;


import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;

public class HorizontalScrollHandler implements EventHandler<Event> {

    /**
     * speed of the auto scroll
     */
    private static final double SPEED = 1.0 / 1000;

    /**
     * noise to prevent small X changes
     */
    private static final double NOISE = 10;

    /**
     * area for scroll detection in percent
     */
    private static final double SCROLL_BOUNDARY_IN_PERCENT = 10;

    private double mLastEventX;
    private ScrollPane mScrollPane;

    public HorizontalScrollHandler(ScrollPane s) {
        super();
        mScrollPane = s;
        mLastEventX = mScrollPane.getWidth() / 2;
    }

    @Override
    public void handle(Event event) {
        if (event.getEventType().equals(DragEvent.DRAG_OVER)) {

            final DragEvent dragEvent = (DragEvent) event;
            double mLeftBoundary = mScrollPane.getWidth() / SCROLL_BOUNDARY_IN_PERCENT;
            double mRightBoundry = mScrollPane.getWidth() - mLeftBoundary;
            final double eventX = dragEvent.getX();

            //TODO needs improvements
            if (eventX > mRightBoundry && eventX > mLastEventX - NOISE) {
                mScrollPane.setHvalue(mScrollPane.getHvalue() + Math.log(eventX - mRightBoundry) * SPEED);
            } else if (eventX < mLeftBoundary && eventX < mLastEventX + NOISE) {
                mScrollPane.setHvalue(mScrollPane.getHvalue() - Math.log(mLeftBoundary - eventX) * SPEED);
            }

            mLastEventX = eventX;
        }

    }
}

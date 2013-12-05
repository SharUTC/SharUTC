package fr.utc.lo23.sharutc.ui.custom;

import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.ScrollEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Catch scroll event on Slider and change the value by 1% each event
 */
public class SliderScrollHandler implements EventHandler<ScrollEvent> {

    private static final Logger log = LoggerFactory.getLogger(SliderScrollHandler.class);

    @Override
    public void handle(ScrollEvent scrollEvent) {
        if (scrollEvent.getSource() instanceof Slider) {
            final Slider s = (Slider) scrollEvent.getSource();
            s.setValue(s.getValue() + s.getMax() * Math.signum(scrollEvent.getDeltaY()) / 100);
        } else {
            log.error("Source must be an instance of javafx.scene.control.Slider;");
        }

    }
}

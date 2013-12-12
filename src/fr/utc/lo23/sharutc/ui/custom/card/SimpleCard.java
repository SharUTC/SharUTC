package fr.utc.lo23.sharutc.ui.custom.card;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * A simple custom VBox with a card style.
 * It can store a click state.
 */
public class SimpleCard extends VBox {
    protected final int STATE_NORMAL = 0;
    protected final int STATE_CLICKED = 1;

    public final static int WIDTH = 180;

    protected int mState;

    /**
     * Creates a SimpleCard from an FXML resource.
     *
     * @param resourceFXML the FXML resource that will be loaded.
     */
    public SimpleCard(String resourceFXML) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(resourceFXML));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            mState = STATE_NORMAL;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //set style
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPrefWidth(WIDTH);
        this.setPrefHeight(70);
        this.getStyleClass().addAll("simpleCard");

    }

    /**
     * Creates a SimpleCard with a given size and alignement.
     *
     * @param resourceFXML the FXML resource that will be loaded.
     * @param width        the pref width of the card.
     * @param height       the pref height of the card.
     * @param alignement   the alignement policy of the card.
     */
    public SimpleCard(String resourceFXML, double width, double height, Pos alignement) {
        this(resourceFXML);

        this.setPrefHeight(height);
        this.setPrefWidth(width);

        this.setAlignment(alignement);
    }


    /**
     * Adapts the style of the card according to the {@link MouseEvent}
     *
     * @param mouseEvent
     */
    public void adaptStyle(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (mState == STATE_CLICKED) {
                mState = STATE_NORMAL;
                getStyleClass().remove("simpleCardClicked");
            } else {
                mState = STATE_CLICKED;
                getStyleClass().add("simpleCardClicked");
            }
        }
    }

    /**
     * Allow to add or remove selection style
     *
     * @param selected true if selection must be displayed
     */
    public void setSelected(boolean selected) {
        if (selected) {
            getStyleClass().add("simpleCardClicked");
        } else {
            getStyleClass().remove("simpleCardClicked");
        }
    }

}

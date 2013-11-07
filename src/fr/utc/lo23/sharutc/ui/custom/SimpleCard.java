package fr.utc.lo23.sharutc.ui.custom;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

abstract public class SimpleCard extends VBox {
    protected final int STATE_NORMAL = 0;
    protected final int STATE_CLICKED = 1;

    protected int mState;

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
        this.setPrefWidth(180);
        this.setPrefHeight(70);
        this.getStyleClass().addAll("simpleCard");

    }


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

}

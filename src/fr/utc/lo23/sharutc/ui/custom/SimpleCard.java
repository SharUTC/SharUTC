package fr.utc.lo23.sharutc.ui.custom;

import java.io.IOException;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

abstract public class SimpleCard extends VBox {
    protected final int STATE_NORMAL = 0;
    protected final int STATE_CLICKED = 1;
    
    protected int mState;
    
    public SimpleCard() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("../fxml/simple_card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            mState = STATE_NORMAL;            
            
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    handleMouseClicked(mouseEvent);
                }                
            });
            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }        
        
    }    
    
    
    protected void handleMouseClicked(MouseEvent mouseEvent) {
        if(mState == STATE_CLICKED) {
            mState = STATE_NORMAL;
            getStyleClass().remove("simpleCardClicked");
        } else {
            mState = STATE_CLICKED;
            getStyleClass().add("simpleCardClicked");
        }
    }
    
}

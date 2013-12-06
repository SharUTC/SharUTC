
package fr.utc.lo23.sharutc.ui.custom.card;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;


public class TagDetailCard extends SimpleCard {
    
    @FXML
    public Label labelTagName;
    @FXML
    public Button buttonDeleteTag;
    
    private String mTagName;
    private Integer mTagWeight;
    
    public TagDetailCard(final String tagName, final ITagDetailCard callBack) {
        super("/fr/utc/lo23/sharutc/ui/fxml/tag_detail_card.fxml");
        mTagName = tagName;
        
        labelTagName.setText(mTagName);
        
        buttonDeleteTag.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                callBack.onTagDeleted(mTagName);
            }
        });
        
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                buttonDeleteTag.setVisible(true);
            }
        });
        
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                buttonDeleteTag.setVisible(false);
            }
        });
    }
    
    public interface ITagDetailCard {
        public void onTagDeleted(final String tag);
    }
}

package fr.utc.lo23.sharutc.ui.custom.card;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TagCard extends SimpleCard {
    
    @FXML
    public Label tagNameLabel;
    
    public TagCard(String tagName) {
        super("/fr/utc/lo23/sharutc/ui/fxml/tag_card.fxml");
        tagNameLabel.setText(tagName);
    }
}

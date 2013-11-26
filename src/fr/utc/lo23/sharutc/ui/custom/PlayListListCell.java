
package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.card.PlayListCard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;


public class PlayListListCell extends ListCell<Music> {

    
    
    @Override
    protected void updateItem(Music music, boolean bln) {
        super.updateItem(music, bln);
        if(music != null) {
            setGraphic(new PlayListCard(music));
        }        
    }
    
}

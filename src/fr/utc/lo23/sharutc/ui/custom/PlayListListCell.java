
package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.card.PlayListCard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;


public class PlayListListCell extends ListCell<PlayListMusic> {

    public PlayListListCell(){
        super();
        
    }
    
    @Override
    protected void updateItem(PlayListMusic musicl, boolean bln) {
        super.updateItem(musicl, bln);
        if(musicl != null) {
            setGraphic(new PlayListCard(musicl));
        }        
    }
    
}

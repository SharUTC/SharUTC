package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.ui.custom.card.PlayListCard;
import javafx.scene.control.ListCell;

public class PlayListListCell extends ListCell<PlayListMusic> {

    public PlayListListCell() {
        super();

    }

    @Override
    protected void updateItem(PlayListMusic musicl, boolean bln) {
        super.updateItem(musicl, bln);
        if (musicl != null) {
            setGraphic(new PlayListCard(musicl));
        }
    }
}

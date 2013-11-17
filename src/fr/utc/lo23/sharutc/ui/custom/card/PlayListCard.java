/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.ui.custom.card;

import javafx.geometry.Pos;

/**
 *
 * @author Vbarthel
 */
public class PlayListCard extends SimpleCard {

    public PlayListCard() {
        super("/fr/utc/lo23/sharutc/ui/fxml/play_list_card.fxml");
        getStyleClass().add("playListCard");
        setPrefWidth(150);
        setAlignment(Pos.TOP_LEFT);
    }

    public void setIsPlaying(boolean isPlaying) {
        if (isPlaying) {
            getStyleClass().add("playListCardPlaying");
        } else {
            getStyleClass().remove("playListCardPlaying");
        }
    }
}

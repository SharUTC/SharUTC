/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.ui.custom.card;

import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 *
 * @author Vbarthel
 */
public class PlayListCard extends SimpleCard {

    @FXML
    public Label musicTitle;
    public Label musicArtist;

    public PlayListCard(Music music) {
        super("/fr/utc/lo23/sharutc/ui/fxml/play_list_card.fxml");
        getStyleClass().add("playListCard");
        setPrefWidth(150);
        setAlignment(Pos.TOP_LEFT);

        musicArtist.setText(music.getAlbum());
        musicTitle.setText(music.getTitle());
    }

    public void setIsPlaying(boolean isPlaying) {
        if (isPlaying) {
            getStyleClass().add("playListCardPlaying");
        } else {
            getStyleClass().remove("playListCardPlaying");
        }
    }
}

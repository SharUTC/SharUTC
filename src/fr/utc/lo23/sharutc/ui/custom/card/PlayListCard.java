package fr.utc.lo23.sharutc.ui.custom.card;

import fr.utc.lo23.sharutc.ui.custom.PlayListMusic;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 * A {@link SimpleCard} that is used to show the detail of a piece of
 * {@link Music} within the playList.
 *
 * This class has to style : one when the {@link Music} associated is currently
 * being played and one when the {@link Music} is not.
 */
public class PlayListCard extends SimpleCard {

    @FXML
    public Label musicTitle;
    public Label musicArtist;

    public PlayListCard(PlayListMusic musicPList) {
        super("/fr/utc/lo23/sharutc/ui/fxml/play_list_card.fxml");
        getStyleClass().add("playListCard");
        setPrefWidth(150);
        setAlignment(Pos.TOP_LEFT);

        musicArtist.setText(musicPList.music.getAlbum());
        musicTitle.setText(musicPList.music.getTitle());

        setIsPlaying(musicPList.isPlaying());
    }

    /**
     * Set the style according to the playing state of the associated
     * {@link Music}.
     *
     * @param isPlaying a {@link Boolean} that represents the playing state of
     * the associated [@link Music}
     */
    private void setIsPlaying(boolean isPlaying) {
        if (isPlaying) {
            getStyleClass().add("playListCardPlaying");
        } else {
            getStyleClass().remove("playListCardPlaying");
        }
    }
}

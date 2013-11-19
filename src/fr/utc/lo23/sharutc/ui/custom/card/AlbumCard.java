package fr.utc.lo23.sharutc.ui.custom.card;

import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


public class AlbumCard extends SimpleCard implements EventHandler<Event>{
    
    private Music mMusic;
    private IAlbumCard mInterface;
    
    public Label albumName;
    public Label artistName;
    
    public AlbumCard(Music music, IAlbumCard i) {
        super("/fr/utc/lo23/sharutc/ui/fxml/album_card.fxml");
        setOnMouseClicked(this);
        mInterface = i;
        initModel(music);
    }
    
    private void initModel(Music music) {
        mMusic = music;
        albumName.setText(mMusic.getAlbum());
        artistName.setText(mMusic.getArtist());
    }

    public Music getModel() {
        return mMusic;
    }
    
    @Override
    public void handle(Event event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    mInterface.onAlbumDetailRequested(mMusic);
                }
            }
        }
    }
    
    public interface IAlbumCard {

        /**
         * user requested more details
         *
         * @param music
         */
        public void onAlbumDetailRequested(Music music);

    }
}

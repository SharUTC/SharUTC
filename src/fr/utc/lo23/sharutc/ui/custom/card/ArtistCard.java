package fr.utc.lo23.sharutc.ui.custom.card;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ArtistCard extends SimpleCard implements EventHandler<Event>{
    
    private String mArtistName;
    private IArtistCard mInterface;
    
    public Label artistNameLabel;
    
    public ArtistCard(String artistName, IArtistCard i) {
        super("/fr/utc/lo23/sharutc/ui/fxml/artist_card.fxml");
        setOnMouseClicked(this);
        mInterface = i;
        mArtistName = artistName;
        artistNameLabel.setText(artistName);
    }


    public String getArtistName() {
        return mArtistName;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    mInterface.onArtistDetailRequested(mArtistName);
                }
            }
        }
    }
    
    public interface IArtistCard {

        /**
         * user requested more details
         *
         * @param music
         */
        public void onArtistDetailRequested(String artistName);

    }
}

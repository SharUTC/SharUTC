package fr.utc.lo23.sharutc.ui.custom.card;

import fr.utc.lo23.sharutc.ui.SongDetailController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * A SimpleCard that displays the Artist of a given [@link Music].
 * The card notifies an {@link IArtistCard} on double click.
 */
public class ArtistCard extends SimpleCard implements EventHandler<Event> {

    @FXML
    public Label artistNameLabel;
    private String mArtistName;
    private IArtistCard mInterface;

    private SongDetailController.CatalogType type = SongDetailController.CatalogType.local;
    public ArtistCard(String artistName, IArtistCard i) {
        super("/fr/utc/lo23/sharutc/ui/fxml/artist_card.fxml");
        setOnMouseClicked(this);
        mInterface = i;
        mArtistName = artistName;
        artistNameLabel.setText(artistName);
    }
    
    public void setCatalogType(SongDetailController.CatalogType type){
        this.type= type;
    }
    

    /**
     * Return the name of the artist.
     *
     * @return the name of the artist.
     */
    public String getArtistName() {
        return mArtistName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(final Event event) {
        if (event instanceof MouseEvent) {
            final MouseEvent mouseEvent = (MouseEvent) event;
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && mouseEvent.getClickCount() == 2) {
                mInterface.onArtistDetailRequested(mArtistName, type);

            }
        }
    }

    /**
     * A simple interface used as callback.
     */
    public interface IArtistCard {

        /**
         * Notify the interface that the user wants to view the details of an
         * artist.
         *
         * @param artistName the name of the artist shown on the card.
         */
        public void onArtistDetailRequested(String artistName, SongDetailController.CatalogType type);
    }
}

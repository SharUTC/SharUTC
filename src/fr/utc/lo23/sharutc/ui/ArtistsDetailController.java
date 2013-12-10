package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.card.ArtistCard;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArtistsDetailController implements RighpaneInterface, Initializable, ArtistCard.IArtistCard {

    private static final Logger log = LoggerFactory.getLogger(ArtistsDetailController.class);
    public IArtistsDetailController mInterface; 
    @FXML
    public StackPane contentContainer;
    @FXML
    public FlowPane artistsContainer;
    @Inject
    private AppModel mAppModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showArtists();
    }

    public void setInterface(IArtistsDetailController i) {
        mInterface = i;
    }

    private void showArtists() {
        final ArrayList<String> artists = new ArrayList<String>();

        //retrieve all the artists
        for (Music m : mAppModel.getLocalCatalog().getMusics()) {
            final String currentArtist = m.getArtist();
            if (!artists.contains(currentArtist)) {
                artists.add(currentArtist);
            }
        }

        //display the artists
        if (artists.isEmpty()) {
            final Label placeHolder = new Label("You have no songs. Please add a song before browsing artists.");
            placeHolder.getStyleClass().add("placeHolderLabel");
            placeHolder.setWrapText(true);
            placeHolder.setTextAlignment(TextAlignment.CENTER);
            contentContainer.getChildren().add(placeHolder);
        } else {
            for (String artistName : artists) {
                artistsContainer.getChildren().add(new ArtistCard(artistName, this));
            }
        }

    }

    @Override
    public void onArtistDetailRequested(String artistName, SongDetailController.CatalogType type) {
        log.info("onArtistDetailRequested " + artistName);
        mInterface.onArtistDetailRequested(artistName, type);
    }

    @Override
    public void onDetach() {
    }

    public interface IArtistsDetailController {

        /**
         * display user details
         *
         * @param music
         */
        public void onArtistDetailRequested(String artistName, SongDetailController.CatalogType type);
    }
}

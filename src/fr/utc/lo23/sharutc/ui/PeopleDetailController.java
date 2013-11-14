package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PeopleDetailController extends DragPreviewDrawer implements Initializable, SongCard.ISongCard {

    public Label login;
    public Button addToFriendsButton;
    public FlowPane songsContainer;
    public FlowPane artistsContainer;
    public FlowPane tagsContainer;
    private UserInfo mUserInfo;
    /**
     * Song Card selected by the user
     */
    private ArrayList<SongCard> mSongCardSelected;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < 3; i++) {
            SongCard newCard = new SongCard(new Music(),this);
            songsContainer.getChildren().add(newCard);
        }
        for (int i = 0; i < 3; i++) {
            ArtistCard newCard = new ArtistCard();
            artistsContainer.getChildren().add(newCard);
        }
        for (int i = 0; i < 3; i++) {
            TagCard newCard = new TagCard();
            tagsContainer.getChildren().add(newCard);
        }

        mSongCardSelected = new ArrayList<SongCard>();

    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        login.setText(mUserInfo.getLogin());
    }

    public void handleAddToFriendsClicked(ActionEvent actionEvent) {

    }

    public void handleSeeMoreFriendsClicked(ActionEvent actionEvent) {

    }

    public void handleSeeMoreArtistsClicked(ActionEvent actionEvent) {

    }

    public void handleSeeMoreTagsClicked(ActionEvent actionEvent) {

    }

    /**
     * Display SongCard selected as Drag preview
     *
     * @param event
     */
    protected void updateSongCardDragPreview(MouseEvent event) {
        super.updateDragPreview(event);
        int i = 0;
        for (SongCard song : mSongCardSelected) {
            final ImageView preview = new ImageView(song.snapshot(null, null));
            StackPane.setMargin(preview, new Insets(20 * i, 20 * i, 0, 0));
            mDragPreview.getChildren().add(preview);
            i++;
        }
    }

    @Override
    public void onPlayRequested(Music music) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onAddToPlayListRequested(Music music) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onSongDetailsRequested(Music music) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onSongCardSelected(SongCard songCard) {
        if (mSongCardSelected.contains(songCard)) {
            mSongCardSelected.remove(songCard);
        } else {
            mSongCardSelected.add(songCard);
        }
    }

    @Override
    public void onDragStart(MouseEvent event, DraggableCard draggableCard) {
        if (draggableCard instanceof SongCard) {
            final SongCard draggedCard = (SongCard) draggableCard;
            //had to checked if this card is already selected because user
            //can drag a selected one or a new one
            mSongCardSelected.remove(draggedCard);
            mSongCardSelected.add(draggedCard);

            //drag event start, inform all selected card
            updateSongCardDragPreview(event);
            for (SongCard songCard : mSongCardSelected) {
                songCard.dragged();
            }
        }
    }

    @Override
    public void onDragStop(DraggableCard draggableCard) {
        if (draggableCard instanceof SongCard) {
            //drag event failed, inform all selected card
            for (SongCard songCard : mSongCardSelected) {
                songCard.dropped();
            }
            //clean the selection
            mSongCardSelected.clear();
            hideDragPreview();
        }
    }
}

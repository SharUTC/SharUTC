package fr.utc.lo23.sharutc.ui;


import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.card.DraggableCard;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class SongSelectorController extends DragPreviewDrawer implements SongCard.ISongCard {

    /**
     * Song Card selected by the user
     */
    protected ArrayList<SongCard> mSongCardSelected;

    public ArrayList<SongCard> getSelectedSong() {
        return mSongCardSelected;
    }

    @Override
    public void init(StackPane dragPreview) {
        super.init(dragPreview);
        mSongCardSelected = new ArrayList<SongCard>();
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

    @Override
    public void onPlayRequested(Music music) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onSongRemoveFromMusicRequested(Music music) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onSongDetailsRequested(Music music) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onSongAddToMusicRequested(Music music) {
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


}

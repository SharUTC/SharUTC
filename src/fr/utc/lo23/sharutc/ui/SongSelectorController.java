package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.card.DraggableCard;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SongSelectorController extends DragPreviewDrawer implements SongCard.ISongCard {

    private static final Logger log = LoggerFactory
            .getLogger(SongSelectorController.class);
    
    private ISongListController mInterface;
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
    
    public void setInterface(ISongListController i) {
        mInterface = i;
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
    public void onTagEditionRequested(Music music) {
        log.info("onTagEditionRequested: " + music.getTitle());
    }

    @Override
    public void onPlayRequested(Music music) {
        log.info("onPlayRequested: " + music.getTitle());
    }

    @Override
    public void onSongDetailsRequested(Music music) {
        log.info("onSongDetailsRequested: " + music.getTitle());
        if(mInterface != null) {
            mInterface.onSongDetailRequested(music);
        }        
    }

    @Override
    public void onSongAddToPlayList(Music music) {
        log.info("onSongAddToPlayList: " + music.getTitle());
    }

    @Override
    public void onSongCardSelected(SongCard songCard) {
        log.info("onSongCardSelected: " + songCard.getModel().getTitle());
        if (mSongCardSelected.contains(songCard)) {
            mSongCardSelected.remove(songCard);
        } else {
            mSongCardSelected.add(songCard);
        }
    }

    @Override
    public void onDetach() {
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

    public interface ISongListController {
        
        void onSongDetailRequested(Music music);
    }
}

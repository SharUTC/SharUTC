package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.player.AddToPlaylistCommand;
import fr.utc.lo23.sharutc.controler.command.player.PlayMusicCommand;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.card.DraggableCard;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SongSelectorController extends DragPreviewDrawer implements SongCard.ISongCard {

    private static final Logger log = LoggerFactory
            .getLogger(SongSelectorController.class);
    private ISongListController mInterface;
    /**
     * Song Card selected by the user
     */
    protected ArrayList<SongCard> mSongCardSelected;
    @Inject
    private PlayMusicCommand mPlayMusicCommand;
    @Inject
    private AddToPlaylistCommand mAddToPlaylistCommand;

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
            updateDragPreview(event, mSongCardSelected);
        }
    }

    @Override
    public void onDragStop(DraggableCard draggableCard) {
        if (draggableCard instanceof SongCard) {
            //drag event failed, inform all selected card
            hideDragPreview(mSongCardSelected);
            //clean the selection
            mSongCardSelected.clear();

        }
    }

    @Override
    public void onTagEditionRequested(Music music) {
        log.info("onTagEditionRequested: " + music.getTitle());
        if (mInterface != null) {
            mInterface.onTagDetailRequested(music);
        }
    }

    @Override
    public void onPlayRequested(Music music) {
        log.info("onPlayRequested: " + music.getTitle());
        if (mInterface != null) {
            mInterface.onSongPlayRequest(music);
        }
        mPlayMusicCommand.setMusic(music);
        mPlayMusicCommand.execute();

    }

    @Override
    public void onSongDetailsRequested(Music music) {
        log.info("onSongDetailsRequested: " + music.getTitle());
        if (mInterface != null) {
            mInterface.onSongDetailRequested(music);
        }
    }

    @Override
    public void onSongAddToPlayList(final Music music) {
        final Runnable addToPlaylistRunnable = new Runnable() {
            @Override
            public void run() {
                log.info("onSongAddToPlayList: " + music.getTitle());
                List<Music> musics = new ArrayList<Music>();
                musics.add(music);
                mAddToPlaylistCommand.setMusics(musics);
                mAddToPlaylistCommand.execute();
            }
        };
        new Thread(addToPlaylistRunnable, "Add to playlist").start();
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

    public interface ISongListController {

        void onSongDetailRequested(Music music);

        void onSongPlayRequest(Music music);

        void onTagDetailRequested(Music music);
    }
}

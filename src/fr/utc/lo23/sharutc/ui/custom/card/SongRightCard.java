package fr.utc.lo23.sharutc.ui.custom.card;

import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class SongRightCard extends DraggableCard implements EventHandler<Event> {

    /**
     * key used for the drag event to identify the content
     */
    public static final String DROP_KEY = SongRightCard.class + "DropKey";

    private Music mModel;
    private boolean mIsOwned;
    private ISongCardRight mInterface;
    @FXML
    public Label songTitle;
    @FXML
    public Label songArtist;

    @FXML
    public CheckBox checkBoxRead;
    @FXML
    public CheckBox checkBoxEdit;
    @FXML
    public CheckBox checkBoxComment;


    public SongRightCard(Music m, ISongCardRight i, boolean isOwned, boolean mayListen, boolean mayReadInfo, boolean mayComment) {
        super("/fr/utc/lo23/sharutc/ui/fxml/song_card_right.fxml", DROP_KEY, i);
        mModel = m;
        mInterface = i;
        songTitle.setText(mModel.getTitle());
        songArtist.setText(mModel.getArtist());
        mIsOwned = isOwned;
        checkBoxEdit.setSelected(mayListen);
        checkBoxRead.setSelected(mayReadInfo);
        checkBoxComment.setSelected(mayComment);

        setOnMouseClicked(this);
    }

    @FXML
    private void handleSongCardRightCheckBoxAction(ActionEvent event) throws IOException {
        final Object source = event.getSource();
        if (source.equals(checkBoxEdit)) {
            if (checkBoxEdit.isSelected())
                mInterface.handleCheckBoxListenClicked(mModel, true);
            else
                mInterface.handleCheckBoxListenClicked(mModel, false);
        } else if (source.equals(checkBoxRead)) {
            if (checkBoxRead.isSelected())
                mInterface.handleCheckBoxReadClicked(mModel, true);
            else
                mInterface.handleCheckBoxReadClicked(mModel, false);
        } else if (source.equals(checkBoxComment)) {
            if (checkBoxComment.isSelected())
                mInterface.handleCheckBoxCommentClicked(mModel, true);
            else
                mInterface.handleCheckBoxCommentClicked(mModel, false);
        }


    }

    public Music getModel() {
        return mModel;
    }


    @Override
    public void handle(Event event) {
        final Object source = event.getSource();
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (source.equals(this)) {
                this.adaptStyle((MouseEvent) event);
                mInterface.onSongRightCardSelected(this);
            }
        }
    }

    /**
     * interface to get callback from SongRightCard
     */
    public interface ISongCardRight extends IDraggableCardListener {

        public void handleCheckBoxListenClicked(Music m, boolean boxstate);

        public void handleCheckBoxReadClicked(Music m, boolean boxstate);

        public void handleCheckBoxCommentClicked(Music m, boolean boxstate);

        /**
         * card has been selected
         *
         * @param songCardRight
         */
        public void onSongRightCardSelected(SongRightCard songCardRight);
    }

}

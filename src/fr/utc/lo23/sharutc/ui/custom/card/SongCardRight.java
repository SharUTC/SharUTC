package fr.utc.lo23.sharutc.ui.custom.card;
import fr.utc.lo23.sharutc.model.domain.Music;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.io.IOException;
import javafx.scene.control.CheckBox;

public class SongCardRight extends SimpleCard implements EventHandler<Event> {

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
    

    public SongCardRight(Music m, ISongCardRight i, boolean isOwned , boolean mayListen, boolean mayReadInfo , boolean mayComment) {
        super("/fr/utc/lo23/sharutc/ui/fxml/song_card_right.fxml");
        mModel = m;
        mInterface = i;
        songTitle.setText(mModel.getTitle());
        songArtist.setText(mModel.getArtist());
        mIsOwned = isOwned;
        checkBoxEdit.setSelected(mayListen);
        checkBoxRead.setSelected(mayReadInfo);
        checkBoxComment.setSelected(mayComment);
        
    }
     @FXML
    private void handleSongCardRightCheckBoxAction(ActionEvent event) throws IOException {
        final Object source = event.getSource();
        if (source.equals(checkBoxEdit)) {
            if (checkBoxEdit.isSelected())
                 mInterface.handleCheckBoxListenClicked(mModel,true);
            else 
                mInterface.handleCheckBoxListenClicked(mModel,false);    
        } else if (source.equals(checkBoxRead)) {
            if (checkBoxRead.isSelected())
                mInterface.handleCheckBoxReadClicked(mModel,true);
            else 
                mInterface.handleCheckBoxReadClicked(mModel,false);   
        } else if (source.equals(checkBoxComment)) {
            if (checkBoxComment.isSelected())
                mInterface.handleCheckBoxCommentClicked(mModel,true);
             else
                mInterface.handleCheckBoxCommentClicked(mModel,false); 
        }  
            

    }

    public Music getModel() {
        return mModel;
    }

    public void onHover(boolean isHover) {
        
    }


    @Override
    public void handle(Event t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
     public interface ISongCardRight  {
         
        public void handleCheckBoxListenClicked(Music m,boolean boxstate);
        public void handleCheckBoxReadClicked(Music m ,boolean boxstate);
        public void handleCheckBoxCommentClicked(Music m ,boolean boxstate);
    }

}

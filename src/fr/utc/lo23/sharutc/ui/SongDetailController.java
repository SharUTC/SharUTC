package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Comment;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.ui.custom.CommentView;
import fr.utc.lo23.sharutc.ui.custom.card.DraggableCard;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SongDetailController implements Initializable, SongCard.ISongCard {

    @FXML
    public VBox topLeftContainer;
    @FXML
    public VBox commentContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        final Music music = new Music();
        music.setAlbum("Natural Mystic");
        music.setTitle("Crazy Baldhead");
        music.setArtist("Bob Marley");

        final SongCard songCard = new SongCard(music, this, false);
        songCard.setPrefWidth(230);
        topLeftContainer.getChildren().add(songCard);
        
        //artificialy populate with some comments
        populateCommentContainer();
    }

    private void populateCommentContainer() {
        final Comment comment1 = new Comment();
        comment1.setAuthorName("Jake");
        comment1.setIndex(1);
        comment1.setText("This song saved my life");

        final Comment comment2 = new Comment();
        comment2.setAuthorName("Amelia");
        comment2.setIndex(2);
        comment2.setText("Sick !");


        final Comment comment3 = new Comment();
        comment3.setAuthorName("PainInTheNeck");
        comment3.setIndex(3);
        comment3.setText("I'm not realy a fan of the genre. Is that event music ?!?");

        commentContainer.getChildren().add(new CommentView(comment1));
        commentContainer.getChildren().add(new CommentView(comment2));
        commentContainer.getChildren().add(new CommentView(comment3));
        commentContainer.getChildren().add(new CommentView(comment1));

    }

    @Override
    public void onPlayRequested(Music music) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSongDetailsRequested(Music music) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSongCardSelected(SongCard songCard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSongAddToPlayList(Music music) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onTagEditionRequested(Music music) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDragStart(MouseEvent event, DraggableCard draggableCard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDragStop(DraggableCard draggableCard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.domain.Comment;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CommentView extends VBox {

    @FXML
    public Label commentTitle;
    @FXML
    public Label commentContent;
    
    final private Comment mComment;
    
    public CommentView(Comment comment) {
        
        mComment = comment;
        
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/comment_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            commentTitle.setText(mComment.getAuthorName() + " n°" + String.valueOf(mComment.getIndex()));
            commentContent.setText(mComment.getText());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }
}

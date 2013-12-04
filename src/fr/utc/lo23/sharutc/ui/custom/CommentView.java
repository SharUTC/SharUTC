package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.model.domain.Comment;
import fr.utc.lo23.sharutc.util.DialogBoxBuilder;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class CommentView extends VBox {

    @FXML
    public Label commentTitle;
    @FXML
    public Label commentContent;
    @FXML
    public Button buttonEditComment;
    @FXML
    public Button buttonDeleteComment;
    @FXML
    public Button buttonAcceptComment;
    @FXML
    public Button buttonCancelComment;
    final private Comment mComment;

    public CommentView(final Comment comment) {

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

    public CommentView(final Comment comment, final IComment iComment) {
        this(comment);
        final TextArea textAreaEdition = new TextArea();

        buttonDeleteComment.setVisible(true);
        buttonDeleteComment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                askDeletionConfirmation(iComment);
            }
        });

        buttonEditComment.setVisible(true);
        buttonEditComment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                startEdition(textAreaEdition);
            }
        });

        buttonAcceptComment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                confirmEdition(iComment, textAreaEdition);
            }
        });

        buttonCancelComment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                cancelEdition(textAreaEdition);
            }
        });
    }

    private void confirmEdition(final IComment iComment, final TextArea textAreaEdition) {
        final String newCommentText = textAreaEdition.getText();
        iComment.onCommentEditionRequest(mComment, newCommentText);
    }

    private void cancelEdition(final TextArea textAreaEdition) {
        buttonAcceptComment.setVisible(false);
        buttonCancelComment.setVisible(false);
        buttonEditComment.setVisible(true);
        CommentView.this.getChildren().remove(textAreaEdition);
        CommentView.this.getChildren().add(commentContent);
    }

    private void startEdition(final TextArea textAreaEdition) {
        buttonEditComment.setVisible(false);
        buttonCancelComment.setVisible(true);
        buttonAcceptComment.setVisible(true);
        CommentView.this.getChildren().remove(commentContent);
        textAreaEdition.setText(mComment.getText());
        textAreaEdition.prefRowCountProperty().set(4);
        textAreaEdition.getStyleClass().add("commentTextArea");
        CommentView.this.getChildren().add(textAreaEdition);
    }

    private void askDeletionConfirmation(final IComment iComment) {
        DialogBoxBuilder.createConfirmBox("Do you really want to delete the comment ?",
                this.getClass().getResource("/fr/utc/lo23/sharutc/ui/css/modal.css").toExternalForm(),
                CommentView.this.getScene().getRoot(),
                new DialogBoxBuilder.IConfirmBox() {
            @Override
            public void onChoiceMade(boolean answer) {
                if (answer) {
                    iComment.onCommentDeletionRequest(mComment);
                }
            }
        }).show();
    }

    public interface IComment {

        public void onCommentEditionRequest(final Comment comment, final String newCommentText);

        public void onCommentDeletionRequest(final Comment comment);
    }
}

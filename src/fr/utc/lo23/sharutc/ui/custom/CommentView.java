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

/**
 * A simple {@link VBox} that shows the author and the content of a comment.
 *
 * This class can notify a {@link IComment} when the user interacts with the
 * view.
 */
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

    /**
     * Create a {@link CommentView} with no control whatsoever.
     *
     * This constructor is usually used for comments that the user didn't make.
     *
     * @param comment the {@link Comment} to be shown.
     */
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

    /**
     * Create a {@link CommentView} with an "edit" and "delete" buttons.
     *
     * This constructor is usually used for comments that the user made.
     *
     * @param comment the {@link Comment} to be shown.
     * @param iComment the {@link IComment} to be notified.
     */
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

    /**
     * Confirm the edition of a comment by asking the {@link IComment} to
     * actually edit the comment.
     *
     * @param iComment the {@link IComment} to be notified.
     * @param textAreaEdition the {@link TextArea} that holds the new comment.
     */
    private void confirmEdition(final IComment iComment, final TextArea textAreaEdition) {
        final String newCommentText = textAreaEdition.getText();
        iComment.onCommentEditionRequest(mComment, newCommentText);
    }

    /**
     * Cancel the edition of a comment.
     *
     * @param textAreaEdition the {@link  TextArea} that was holding the new
     * comment.
     */
    private void cancelEdition(final TextArea textAreaEdition) {
        buttonAcceptComment.setVisible(false);
        buttonCancelComment.setVisible(false);
        buttonEditComment.setVisible(true);
        CommentView.this.getChildren().remove(textAreaEdition);
        CommentView.this.getChildren().add(commentContent);
    }

    /**
     * Start the edition of the comment.
     *
     * @param textAreaEdition the {@link TextArea} that will hold the new
     * comment.
     */
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

    /**
     * Prompt a confirmation box to ask the user if he really wants to delete
     * the comment.
     *
     * @param iComment the {@link IComment} to be notified if the user confirms
     * the deletion.
     */
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

    /**
     * A Simple interface used by a {@link CommentView}.
     */
    public interface IComment {

        /**
         * The {@link IComment} is being asked to edit a {@link Comment}
         *
         * @param comment the {@link Comment} to be edited.
         * @param newCommentText the new content of the comment.
         */
        public void onCommentEditionRequest(final Comment comment, final String newCommentText);

        /**
         * The {@link IComment} is being asked to delete a {@link Comment}.
         *
         * @param comment the {@link Comment} to be deleted.
         */
        public void onCommentDeletionRequest(final Comment comment);
    }
}

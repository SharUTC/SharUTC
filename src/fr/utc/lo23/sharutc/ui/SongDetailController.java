package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.music.AddCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.EditCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveFromLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.SetScoreCommand;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Comment;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.Score;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.ui.custom.CommentView;
import fr.utc.lo23.sharutc.ui.custom.CommentView.IComment;
import fr.utc.lo23.sharutc.ui.custom.RatingStar;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import fr.utc.lo23.sharutc.util.CollectionEvent.Type;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SongDetailController extends SongSelectorController implements Initializable, PropertyChangeListener, CollectionChangeListener<Music>, IComment {

    private static final Logger log = LoggerFactory.getLogger(SongDetailController.class);
    @FXML
    public TextArea commentTextArea;
    @FXML
    public Button addRemoveButton;
    @FXML
    public VBox topLeftContainer;
    @FXML
    public VBox commentContainer;
    @FXML
    public RatingStar starMyRate1;
    @FXML
    public RatingStar starMyRate2;
    @FXML
    public RatingStar starMyRate3;
    @FXML
    public RatingStar starMyRate4;
    @FXML
    public RatingStar starMyRate5;
    @FXML
    public RatingStar starAverageRate1;
    @FXML
    public RatingStar starAverageRate2;
    @FXML
    public RatingStar starAverageRate3;
    @FXML
    public RatingStar starAverageRate4;
    @FXML
    public RatingStar starAverageRate5;
    @Inject
    private AppModel mAppModel;
    @Inject
    private SetScoreCommand mSetScoreCommand;
    @Inject
    private RemoveFromLocalCatalogCommand mRemoveFromLocalCatalogCommand;
    @Inject
    AddCommentCommand mAddCommentCommand;
    @Inject
    private RemoveCommentCommand mRemoveCommentCommand;
    @Inject
    private EditCommentCommand mEditCommentCommand;
    private RatingStar[] mMyRatingStars;
    private RatingStar[] mAverageRatingStars;
    private Music mMusic;
    private Score mUserScore;
    private ISongDetailController mInteface;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mMyRatingStars = new RatingStar[]{
            starMyRate1,
            starMyRate2,
            starMyRate3,
            starMyRate4,
            starMyRate5
        };

        mAverageRatingStars = new RatingStar[]{
            starAverageRate1,
            starAverageRate2,
            starAverageRate3,
            starAverageRate4,
            starAverageRate5
        };

        mAppModel.getLocalCatalog().addPropertyChangeListener(this);
    }

    public void setInterface(ISongDetailController i) {
        super.setInterface(i);
        mInteface = i;
    }

    public void setMusic(final Music music) {
        mMusic = music;
        setUserScore();
        showMusicInfo();
        showMyRating();
        showAverageRating();
        showComments();
    }

    private void setUserScore() {
        mUserScore = mMusic.getScore(mAppModel.getProfile().getUserInfo().getPeerId());
        if (mUserScore != null) {
            mUserScore.addPropertyChangeListener(this);
        }
    }

    private void showComments() {
        final List<Comment> comments = mMusic.getComments();
        for (Comment comment : comments) {
            if (comment.getAuthorPeerId().equals(mAppModel.getProfile().getUserInfo().getPeerId())) {
                commentContainer.getChildren().add(new CommentView(comment, this));
            } else {
                commentContainer.getChildren().add(new CommentView(comment));
            }

        }
        //artificialy populate with some comments
        populateCommentContainer();
    }

    private void showMusicInfo() {
        final SongCard songCard = new SongCard(mMusic, this, false);
        songCard.setPrefWidth(230);
        topLeftContainer.getChildren().add(songCard);

        if (mAppModel.getLocalCatalog().contains(mMusic)) {
            addRemoveButton.setText("Remove");
            addRemoveButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    log.debug("remove button clicked");
                    mRemoveFromLocalCatalogCommand.setMusics(Arrays.asList(mMusic));
                    mRemoveFromLocalCatalogCommand.execute();
                }
            });
        }
    }

    private void showAverageRating() {
        final Set<Score> scores = mMusic.getScores();
        if (scores != null && scores.size() > 0) {
            int averageScore = 0;
            for (Score score : scores) {
                averageScore += score.getValue();
            }
            fillRatingStar(averageScore / scores.size(), mAverageRatingStars);
        }
    }

    private void showMyRating() {
        int currentScoreValue = 0;
        if (mUserScore != null) {
            currentScoreValue = mUserScore.getValue();
        }
        fillRatingStar(currentScoreValue, mMyRatingStars);
    }

    private void fillRatingStar(final int rate, final RatingStar[] ratingStars) {
        for (int i = 0; i < 5; i++) {
            if (i < rate) {
                ratingStars[i].fill(true);
            } else {
                ratingStars[i].fill(false);
            }
        }
    }

    @FXML
    private void handleAddCommentAction(ActionEvent event) {
        final String comment = commentTextArea.getText().trim();
        if (!comment.isEmpty()) {
            log.debug("addCommentCommand : " + comment);
            mAddCommentCommand.setMusic(mMusic);
            mAddCommentCommand.setComment(comment);
            final Peer myPeer = mAppModel.getProfile().getUserInfo().toPeer();
            mAddCommentCommand.setAuthorPeer(myPeer);

            Peer ownerPeer = myPeer;
            if (!mMusic.getOwnerPeerId().equals(myPeer.getId())) {
                log.debug("not my music !");
                ownerPeer = mAppModel.getActivePeerList().getPeerByPeerId(mMusic.getOwnerPeerId());
            }

            if (ownerPeer != null) {
                mAppModel.getActivePeerList().getPeerByPeerId(Long.MIN_VALUE);
                mAddCommentCommand.setOwnerPeer(ownerPeer);
                //The command doesn't work as intended because of a bug with the
                //Comment Indexes -> Demande #135
                mAddCommentCommand.execute();
                commentTextArea.clear();
                //Update UI directly, since no events are triggered when a comment is added.
                commentContainer.getChildren().clear();
                showComments();
            } else {
                commentTextArea.clear();
                commentTextArea.setText("user not connected anymore");
            }

            log.debug("addCommentCommand -- end ");
        }
    }

    public void handleMouseEnteredRatingStar(MouseEvent mouseEvent) {
        final Object source = mouseEvent.getSource();
        int previewRate = 1;
        if (source == starMyRate2) {
            previewRate = 2;
        } else if (source == starMyRate3) {
            previewRate = 3;
        } else if (source == starMyRate4) {
            previewRate = 4;
        } else if (source == starMyRate5) {
            previewRate = 5;
        }
        fillRatingStar(previewRate, mMyRatingStars);
    }

    public void handleMouseExitedRatingStar(MouseEvent mouseEvent) {
        showMyRating();
    }

    public void handleMouseClickedRatingStar(MouseEvent mouseEvent) {
        int currrentScoreValue = 0;
        if (mUserScore != null) {
            currrentScoreValue = mMusic.getScore(mAppModel.getProfile().getUserInfo().getPeerId()).getValue();
        }

        final Object source = mouseEvent.getSource();
        int newCandidateRate = 0;
        if (source == starMyRate5) {
            newCandidateRate = 5;
        } else if (source == starMyRate4) {
            newCandidateRate = 4;
        } else if (source == starMyRate3) {
            newCandidateRate = 3;
        } else if (source == starMyRate2) {
            newCandidateRate = 2;
        } else if (source == starMyRate1) {
            if (currrentScoreValue != 1) {
                newCandidateRate = 1;
            }
        }

        if (newCandidateRate != currrentScoreValue) {
            log.debug("new rate : " + String.valueOf(newCandidateRate));
            mSetScoreCommand.setMusic(mMusic);
            mSetScoreCommand.setScore(newCandidateRate);
            mSetScoreCommand.setPeer(mAppModel.getProfile().getUserInfo().toPeer());
            mSetScoreCommand.execute();

            //Update UI directly, since no events are triggered when the very first score is set.
            if (mUserScore == null) {
                log.debug("score -- work-around");
                setUserScore();
                showMyRating();
                showAverageRating();
            }
        }
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mUserScore != null) {
            mUserScore.removePropertyChangeListener(this);
        }
        mAppModel.getLocalCatalog().removePropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String propertyName = evt.getPropertyName();
        if (Score.Property.VALUE.name().equals(propertyName)) {
            log.debug("score updated");
            showMyRating();
            showAverageRating();
        }
    }

    @Override
    public void collectionChanged(CollectionEvent<Music> ev) {
        final Type type = ev.getType();
        if (CollectionEvent.Type.REMOVE.equals(type)) {
            log.info("remove music from local catalog");
            mInteface.onSongRemovedFromLocalCatalog();
        }
    }

    @Override
    public void onCommentEditionRequest(final Comment comment, final String newCommentText) {
        log.debug("comment edition requested !");
        mEditCommentCommand.setMusic(mMusic);
        mEditCommentCommand.setAuthorPeer(mAppModel.getProfile().getUserInfo().toPeer());
        mEditCommentCommand.setOwnerPeer(mAppModel.getProfile().getUserInfo().toPeer());
        mEditCommentCommand.setCommentId(comment.getIndex());
        mEditCommentCommand.setComment(newCommentText);
        //The command doesn't work as intended because of a bug with the
        //Comment Indexes -> Demande #135
        mEditCommentCommand.execute();
        //Update UI directly, since no events are triggered when a comment is deleted
        commentContainer.getChildren().clear();
        showComments();
    }

    @Override
    public void onCommentDeletionRequest(final Comment comment) {
        log.debug("comment deletion requested !");
        mRemoveCommentCommand.setCommentId(comment.getIndex());
        mRemoveCommentCommand.setMusic(mMusic);
        mRemoveCommentCommand.setPeer(mAppModel.getProfile().getUserInfo().toPeer());
        //The command doesn't work as intended because of a bug with the
        //Comment Indexes -> Demande #135
        mRemoveCommentCommand.execute();
        //Update UI directly, since no events are triggered when a comment is deleted
        commentContainer.getChildren().clear();
        showComments();
    }

    public interface ISongDetailController extends ISongListController {

        public void onSongRemovedFromLocalCatalog();
    }
}

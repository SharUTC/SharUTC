package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.music.AddCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.AddTagCommand;
import fr.utc.lo23.sharutc.controler.command.music.EditCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveCommentCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveFromLocalCatalogCommand;
import fr.utc.lo23.sharutc.controler.command.music.RemoveTagCommand;
import fr.utc.lo23.sharutc.controler.command.music.SetScoreCommand;
import fr.utc.lo23.sharutc.controler.command.search.DownloadMusicsCommand;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.Comment;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.Score;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.ui.custom.CommentView;
import fr.utc.lo23.sharutc.ui.custom.CommentView.IComment;
import fr.utc.lo23.sharutc.ui.custom.RatingStar;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import fr.utc.lo23.sharutc.ui.custom.card.TagDetailCard;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import fr.utc.lo23.sharutc.util.CollectionEvent.Type;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SongDetailController extends SongSelectorController implements Initializable,
        PropertyChangeListener, CollectionChangeListener<Music>, IComment,
        TagDetailCard.ITagDetailCard {

    private static final Logger log = LoggerFactory.getLogger(SongDetailController.class);
    @FXML
    public ProgressIndicator progressIndicatorAddRemove;
    @FXML
    public HBox inputContainer;
    @FXML
    public Button addRemoveButton;
    @FXML
    public VBox topLeftContainer;
    @FXML
    public Button addInputButton;
    @FXML
    public Label centralSectionTitle;
    @FXML
    public ScrollPane centralScrollPane;
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
    public Label ownerLogin;
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
    @Inject
    private AddTagCommand mAddTagCommand;
    @Inject
    private RemoveTagCommand mRemoveTagCommand;
    @Inject
    private DownloadMusicsCommand mDownloadMusicsCommand;
    private RatingStar[] mMyRatingStars;
    private RatingStar[] mAverageRatingStars;
    private Music mMusic;
    private Score mUserScore;
    private ISongDetailController mInteface;
    private VBox mCommentContainer;
    private FlowPane mTagContainer;
    private TextArea mCommentInputTextArea;
    private TextField mTagInputTextArea;

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

        ownerLogin.setText(mAppModel.getProfile().getUserInfo().getLogin());

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
    }

    private void setUserScore() {
        mUserScore = mMusic.getScore(mAppModel.getProfile().getUserInfo().getPeerId());
        if (mUserScore != null) {
            mUserScore.addPropertyChangeListener(this);
        } else if (mMusic != null) {
            mMusic.addPropertyChangeListener(this);
        }
    }

    public void showTags() {
        mTagContainer = new FlowPane(Orientation.HORIZONTAL);
        mTagContainer.setHgap(7);
        mTagContainer.setVgap(7);
        centralScrollPane.setContent(mTagContainer);
        centralSectionTitle.setText("Tags");
        mTagInputTextArea = new TextField();
        mTagInputTextArea.setPromptText("Type a new tag...");
        mTagInputTextArea.getStyleClass().add("commentTextArea");
        mTagInputTextArea.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                handleAddTagAction(t);
            }
        });
        HBox.setHgrow(mTagInputTextArea, Priority.ALWAYS);
        inputContainer.getChildren().add(mTagInputTextArea);
        addInputButton.setText("Add a Tag");
        addInputButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                handleAddTagAction(t);
            }
        });
        loadTags();
    }

    private void loadTags() {
        final Set<String> tags = mMusic.getTags();
        for (final String tag : tags) {
            final TagDetailCard tagCard = new TagDetailCard(tag, this);
            tagCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    mInteface.onTagFilterRequested(tag);
                }
            });
            mTagContainer.getChildren().add(tagCard);
        }
    }

    public void showComments() {
        mCommentContainer = new VBox();
        centralScrollPane.setContent(mCommentContainer);
        centralSectionTitle.setText("Comments");
        mCommentInputTextArea = new TextArea();
        mCommentInputTextArea.getStyleClass().add("commentTextArea");
        mCommentInputTextArea.setPrefRowCount(3);
        mCommentInputTextArea.setPromptText("Type your comment...");
        HBox.setHgrow(mCommentInputTextArea, Priority.ALWAYS);
        inputContainer.getChildren().add(mCommentInputTextArea);
        addInputButton.setText("Comment");
        addInputButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                handleAddCommentAction(t);
            }
        });
        loadComments();
    }

    private void loadComments() {
        final List<Comment> comments = mMusic.getComments();
        for (Comment comment : comments) {
            if (comment.getAuthorPeerId().equals(mAppModel.getProfile().getUserInfo().getPeerId())) {
                mCommentContainer.getChildren().add(new CommentView(comment, this));
            } else {
                mCommentContainer.getChildren().add(new CommentView(comment));
            }

        }
        //artificialy populate with some comments
        populateCommentContainer();
    }

    private void showMusicInfo() {
        topLeftContainer.getChildren().clear();
        final SongCard songCard = new SongCard(mMusic, this, mAppModel);
        songCard.setPrefWidth(230);
        topLeftContainer.getChildren().add(songCard);
        progressIndicatorAddRemove.setVisible(false);
        addRemoveButton.setVisible(true);

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
        } else {
            addRemoveButton.setText("Add");
            addRemoveButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    log.debug("add button clicked");
                    log.debug("music file name " + mMusic.getFileName());
                    final File file = new File(mMusic.getFileName());
                    final Catalog catalog = new Catalog();
                    catalog.add(mMusic);
                    progressIndicatorAddRemove.setVisible(true);
                    addRemoveButton.setVisible(false);
                    mDownloadMusicsCommand.setCatalog(catalog);
                    mDownloadMusicsCommand.execute();
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

    private void handleAddTagAction(ActionEvent event) {
        final String tag = mTagInputTextArea.getText().trim();
        if (!tag.isEmpty()
                && !SongListController.VIRTUAL_TAG_ALL_SONGS.equals(tag)
                && !SongListController.VIRTUAL_TAG_MY_SONGS.equals(tag)) {
            log.debug("addTagCommand" + tag);
            mAddTagCommand.setMusic(mMusic);
            mAddTagCommand.setTag(tag);
            mAddTagCommand.execute();
            mTagInputTextArea.clear();
            mTagContainer.getChildren().clear();
            loadTags();
        }
    }

    private void handleAddCommentAction(ActionEvent event) {
        final String comment = mCommentInputTextArea.getText().trim();
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
                mAddCommentCommand.execute();
                mCommentInputTextArea.clear();
                //Update UI directly, since no events are triggered when a comment is added.
                mCommentContainer.getChildren().clear();
                loadComments();
            } else {
                mCommentInputTextArea.clear();
                mCommentInputTextArea.setText("user not connected anymore");
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

        mCommentContainer.getChildren().add(new CommentView(comment1));
        mCommentContainer.getChildren().add(new CommentView(comment2));
        mCommentContainer.getChildren().add(new CommentView(comment3));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mUserScore != null) {
            mUserScore.removePropertyChangeListener(this);
        }
        if (mMusic != null) {
            mMusic.removePropertyChangeListener(this);
        }
        mAppModel.getLocalCatalog().removePropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String propertyName = evt.getPropertyName();
        if (Score.Property.VALUE.name().equals(propertyName)
                || Music.Property.SCORES.name().equals(propertyName)) {
            log.debug("score updated");
            if (mUserScore == null) {
                mMusic.removePropertyChangeListener(this);
                setUserScore();
            }
            showMyRating();
            showAverageRating();
        }
    }

    @Override
    public void collectionChanged(final CollectionEvent<Music> ev) {
        final Type type = ev.getType();
        log.debug("property Change -> " + type.name());
        if (CollectionEvent.Type.REMOVE.equals(type)) {
            log.info("remove music from local catalog");
            mInteface.onSongRemovedFromLocalCatalog();
        } else if (CollectionEvent.Type.ADD.equals(type)) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    log.info("add music to local catalog");
                    if (mUserScore != null) {
                        mUserScore.removePropertyChangeListener(SongDetailController.this);
                    }
                    if (mMusic != null) {
                        mMusic.removePropertyChangeListener(SongDetailController.this);
                    }
                    setMusic(ev.getItem());
                }
            });
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
        mEditCommentCommand.execute();
        //Update UI directly, since no events are triggered when a comment is deleted
        mCommentContainer.getChildren().clear();
        loadComments();
    }

    @Override
    public void onCommentDeletionRequest(final Comment comment) {
        log.debug("comment deletion requested !");
        mRemoveCommentCommand.setCommentId(comment.getIndex());
        mRemoveCommentCommand.setMusic(mMusic);
        mRemoveCommentCommand.setPeer(mAppModel.getProfile().getUserInfo().toPeer());
        mRemoveCommentCommand.execute();
        //Update UI directly, since no events are triggered when a comment is deleted
        mCommentContainer.getChildren().clear();
        loadComments();
    }

    @Override
    public void onTagDeleted(String tag) {
        log.debug("delete Tag : " + tag);
        mRemoveTagCommand.setMusic(mMusic);
        mRemoveTagCommand.setTag(tag);
        mRemoveTagCommand.execute();
        mTagContainer.getChildren().clear();
        loadTags();
    }

    public interface ISongDetailController extends ISongListController {

        public void onSongRemovedFromLocalCatalog();

        public void onTagFilterRequested(String tagName);
    }
}

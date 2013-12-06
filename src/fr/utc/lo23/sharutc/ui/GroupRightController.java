/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.profile.ManageRightsCommand;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Category;
import fr.utc.lo23.sharutc.ui.custom.HorizontalScrollHandler;
import fr.utc.lo23.sharutc.ui.custom.card.RightCard;
import fr.utc.lo23.sharutc.ui.custom.card.SimpleCard;
import fr.utc.lo23.sharutc.ui.custom.card.SongCardRight;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author shima
 */
public class GroupRightController implements Initializable, SongCardRight.ISongCardRight, RightCard.IRightCard {


    @Inject
    private ManageRightsCommand rightCommand;
    @FXML
    public FlowPane songsContainer;
    @FXML
    public FlowPane rightContainer;
    @FXML
    public ScrollPane rightScrollPane;
    @FXML
    public Label Group;
    private Category currentCategory;
    private static final Logger log = LoggerFactory.getLogger(GroupRightController.class);

    private SimpleCard mAllSongCard;
    private RightCard mReadSongCard;
    private RightCard mListenSongCard;
    private RightCard mCommentAndNoteSongCard;
    private RightCard mAllRightsSongCard;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rightScrollPane.getStyleClass().add("myScrollPaneWithTopBorder");
        HorizontalScrollHandler scrollHandler = new HorizontalScrollHandler(rightScrollPane);

        for (int i = 0; i < 20; i++) {
            final Music m = new Music();
            m.setTitle("Music " + i);
            m.setArtist("Artist" + i);
            SongCardRight newCard = new SongCardRight(m, this, false, true, false, true);
            songsContainer.getChildren().add(newCard);
        }
        // TODO
        drawRightCard();
    }


    public void setGroupInfo(Category category) {

        Group.setText(category.getName());
        currentCategory = category;
    }


    public void handleCheckBoxListenClicked(Music m, boolean boxstate) {

        rightCommand.setCategory(currentCategory);
        rightCommand.setMusic(m);
        if (boxstate) {
            log.info("Goup:" + Group.getText() + " gives maylisten rights on music :" + m.getTitle());
            rightCommand.setMayListen(true);
        } else {
            log.info("Goup:" + Group.getText() + " removes maylisten rights on music :" + m.getTitle());
            rightCommand.setMayListen(false);
        }
        rightCommand.execute();
    }

    public void handleCheckBoxCommentClicked(Music m, boolean boxstate) {
        rightCommand.setCategory(currentCategory);
        rightCommand.setMusic(m);
        if (boxstate) {
            log.info("Goup:" + Group.getText() + " gives Comment rights on music :" + m.getTitle());
            rightCommand.setMayCommentAndScore(true);
        } else {
            log.info("Goup:" + Group.getText() + " removes Comment rights on music :" + m.getTitle());
            rightCommand.setMayCommentAndScore(false);
        }
        rightCommand.execute();
    }

    public void handleCheckBoxReadClicked(Music m, boolean boxstate) {
        rightCommand.setCategory(currentCategory);
        rightCommand.setMusic(m);
        if (boxstate) {
            log.info("Goup:" + Group.getText() + " gives ReadInfo rights on music :" + m.getTitle());
            rightCommand.setMayReadInfo(true);
        } else {
            log.info("Goup:" + Group.getText() + " removes ReadInfo rights on music :" + m.getTitle());
            rightCommand.setMayReadInfo(false);
        }
        rightCommand.execute();
    }

    /**
     * display right card
     */
    private void drawRightCard() {

        final Label allSongLabel = new Label("Songs");
        allSongLabel.getStyleClass().add("largeTextGreyBold");
        mAllSongCard = new SimpleCard("/fr/utc/lo23/sharutc/ui/fxml/simple_card.fxml",
                180, 101, Pos.CENTER);
        mAllSongCard.getChildren().add(allSongLabel);

        mReadSongCard = new RightCard("Read", this);

        mListenSongCard = new RightCard("Listen", this);

        mCommentAndNoteSongCard = new RightCard("Note", this);

        mAllRightsSongCard = new RightCard("All Right", this);

        rightContainer.getChildren().addAll(mAllSongCard, mReadSongCard, mListenSongCard, mCommentAndNoteSongCard,
                mAllRightsSongCard);
    }

    @Override
    public void onSongAdded(RightCard card) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.controler.command.profile.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import fr.utc.lo23.sharutc.model.userdata.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.utc.lo23.sharutc.ui.custom.card.SongCardRight;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

/**
 * FXML Controller class
 *
 * @author shima
 */
public class GroupRightController implements Initializable, SongCardRight.ISongCardRight {

    
    @Inject
    private ManageRightsCommand rightCommand;
    @FXML
    public FlowPane songsContainer;
    @FXML
    public Label Group;
    private Category currentCategory;
    private static final Logger log = LoggerFactory.getLogger(GroupRightController.class);
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
          for (int i = 0; i < 20; i++) {
            final Music m = new Music();
            m.setTitle("Music " + i);
            m.setArtist("Artist" + i);
            SongCardRight newCard = new SongCardRight(m,this,false,true,false,true);
            songsContainer.getChildren().add(newCard);
        }
        // TODO
    }
    
    
    public void setGroupInfo(Category category) {
 
        Group.setText(category.getName());
        currentCategory=category;
    }
    
    
    
    public void handleCheckBoxListenClicked(Music m,boolean boxstate) {
        
        rightCommand.setCategory(currentCategory);
        rightCommand.setMusic(m);
        if (boxstate){
            log.info("Goup:" + Group.getText()+" gives maylisten rights on music :"+m.getTitle());
            rightCommand.setMayListen(true);
            }
        
        else {
            log.info("Goup:" + Group.getText()+" removes maylisten rights on music :"+m.getTitle());
            rightCommand.setMayListen(false);
            }
        rightCommand.execute();
    }
    public void handleCheckBoxCommentClicked(Music m,boolean boxstate) {
        rightCommand.setCategory(currentCategory);
        rightCommand.setMusic(m);
        if (boxstate){
            log.info("Goup:" + Group.getText()+" gives Comment rights on music :"+m.getTitle());
            rightCommand.setMayCommentAndScore(true);
        }
        else{
            log.info("Goup:" + Group.getText()+" removes Comment rights on music :"+m.getTitle());
            rightCommand.setMayCommentAndScore(false);
        }
        rightCommand.execute();
    }
    public void handleCheckBoxReadClicked(Music m,boolean boxstate) {
        rightCommand.setCategory(currentCategory);
        rightCommand.setMusic(m);
        if (boxstate){
            log.info("Goup:" + Group.getText()+" gives ReadInfo rights on music :"+m.getTitle());
            rightCommand.setMayReadInfo(true);
        }
        else{
            log.info("Goup:" + Group.getText()+" removes ReadInfo rights on music :"+m.getTitle());
            rightCommand.setMayReadInfo(false);
        }
        rightCommand.execute();
    }
}

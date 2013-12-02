/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.domain.Music;
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

    @FXML
    public FlowPane songsContainer;
    @FXML
    public Label Group;
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
            SongCardRight newCard = new SongCardRight(m,this,false);
            songsContainer.getChildren().add(newCard);
        }
        // TODO
    }
    
    
    public void setGroupInfo(Category category) {
 
        Group.setText(category.getName());
    }
    
    public void handleCheckBoxEditClicked(Music m,boolean boxstate) {
        if (boxstate)log.info("Goup:" + Group.getText()+"gives E rights on music :"+m.getTitle());
        else log.info("Goup:" + Group.getText()+"removes E rights on music :"+m.getTitle());
    }
    public void handleCheckBoxCommentClicked(Music m,boolean boxstate) {
        if (boxstate)log.info("Goup:" + Group.getText()+"gives C rights on music :"+m.getTitle());
        else log.info("Goup:" + Group.getText()+"removes C rights on music :"+m.getTitle());
    }
    public void handleCheckBoxReadClicked(Music m,boolean boxstate) {
        if (boxstate)log.info("Goup:" + Group.getText()+"gives R rights on music :"+m.getTitle());
        else log.info("Goup:" + Group.getText()+"removes R rights on music :"+m.getTitle());
    }
}

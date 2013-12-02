/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.ui.custom;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import fr.utc.lo23.sharutc.ui.custom.card.SimpleCard;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CardList extends VBox {

    static final Logger log = Logger.getLogger(CardList.class.getName());

    //private final static int MIN_CARD = 4;
    private boolean showAll = false;

    public Label boxtitle;
    public FlowPane content;
    public Button morebt;
    private List<SimpleCard> childs = new ArrayList<SimpleCard>();

    public CardList(String title, String styleClass) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fr/utc/lo23/sharutc/ui/fxml/card_list.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            boxtitle.setText(title);
            morebt.getStyleClass().add(styleClass);
            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
       
    }
    
    

    @FXML
    private void handleMenuButtonAction(ActionEvent event) throws IOException {
        if (event.getSource() == morebt) {
            showAll = !showAll;
            refreshListVisibility();
        }
    }
    
    private int getLineCardCount(){
        int count = ((int) content.getWidth())/SimpleCard.WIDTH;
        if(count == 0)
            count = 3;
        return count ;
    }

    
    public void addChild(SimpleCard card) {
        childs.add(card);

        if (showAll || content.getChildren().size() < getLineCardCount()) {
            content.getChildren().add(card);
        }
        refreshListVisibility();

    }

    public void clear(){
       childs.clear();
       content.getChildren().clear();
    }
    
    private void refreshListVisibility() {
        if (showAll) {
            for (int i = content.getChildren().size(); i < childs.size(); i++) {

                content.getChildren().add(childs.get(i));

            }
        } else {
            for (int i = content.getChildren().size() - 1; i >= getLineCardCount(); i--) {

                content.getChildren().remove(i);
            }

        }

    }

}

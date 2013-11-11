/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.ui.widget;

import fr.utc.lo23.sharutc.ui.custom.SimpleCard;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class CardList implements Initializable {

    static final Logger log = Logger.getLogger(CardList.class.getName());

    private final static int MIN_CARD = 4;
    private boolean showAll = false;

    private String title;
    public Text boxtitle;
    public FlowPane content;
    public Button morebt;
    private List<SimpleCard> childs = new ArrayList<SimpleCard>();

    public CardList(String title) {
        this.title = title;
    }

    public Pane buildPane() {
        Pane pane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/card_list.fxml"));
            loader.setController(this);
            pane = (Pane) loader.load();

        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return pane;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boxtitle.setText(title);

    }

    @FXML
    private void handleMenuButtonAction(ActionEvent event) throws IOException {
        if (event.getSource() == morebt) {
            showAll = !showAll;
            refreshListVisibility();
        }
    }

    public void addChild(SimpleCard card) {
        childs.add(card);

        if (showAll || content.getChildren().size() < MIN_CARD) {
            content.getChildren().add(card);
        }

    }

    private void refreshListVisibility() {
        if (showAll) {
            for (int i = content.getChildren().size(); i < childs.size(); i++) {

                content.getChildren().add(childs.get(i));

            }
        } else {
            for (int i = content.getChildren().size() - 1; i >= MIN_CARD; i--) {

                content.getChildren().remove(i);
            }

        }

    }

}

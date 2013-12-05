/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.ui.custom;

import fr.utc.lo23.sharutc.ui.custom.card.SimpleCard;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CardList extends VBox {

    private static final Logger log = LoggerFactory.getLogger(CardList.class);
    //private final static int MIN_CARD = 4;
    private boolean mShowAll = false;
    public Label boxtitle;
    public FlowPane content;
    public Button morebt;
    private List<SimpleCard> mChildren = new ArrayList<SimpleCard>();

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
            mShowAll = !mShowAll;
            refreshListVisibility();
        }
    }

    private int getLineCardCount() {
        int count = ((int) content.getWidth()) / SimpleCard.WIDTH;
        if (count == 0) {
            count = 3;
        }
        return count;
    }

    public void addChild(SimpleCard card) {
        mChildren.add(card);
        log.info("add child to content");
        refreshListVisibility();
        log.info("add child to content -- bis");
    }

    public void clear() {
        mChildren.clear();
        content.getChildren().clear();
    }

    private void refreshListVisibility() {
        content.getChildren().clear();
        int numberOfShowChildren = Math.min(getLineCardCount(), mChildren.size());
        if (mShowAll) {
            numberOfShowChildren = mChildren.size();
        }
        for (int i = content.getChildren().size(); i < numberOfShowChildren; i++) {
            content.getChildren().add(mChildren.get(i));
        }
    }
}

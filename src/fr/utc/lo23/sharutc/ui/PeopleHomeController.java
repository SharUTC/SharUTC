package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.ui.widget.ItemBox;
import fr.utc.lo23.sharutc.ui.widget.ItemsList;
import fr.utc.lo23.sharutc.ui.widget.SearchResultItemBox;
import fr.utc.lo23.sharutc.ui.widget.SearchResultItemsList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class PeopleHomeController implements Initializable {

    @FXML
    public VBox people;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ItemsList list = new ItemsList();
        people.getChildren().add(list.buildPane());

        //TODO make specific fxml and widget
        for (int i = 0; i < 50; i++) {
            list.addChild(new ItemBox("Paul Kalkbrenner " + i));
        }
    }

}

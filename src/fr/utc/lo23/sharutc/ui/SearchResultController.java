package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.ui.widget.ItemBox;
import fr.utc.lo23.sharutc.ui.widget.ItemsList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;

public class SearchResultController implements Initializable {
    public VBox gridpane;
    private String search;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (resourceBundle != null) {
            search = resourceBundle.getString("search");
        } else {
            search = "";
        }
       
        
        ItemsList list = new ItemsList("album");
         gridpane.getChildren().add(list.buildPane());
        
        list.addChild(new ItemBox("artiste1", "soustitre 1"));
        list.addChild(new ItemBox("artiste2", "soustitre 2"));
        
          list = new ItemsList("album");
         gridpane.getChildren().add(list.buildPane());
        
        list.addChild(new ItemBox("artiste1", "soustitre 1"));
        list.addChild(new ItemBox("artiste2", "soustitre 2"));
        
    }
}

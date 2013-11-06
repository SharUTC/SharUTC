package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.ui.widget.SearchResultItemBox;
import fr.utc.lo23.sharutc.ui.widget.SearchResultItemsList;
import javafx.fxml.Initializable;

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
       
        
        SearchResultItemsList list = new SearchResultItemsList("album");
         gridpane.getChildren().add(list.buildPane());
        
        list.addChild(new SearchResultItemBox("artiste1", "soustitre 1"));
        list.addChild(new SearchResultItemBox("artiste2", "soustitre 2"));
        
          list = new SearchResultItemsList("album");
         gridpane.getChildren().add(list.buildPane());
        
        list.addChild(new SearchResultItemBox("artiste1", "soustitre 1"));
        list.addChild(new SearchResultItemBox("artiste2", "soustitre 2"));
        
    }
}

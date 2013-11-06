/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.lo23.sharutc.ui.widget;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 *
 * @author Florian
 */
public class SearchResultItemsList implements Initializable {

    static final Logger log = Logger.getLogger(SearchResultItemBox.class.getName());

    private String title;
    public Text boxtitle;
    public FlowPane content;


    public SearchResultItemsList(String title) {
        this.title = title;


    }
    public Pane buildPane(){
        Pane pane = null;
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/search_result_items_list.fxml"));
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

    public void addChild(SearchResultItemBox item){
        content.getChildren().add(item.buildPane());

    }

}

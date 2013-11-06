/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.ui.widget;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Florian
 */
public class SearchResultItemBox extends ItemBox {

    static final Logger log = Logger.getLogger(SearchResultItemBox.class.getName());

    protected String subTitle;
    public Text boxtitle;
    public Text boxsubtitle;

    public SearchResultItemBox(String title, String subTitle) {
        super(title);
        this.subTitle = subTitle;


    }

    public Pane buildPane() {
        Pane pane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/search_result_item_box.fxml"));
            loader.setController(this);
            pane = (Pane) loader.load();

        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return pane;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        boxsubtitle.setText(subTitle);
    }

}

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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 *
 * @author Florian
 */
public class ItemBox implements Initializable {

    static final Logger log = Logger.getLogger(ItemBox.class.getName());

    private String subTitle;
    private String title;
    public Text boxtitle;
    public Text boxsubtitle;

    public ItemBox(String title, String subTitle) {
        this.title = title;
        this.subTitle =subTitle;
        

    }

     public Pane buildPane(){
        Pane pane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/item_box.fxml"));
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
        boxsubtitle.setText(subTitle);
    }

}

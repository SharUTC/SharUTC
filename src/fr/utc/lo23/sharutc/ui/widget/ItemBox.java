/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.ui.widget;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ItemBox implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(ItemBox.class);

    protected Pane mPane;
    protected String title;
    public Text boxtitle;

    public ItemBox(String title) {
        mPane = null;
        this.title = title;

    }

    public Pane buildPane(String fxmlResource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlResource));
            loader.setController(this);
            mPane = (Pane) loader.load();
        } catch (IOException ex) {
            log.warn(ex.getMessage());
        }
        return mPane;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boxtitle.setText(title);
    }

}

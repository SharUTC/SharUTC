/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc.ui.widget;

import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * @author Florian
 */
public class SearchResultItemBox extends ItemBox {

    static final Logger log = Logger.getLogger(SearchResultItemBox.class.getName());

    protected String subTitle;
    public Text boxsubtitle;

    public SearchResultItemBox(String title, String subTitle) {
        super(title);
        this.subTitle = subTitle;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        boxsubtitle.setText(subTitle);
    }

}

package fr.utc.lo23.sharutc.ui;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchResultController implements Initializable {
    public Label title;
    private String search;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (resourceBundle != null) {
            search = resourceBundle.getString("search");
        } else {
            search = "";
        }
        title.setText("Search results for : " + search);
    }
}

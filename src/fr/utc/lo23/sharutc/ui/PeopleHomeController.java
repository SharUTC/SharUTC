package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.ui.custom.DynamicGridPane;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class PeopleHomeController implements Initializable {

    public VBox gridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initializeDynamicGridPane(Scene s) {
        ObservableList<Node> children = gridPane.getChildren();
        children.clear();
        DynamicGridPane d = new DynamicGridPane(s.widthProperty());
        children.add(d);
    }

}

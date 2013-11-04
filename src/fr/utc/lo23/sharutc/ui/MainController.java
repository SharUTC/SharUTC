package fr.utc.lo23.sharutc.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class MainController implements Initializable {

    public Button songsbutton;
    public Button peoplebutton;
    public Button artistsbutton;
    public Button albumsbutton;
    public Pane rightpane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void handleMenuButtonAction(ActionEvent event) throws IOException {

        ObservableList<Node> children = rightpane.getChildren();
        children.clear();

        if(event.getSource() == songsbutton) {
            children.add((Node) FXMLLoader.load(getClass().getResource("fxml/songs_detail.fxml")));
        }
        else if (event.getSource() == peoplebutton) {
            children.add((Node) FXMLLoader.load(getClass().getResource("fxml/people_detail.fxml")));
        }
        else if (event.getSource() == artistsbutton) {
            children.add((Node) FXMLLoader.load(getClass().getResource("fxml/artists_detail.fxml")));
        }
        else if (event.getSource() == albumsbutton) {
            children.add((Node) FXMLLoader.load(getClass().getResource("fxml/albums_detail.fxml")));
        }
    }

    @FXML
    public void handleTextEntered(ActionEvent actionEvent) throws IOException {
        ObservableList<Node> children = rightpane.getChildren();
        children.clear();
        children.add((Node) FXMLLoader.load(getClass().getResource("fxml/searchresult_detail.fxml")));
    }
}

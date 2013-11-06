package fr.utc.lo23.sharutc.ui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public Button songsbutton;
    public Button peoplebutton;
    public Button artistsbutton;
    public Button albumsbutton;
    public Pane rightpane;
    private Scene mScene;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void handleMenuButtonAction(ActionEvent event) throws IOException {

        ObservableList<Node> children = rightpane.getChildren();
        children.clear();

        if (event.getSource() == songsbutton) {
            children.add((Node) FXMLLoader.load(getClass().getResource("fxml/songs_detail.fxml")));
        } else if (event.getSource() == peoplebutton) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/people_home.fxml"));
            // has to be loaded before getting the controller
            children.add((Node) loader.load());
            //retrieve the controller
            PeopleHomeController controller = loader.<PeopleHomeController>getController();
            //call any public method here
            controller.initializeDynamicGridPane(mScene);
        } else if (event.getSource() == artistsbutton) {
            children.add((Node) FXMLLoader.load(getClass().getResource("fxml/artists_detail.fxml")));
        } else if (event.getSource() == albumsbutton) {
            children.add((Node) FXMLLoader.load(getClass().getResource("fxml/albums_detail.fxml")));
        }
    }

    @FXML
    public void handleTextEntered(ActionEvent actionEvent) throws IOException {
        ObservableList<Node> children = rightpane.getChildren();
        children.clear();
        children.add((Node) FXMLLoader.load(getClass().getResource("fxml/searchresult_detail.fxml")));
    }

    public void setScene(Scene s) {
        System.out.println("Scene set");
        mScene = s;
    }
}

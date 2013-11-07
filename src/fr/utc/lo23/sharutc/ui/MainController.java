package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, PeopleHomeController.IPeopleHomeController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    public Button songsbutton;
    public Button peoplebutton;
    public Button artistsbutton;
    public Button albumsbutton;
    public Pane rightpane;
    public HBox bottombar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            bottombar.getChildren().add((Node) FXMLLoader.load(getClass().getResource("fxml/player.fxml")));
        } catch (IOException exception) {
        }
    }

    @FXML
    private void handleMenuButtonAction(ActionEvent event) throws IOException {

        ObservableList<Node> children = rightpane.getChildren();
        children.clear();

        if (event.getSource() == songsbutton) {
            children.add((Node) FXMLLoader.load(getClass().getResource("fxml/songs_detail.fxml")));
        } else if (event.getSource() == peoplebutton) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/people_home.fxml"));
            loader.setController(new PeopleHomeController(this));
            children.add((Node) loader.load());
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


    @Override
    public void onPeopleDetailRequested(UserInfo user) {
        ObservableList<Node> children = rightpane.getChildren();
        children.clear();
        log.info("people detail requested : " + user.getLogin());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/people_detail.fxml"));
        loader.setController(new PeopleDetailController(user));
        try {
            children.add((Node) loader.load());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onGroupDetailRequested() {
    }
}

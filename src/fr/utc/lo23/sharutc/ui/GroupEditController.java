package fr.utc.lo23.sharutc.ui;

import fr.utc.lo23.sharutc.model.userdata.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author shima
 */
public class GroupEditController implements Initializable {


    @FXML
    public Label Group;
    @FXML
    TextField NewNameGroup;

    private static final Logger log = LoggerFactory.getLogger(GroupEditController.class);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {


    }
    // TODO


    public void setGroupInfo(Category category) {

        Group.setText(category.getName());
    }

    public void handleNewGroupNameEntered(ActionEvent event) throws IOException {
        log.info(Group.getText() + " as been changed to " + NewNameGroup.getText());
        Group.setText(NewNameGroup.getText());
    }


}

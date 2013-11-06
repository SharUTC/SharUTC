
package fr.utc.lo23.sharutc.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));

        Scene scene = new Scene((Pane) loader.load());
        scene.getStylesheets().add(this.getClass().getResource("css/main.css").toExternalForm());
        stage.setScene(scene);

        MainController controller = loader.<MainController>getController();
        //call any public method of your controller
        controller.setScene(scene);

        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
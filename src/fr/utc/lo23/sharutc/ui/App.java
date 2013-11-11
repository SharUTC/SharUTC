package fr.utc.lo23.sharutc.ui;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.google.inject.Module;
import fr.utc.lo23.sharutc.injection.CommandModule;
import fr.utc.lo23.sharutc.injection.ModelModule;
import fr.utc.lo23.sharutc.injection.ServiceModule;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class App extends GuiceApplication {

    private static final Logger log = LoggerFactory
            .getLogger(App.class);
    @Inject
    private GuiceFXMLLoader mFxmlLoader;

    @Override
    public void start(Stage stage) throws Exception {

        final GuiceFXMLLoader.Result loadingResult = mFxmlLoader.load(getClass().getResource("fxml/main.fxml"));
        final Parent root = loadingResult.getRoot();

        Scene scene = new Scene((Parent) root);
        scene.getStylesheets().add(this.getClass().getResource("css/main.css").toExternalForm());
        stage.setScene(scene);

        //If needed, cast your controller
        //and call any public method here
        ((MainController) loadingResult.getController()).sceneCreated();

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
        log.info("Application STARTED");
        launch(args);
        log.info("Application FINISHED");
    }

    @Override
    public void init(List<Module> appModules) throws Exception {
        appModules.add(new ModelModule());
        appModules.add(new ServiceModule());
        appModules.add(new CommandModule());
    }
}
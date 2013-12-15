package fr.utc.lo23.sharutc.ui;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.google.inject.Module;
import fr.utc.lo23.sharutc.injection.CommandModule;
import fr.utc.lo23.sharutc.injection.ModelModule;
import fr.utc.lo23.sharutc.injection.ServiceModule;
import fr.utc.lo23.sharutc.ui.navigation.NavigationController;
import fr.utc.lo23.sharutc.ui.navigation.NavigationHandler;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import javafx.scene.image.Image;

public class App extends GuiceApplication implements NavigationHandler {

    private static final Logger log = LoggerFactory
            .getLogger(App.class);
    @Inject
    private GuiceFXMLLoader mFxmlLoader;
    private Stage mStage;
    private Scene mScene;
    private NavigationController mCurrentNavigationController;

    @Override
    public void start(Stage stage) throws Exception {
        mStage = stage;
        mStage.setTitle("SharUTC");
        mStage.getIcons().add(new Image("fr/utc/lo23/sharutc/ui/drawable/ic_icon.png"));
        goToLoginPage();
    }

    @Override
    public void goToLoginPage() {
        goTo("/fr/utc/lo23/sharutc/ui/fxml/login.fxml");
        //Use for an automatic login
        //((LoginController)mCurrentNavigationController).automaticLogin("florian", "florian");
    }

    @Override
    public void goToRegistrationPage() {
        goTo("/fr/utc/lo23/sharutc/ui/fxml/registration.fxml");
    }

    @Override
    public void goToMainPage() {
        goTo("/fr/utc/lo23/sharutc/ui/fxml/main.fxml");
        ((MainController) mCurrentNavigationController).sceneCreated();
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

    @Override
    public void stop() throws Exception {
        super.stop();
        if (mCurrentNavigationController instanceof MainController) {
            ((MainController) mCurrentNavigationController).close();
        }
    }

    private void goTo(String url) {
        try {
            final GuiceFXMLLoader.Result loadingResult = mFxmlLoader.load(getClass().getResource(url));
            final Parent root = loadingResult.getRoot();
            mCurrentNavigationController = (NavigationController) loadingResult.getController();
            mCurrentNavigationController.setNavigationHandler(this);
            if (mScene == null) {
                mScene = new Scene(root);

                mScene.getStylesheets().add(this.getClass().getResource("/fr/utc/lo23/sharutc/ui/css/main.css").toExternalForm());
                mStage.setScene(mScene);
                mStage.show();
            } else {
                mScene.setRoot(root);
            }
        } catch (IOException ex) {
            log.error("goto : " + url, ex);
        }

    }
}
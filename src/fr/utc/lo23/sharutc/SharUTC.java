package fr.utc.lo23.sharutc;

import fr.utc.lo23.sharutc.injection.ServiceModule;
import fr.utc.lo23.sharutc.injection.ModelModule;
import fr.utc.lo23.sharutc.injection.CommandModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
 import javafx.application.Application;
 import javafx.event.ActionEvent;
 import javafx.event.EventHandler;
 import javafx.scene.Scene;
 import javafx.scene.control.Button;
 import javafx.scene.layout.StackPane;
 import javafx.stage.Stage;
 */

/**
 *
 */
public class SharUTC /*extends Application*/ {

    private static final Logger log = LoggerFactory
            .getLogger(SharUTC.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        log.info("Application STARTED");
        injectDependencies();

        startView();

        log.info("Application FINISHED");
    }

    private static void injectDependencies() {
        List<Module> appModules = new ArrayList<Module>();
        appModules.add(new ModelModule());
        appModules.add(new ServiceModule());
        appModules.add(new CommandModule());
        Guice.createInjector(appModules);
    }

    private static void startView() {
        // launch(args);
    }
    /*
     @Override
     public void start(Stage primaryStage) {
     Button btn = new Button();
     btn.setText("Say 'Hello World'");
     btn.setOnAction(new EventHandler<ActionEvent>() {
     @Override
     public void handle(ActionEvent event) {
     System.out.println("Hello World!");
     }
     });

     StackPane root = new StackPane();
     root.getChildren().add(btn);

     Scene scene = new Scene(root, 300, 250);

     primaryStage.setTitle("Hello World!");
     primaryStage.setScene(scene);
     primaryStage.show();
     }
     */
}

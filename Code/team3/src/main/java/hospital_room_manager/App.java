package hospital_room_manager;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

//Used to start the JavaFX app, switching between FXML screens. 
public class App extends Application {

    private static Scene scene;

    @Override
    //It loads the first screen, then applies the stylesheet
    public void start(Stage stage) throws IOException {
        Parent root = loadFXML("login");
        scene = new Scene(root, 640, 480);

        if (root instanceof Region) {
            Region region = (Region) root;
            region.prefWidthProperty().bind(scene.widthProperty());
            region.prefHeightProperty().bind(scene.heightProperty());
        }

        String css = App.class.getResource("styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Hospital Room Manager");
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.show();
    }

    //swaps the page with no need of restarting the app.
    public static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);
    }

    //Loads FXML file from the resources folder
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    //The ordinary Java entry point. 
    public static void main(String[] args) {
        launch();
    }
}
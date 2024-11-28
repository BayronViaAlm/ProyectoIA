import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TestFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Test JavaFX");
        primaryStage.setScene(new Scene(new Label("JavaFX is working!"), 300, 200));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

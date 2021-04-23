package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.utils.Constant;
import sample.utils.MinWindowManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle(Constant.APP_NAME);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream(Constant.ICO_PATH)));
        primaryStage.setScene(new Scene(root, 768, 400));
        primaryStage.show();
        MinWindowManager.getInstance().listen(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

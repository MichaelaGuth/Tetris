package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.menu.Controller;
import sample.obrazky.ImageLoader;

public class Main extends Application {
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("menu/menu.fxml"));
        primaryStage.setTitle("Tetris");
        primaryStage.setScene(new Scene(root, 600, 800));
        primaryStage.getIcons().add(ImageLoader.LoadImage("Icon.png"));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

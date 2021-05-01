package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("fxml/Main.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("fxml/Main.fxml"));
        primaryStage.setTitle("MILK TEA STORE");
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();


        /*GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                Button btn = new Button(i + "" + j);
                btn.setPrefSize(60, 50);
                grid.add(btn, j, i);
            }
        }
        ScrollPane scr = new ScrollPane(grid);
        primaryStage.setScene(new Scene(scr));
        primaryStage.setHeight(600);
        primaryStage.setWidth(500);
        primaryStage.show();*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}

package com.vvmarkets;

import javafx.application.Application;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.print.Printer;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        TextArea textArea = new TextArea();
        Button button = new Button("Get all printers");
        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        ObservableSet<Printer> printers = Printer.getAllPrinters();

                        for (Printer printer :
                                printers) {
                            textArea.appendText(printer.getName() + "\n");
                        }
                    }
                }
        );

        // Create the VBox with a 10px spacing
        VBox root = new VBox(10);
        // Add the Children to the VBox
        root.getChildren().addAll(button,textArea);
        // Set the Size of the VBox
        root.setPrefSize(400, 250);
        // Set the Style-properties of the VBox
        root.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");

        // Create the Scene
        Scene scene = new Scene(root);
        // Add the scene to the Stage
        primaryStage.setScene(scene);
        // Set the title of the Stage
        primaryStage.setTitle("Showing all Printers");
        // Display the Stage
        primaryStage.show();

//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();



    }


    public static void main(String[] args) {
        launch(args);
    }
}

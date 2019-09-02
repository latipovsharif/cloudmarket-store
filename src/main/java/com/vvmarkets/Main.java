package com.vvmarkets;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import javafx.application.Application;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.print.Printer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main extends Application {

    private static final Logger log = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception{

        log.debug("Starting application");

        TextArea textArea = new TextArea();
        Button button = new Button("Get all printers");
        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        ObservableSet<Printer> printers = Printer.getAllPrinters();
                        Printer defaultPrinter = Printer.getDefaultPrinter();

                        for (Printer printer :
                                printers) {

                            textArea.appendText(printer.getName() + "\n");
                        }
                    }
                }
        );


        Observable<String> observable = Observable.fromArray("one", "two", "three");
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                log.info("subscribed");
            }

            @Override
            public void onNext(String s) {
                log.info("next");
            }

            @Override
            public void onError(Throwable e) {
                log.info("error");
            }

            @Override
            public void onComplete() {
                log.info("completed");
            }
        };

        observable.subscribe(observer);

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
        primaryStage.setScene(scene);
        setPrimaryStageAttrs(primaryStage);
        primaryStage.show();

//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


    private void setPrimaryStageAttrs(Stage primaryStage) {
        primaryStage.setTitle("Showing all Printers");
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }
}

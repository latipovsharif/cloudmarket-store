package com.vvmarkets;

import com.vvmarkets.dao.Product;
import com.vvmarkets.presenters.KeyboardPresenter;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class Main extends Application {

    private static final Logger log = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception{

        log.debug("Starting application");
//
//        TextArea textArea = new TextArea();
//        Button button = new Button("Get all printers");
//        button.setOnAction(
//                new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent actionEvent) {
//                        ObservableSet<Printer> printers = Printer.getAllPrinters();
//                        Printer defaultPrinter = Printer.getDefaultPrinter();
//
//                        for (Printer printer :
//                                printers) {
//
//                            textArea.appendText(printer.getName() + "\n");
//                        }
//                    }
//                }
//        );
//
//
//        Observable<String> observable = Observable.fromArray("one", "two", "three");
//        Observer<String> observer = new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                log.info("subscribed");
//            }
//
//            @Override
//            public void onNext(String s) {
//                log.info("next");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                log.info("error");
//            }
//
//            @Override
//            public void onComplete() {
//                log.info("completed");
//            }
//        };
//
//        observable.subscribe(observer);

        // Create the VBox with a 10px spacing
//        VBox root = new VBox(10);
//        // Add the Children to the VBox
//        root.getChildren().addAll(button,textArea);
//        // Set the Size of the VBox
//        root.setPrefSize(400, 250);
//        // Set the Style-properties of the VBox
//        root.setStyle("-fx-padding: 10;" +
//                "-fx-border-style: solid inside;" +
//                "-fx-border-width: 2;" +
//                "-fx-border-insets: 5;" +
//                "-fx-border-radius: 5;" +
//                "-fx-border-color: blue;");

//         Create the Scene
//        Scene scene = new Scene(root);
//        primaryStage.setScene(scene);
//        setPrimaryStageAttrs(primaryStage);
//        primaryStage.show();

        List<Product> products = Product.GetProducts();
        for (Product p : products) {
            System.out.println(p.getId());
        }

        Parent root = null;
        KeyboardPresenter keyboard = new KeyboardPresenter();
        root = keyboard.getView();
        primaryStage.setScene(new Scene(root, 300, 275));
        setPrimaryStageAttrs(primaryStage);
        primaryStage.show();
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

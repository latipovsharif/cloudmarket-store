package com.vvmarkets;

import com.vvmarkets.dao.Product;
import com.vvmarkets.presenters.LogInPresenter;
import com.vvmarkets.presenters.MainPresenter;
import com.vvmarkets.services.ProductService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseList;
import io.reactivex.internal.observers.BlockingBaseObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Main extends Application {

    private static final Logger log = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.setProperty("java.net.useSystemProxies", "true");
        log.debug("Starting application");

        ProductService productService = RestClient.getClient().create(ProductService.class);
        Call<ResponseList<Product>> listProductCall = productService.productList();
        listProductCall.enqueue(new Callback<ResponseList<Product>>() {
            @Override
            public void onResponse(Call<ResponseList<Product>> call, Response<ResponseList<Product>> response) {
                log.info(response);
                log.info(response.code());
                log.info(response.body().getList());
                log.info(response.body().getItemPerPage());
            }

            @Override
            public void onFailure(Call<ResponseList<Product>> call, Throwable t) {
                log.error(t.getMessage());
            }
        });

        Parent root = null;
        LogInPresenter logIn = new LogInPresenter();
        root = logIn.getView();

        logIn.controller.signedIn.subscribe(new BlockingBaseObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    Platform.runLater(
                        () -> {
                            try {
                                MainPresenter main = new MainPresenter();
                                primaryStage.setScene(new Scene(main.getView()));
                                primaryStage.setFullScreen(true);
                            } catch (Exception e) {
                                log.error("cannot set main scene:" + e);
                            }
                        }
                    );
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });

        primaryStage.setScene(new Scene(root));
        setPrimaryStageAttrs(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setPrimaryStageAttrs(Stage primaryStage) {
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }
}

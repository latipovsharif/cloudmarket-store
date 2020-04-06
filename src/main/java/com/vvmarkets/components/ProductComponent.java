package com.vvmarkets.components;

import com.vvmarkets.core.IListContent;
import com.vvmarkets.core.ListUtil;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.ProductProperties;
import com.vvmarkets.services.ProductService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseBody;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProductComponent extends VBox {

    public IListContent getProduct() {
        return product;
    }

    public void setProduct(IListContent product) {
        this.product = product;
    }

    private IListContent product;

    public ProductComponent(IListContent product) {
        this.product = product;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/productComponent.fxml"));
            Parent root = loader.load();
            ProductComponentController controller = loader.getController();
            controller.setProduct(product);
            this.getChildren().add(root);
            
        } catch (Exception e) {
            Utils.logException(e, "cannot load fxml for quantity dialog");
        }
    }

    public static List<ProductComponent> getList() {
        return listToComponent(ListUtil.INSTANCE.fillMain(), false);
    }

    public static List<ProductComponent> getList(String productId) {
        return listToComponent(ListUtil.INSTANCE.listForCategory(productId, false), true);
    }

    public static List<ProductComponent> getSearchList(String searchString) {
        ProductService productService = RestClient.getClient().create(ProductService.class);
        Call<ResponseBody<List<ProductProperties>>> listProductForCategoryCall = productService.productSearch(searchString);

        try{
            Response<ResponseBody<List<ProductProperties>>> response = listProductForCategoryCall.execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        return listToComponent(response.body().getBody(), true);
                    }
                }
            }
        } catch (Exception e) {
            Utils.logException(e, "error while handling response of category for product list");
        }
        return new ArrayList<>();
    }

    public static List<ProductComponent> listToComponent(List<? extends IListContent> contents, boolean addBack){
        List<ProductComponent> res = new ArrayList<>();

        if (addBack) {
            var p = new ProductProperties();
            p.setName("");
            p.setThumb("images/back.png");

            ProductComponent pc = new ProductComponent(p);
            res.add(0, pc);
        }

        if (contents == null) {
            return res;
        }

        try {
            CopyOnWriteArrayList<IListContent> lst = new CopyOnWriteArrayList<>(contents);

            for (IListContent content : lst) {
                if (content == null) {
                    continue;
                }

                ProductComponent pc = new ProductComponent(content);
                res.add(pc);
            }
        }catch(Exception e) {
            Utils.logException(e, "cannot get list of content");
        }
        return res;
    }

//    private static ProductComponent getComponent(IListContent content) {
//        ProductComponent pc = new ProductComponent(content);
//        pc.setStyle("-fx-pref-width: 170px; -fx-pref-height: 200px; -fx-alignment: center; -fx-background-color: #d8f3ff");
//        ImageView iv = new ImageView();
//        iv.setImage(content.getThumb());
//        iv.prefHeight(150);
//        iv.prefWidth(150);
//        iv.setFitWidth(150);
//        iv.setFitHeight(150);
//        Label lbl = new Label(content.getName());
//        lbl.setStyle(
//                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-wrap-text: true; -fx-pref-width: 170px; -fx-pref-height: 40px");
//        pc.getChildren().add(iv);
//        pc.getChildren().add(lbl);
//        return pc;
//    }
}
